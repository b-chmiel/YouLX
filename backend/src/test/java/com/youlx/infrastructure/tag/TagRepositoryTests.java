package com.youlx.infrastructure.tag;

import com.youlx.domain.tag.Tag;
import com.youlx.domain.tag.TagRepository;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.JpaConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
        classes = JpaConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class TagRepositoryTests {
    @Autowired
    private TagRepository repository;
    @MockBean
    private HashId hashId;

    @Test
    void createAndGet() {
        final var tags = List.of(new Tag("a"), new Tag("b"));
        tags.forEach(tag -> repository.create(tag));
        assertEquals(tags, repository.getAll());
    }
}
