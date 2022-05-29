package com.youlx.domain.tag;

import com.youlx.domain.utils.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository repository;

    @Override
    public List<Tag> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(Tag tag) throws ApiException {
        repository.create(tag);
    }
}
