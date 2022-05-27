package com.youlx.infrastructure.photo;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.photo.ApiImageException;
import com.youlx.domain.photo.PhotoRepository;
import com.youlx.domain.user.UserRepository;
import com.youlx.domain.utils.ApiNotFoundException;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.domain.utils.uuid.Uuid;
import com.youlx.infrastructure.JpaConfig;
import com.youlx.infrastructure.offer.OfferTuple;
import com.youlx.infrastructure.user.UserTuple;
import com.youlx.testUtils.Fixtures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;

import static com.youlx.testUtils.Fixtures.user;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = JpaConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class PhotoRepositoryTest {
    @MockBean
    private HashId hashId;
    @MockBean
    private Uuid uuid;

    @Autowired
    private PhotoRepository repository;

    @Autowired
    private PhotoRepositoryImpl.OfferRepo offerRepo;
    @Autowired
    private PhotoRepositoryImpl.Repo repo;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() throws ApiHashIdException {
        userRepository.create(user);
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
            when(hashId.decode(id)).thenThrow(ApiHashIdException.class);
            assertThrows(ApiHashIdException.class, () -> repository.savePhoto(id, null));
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
            final var id = "a";
            final var offer = new OfferTuple(new Offer("", "", user), new UserTuple(user));
            final var offerSaved = offerRepo.save(offer);
            when(hashId.decode(id)).thenReturn(offerSaved.getId());
            when(uuid.generate()).thenReturn(Fixtures.photo.getId());

            final var saved = repository.savePhoto(id, Fixtures.photo);

            assertEquals(Fixtures.photo.getId(), saved.getId());
//            assertEquals(Fixtures.photo.getData(), saved.getData());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void delete() {
            final var id = "a";
            final var offer = new OfferTuple(new Offer("", "", user), new UserTuple(user));
            final var offerSaved = offerRepo.save(offer);
            when(hashId.decode(id)).thenReturn(offerSaved.getId());
            when(uuid.generate()).thenReturn(Fixtures.photo.getId());

            repository.savePhoto(id, Fixtures.photo);

            assertTrue(repository.findById(Fixtures.photo.getId()).isPresent());

            repository.delete(id, Fixtures.photo.getId());

            assertTrue(repository.findById(Fixtures.photo.getId()).isEmpty());
            assertTrue(repo.findAll().isEmpty());
        }
    }
}
