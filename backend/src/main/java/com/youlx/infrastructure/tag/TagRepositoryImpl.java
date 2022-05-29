package com.youlx.infrastructure.tag;

import com.youlx.domain.tag.Tag;
import com.youlx.domain.tag.TagRepository;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    public interface Repo extends JpaRepository<TagTuple, String> {

    }

    private final Repo repo;

    @Override
    public List<Tag> getAll() {
        return repo.findAll().stream().map(TagTuple::toDomain).toList();
    }

    @Override
    public void create(Tag tag) throws ApiException {
        if (repo.findById(tag.name()).isPresent()) {
            throw new ApiConflictException("Tag with the same name exists.");
        }

        repo.save(new TagTuple(tag));
    }
}
