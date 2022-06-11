package com.infrastructure.tag;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.tag.Tag;
import com.domain.tag.TagRepository;
import com.domain.user.UserRepository;
import com.domain.utils.exception.ApiConflictException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.offer.OfferRepositoryImpl;
import com.infrastructure.offer.OfferTuple;
import com.infrastructure.photo.PhotoRepositoryImpl;
import com.infrastructure.user.UserRepositoryImpl;
import com.infrastructure.user.UserTuple;
import org.hashids.Hashids;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(
        classes = {HashIdImpl.class, Hashids.class, TagRepositoryImpl.class, OfferRepositoryImpl.class, UserRepositoryImpl.class, PhotoRepositoryImpl.class},
        loader = AnnotationConfigContextLoader.class
)
@EnableJpaRepositories("com.infrastructure")
@EntityScan("com.infrastructure")
@Transactional
class TagRepositoryTests {
    @Autowired
    private TagRepository repository;

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JpaOfferRepository offerRepo;

    @Autowired
    private HashId hashId;

    @BeforeEach
    void setup() {
        repository.clear();
        userRepository.create(Fixtures.user);
        offerRepository.clear();
    }

    @AfterEach
    void teardown() {
        repository.clear();
        offerRepository.clear();
    }

    @Nested
    class GetTests {
        @Test
        void get() {
            final var tag1 = new Tag("1");
            final var tag2 = new Tag("2");
            repository.create(tag1);
            repository.create(tag2);

            assertEquals(List.of(tag1, tag2), repository.getAll());
        }

        @Test
        void getAfterAssignedInOrder() {
            final var tag1 = new Tag("1");
            final var tag2 = new Tag("2");
            final var offer = new Offer(null, null, null, null, null, Optional.empty(), Fixtures.user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
            userRepository.create(Fixtures.user);
            final var createdOffer = offerRepo.saveAndFlush(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);
            repository.create(tag1);
            repository.create(tag2);

            repository.assignAllToOffer(createdOffer.getId(), Set.of(tag1));

            assertEquals(List.of(tag1, tag2), repository.getAll());
        }

        @Test
        void getAfterAssignedReversed() {
            final var tag1 = new Tag("1");
            final var tag2 = new Tag("2");
            final var offer = new Offer(null, null, null, null, null, Optional.empty(), Fixtures.user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
            userRepository.create(Fixtures.user);
            final var createdOffer = offerRepo.saveAndFlush(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);
            repository.create(tag1);
            repository.create(tag2);

            repository.assignAllToOffer(createdOffer.getId(), Set.of(tag2));

            assertEquals(List.of(tag2, tag1), repository.getAll());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void conflict() {
            final var tag = new Tag("a");
            repository.create(tag);
            assertThrows(ApiConflictException.class, () -> repository.create(tag));
        }

        @Test
        void create() {
            final var tags = List.of(new Tag("a"), new Tag("b"));
            tags.forEach(tag -> repository.create(tag));
            assertEquals(tags, repository.getAll());
        }
    }

    @Nested
    class AssignAllToOffer {
        @Test
        void offerNotFound() {
            assertThrows(ApiNotFoundException.class, () -> repository.assignAllToOffer("asdf", Set.of(new Tag("sdf"))));
        }

        @Test
        void tagNotFound() {
            userRepository.create(Fixtures.user);
            offerRepo.save(new OfferTuple(Fixtures.offer, new UserTuple(Fixtures.user)));

            assertThrows(ApiNotFoundException.class, () -> repository.assignAllToOffer("asdf", Set.of(new Tag("asdf"))));
        }

        @Test
        @Commit
        void tagAlreadyAssigned() {
            final var offer = new Offer("8", "", "", OfferStatus.OPEN, LocalDateTime.now(), Optional.empty(), Fixtures.user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
            userRepository.create(Fixtures.user);
            final var createdOffer = offerRepo.saveAndFlush(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);
            repository.create(Fixtures.tag);

            repository.assignAllToOffer(createdOffer.getId(), Set.of(Fixtures.tag));
            assertThrows(ApiConflictException.class, () -> repository.assignAllToOffer(createdOffer.getId(), Set.of(Fixtures.tag)));
        }

        @Test
        @Commit
        void assign() {
            final var offer = new Offer("9", null, null, null, null, Optional.empty(), Fixtures.user, List.of(), BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), Set.of());
            userRepository.create(Fixtures.user);
            final var createdOffer = offerRepo.saveAndFlush(new OfferTuple(offer, new UserTuple(Fixtures.user))).toDomain(hashId);
            repository.create(Fixtures.tag);

            assertEquals(Set.of(), offerRepository.findById(createdOffer.getId()).get().getTags());
            repository.assignAllToOffer(createdOffer.getId(), Set.of(Fixtures.tag));

            assertEquals(Set.of(Fixtures.tag), offerRepository.findById(createdOffer.getId()).get().getTags());
        }
    }
}
