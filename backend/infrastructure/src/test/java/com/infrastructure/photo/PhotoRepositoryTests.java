package com.infrastructure.photo;

import com.domain.offer.Offer;
import com.domain.photo.ApiImageException;
import com.domain.photo.PhotoRepository;
import com.domain.user.UserRepository;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.ApiHashIdException;
import com.domain.utils.hashId.HashId;
import com.domain.utils.hashId.HashIdImpl;
import com.infrastructure.Fixtures;
import com.infrastructure.JpaConfig;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.offer.OfferTuple;
import com.infrastructure.user.UserTuple;
import org.hashids.Hashids;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = {JpaConfig.class, HashIdImpl.class, Hashids.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class PhotoRepositoryTests {
    @Autowired
    private HashId hashId;

    @Autowired
    private PhotoRepository repository;

    @Autowired
    private JpaOfferRepository offerRepo;
    @Autowired
    private JpaPhotoRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setup() throws ApiHashIdException {
        userRepository.create(Fixtures.user);
        offerRepo.deleteAll();
        repo.deleteAll();
    }

    @AfterEach
    void cleanup() {
        offerRepo.deleteAll();
        userRepository.clear();
        repo.deleteAll();
    }

    @Nested
    class SavePhotoTests {
        @Test
        void throwOnIncorrectId() {
            final var id = "a";
            assertThrows(ApiImageException.class, () -> repository.savePhoto(id, null));
        }

        @Test
        void throwOnNonExistingOffer() {
            assertThrows(ApiNotFoundException.class, () -> repository.savePhoto("a", Fixtures.photo));
        }

        @Test
        void throwOnImageException() {
            assertThrows(ApiImageException.class, () -> repository.savePhoto(null, null));
        }

        @Test
        void save() {
            final var offer = new OfferTuple(new Offer("", "", Fixtures.user, null, Set.of()), new UserTuple(Fixtures.user));
            final var offerSaved = offerRepo.save(offer);

            final var saved = repository.savePhoto(hashId.encode(offerSaved.getId()), Fixtures.photo);

            repository.exists(saved.getId());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void delete() {
            final var offer = new OfferTuple(new Offer("", "", Fixtures.user, null, Set.of()), new UserTuple(Fixtures.user));
            final var id = hashId.encode(offerRepo.save(offer).getId());

            final var saved = repository.savePhoto(id, Fixtures.photo);

            assertTrue(repository.exists(saved.getId()));

            em.flush();
            repository.delete(id, saved.getId());

            assertFalse(repository.exists(saved.getId()));
        }

        @Test
        void notFound() {
            assertThrows(ApiNotFoundException.class, () -> repository.delete("", ""));
        }

        @Test
        void multiplePhotos() {
            final var offer = new OfferTuple(new Offer("", "", Fixtures.user, null, Set.of()), new UserTuple(Fixtures.user));
            final var id = hashId.encode(offerRepo.save(offer).getId());

            final var saved1 = repository.savePhoto(id, Fixtures.photo);
            final var saved2 = repository.savePhoto(id, Fixtures.photo);
            final var saved3 = repository.savePhoto(id, Fixtures.photo);

            em.flush();
            repository.delete(id, saved1.getId());

            assertFalse(repository.exists(saved1.getId()));
            assertTrue(repository.exists(saved2.getId()));
            assertTrue(repository.exists(saved3.getId()));
        }

        @Test
        void multipleOffers() {
            final var offer1 = new OfferTuple(new Offer("", "", Fixtures.user, null, Set.of()), new UserTuple(Fixtures.user));
            final var offer2 = new OfferTuple(new Offer("", "", Fixtures.user, null, Set.of()), new UserTuple(Fixtures.user));
            final var id1 = hashId.encode(offerRepo.save(offer1).getId());
            final var id2 = hashId.encode(offerRepo.save(offer2).getId());

            repository.savePhoto(id1, Fixtures.photo);
            final var saved = repository.savePhoto(id2, Fixtures.photo);

            assertThrows(ApiNotFoundException.class, () -> repository.delete(id1, saved.getId()));
            assertTrue(repository.exists(saved.getId()));
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void findById() throws IOException {
            final var id = repo.save(new PhotoTuple(Fixtures.photo)).getId();

            assertTrue(repository.findById(hashId.encode(id)).isPresent());
        }
    }

    @Nested
    class ExistsTests {
        @Test
        void doesNotExist() {
            assertFalse(repository.exists("asdf"));
        }

        @Test
        void exists() throws IOException {
            final var id = repo.save(new PhotoTuple(Fixtures.photo)).getId();

            assertTrue(repository.exists(hashId.encode(id)));
        }
    }
}
