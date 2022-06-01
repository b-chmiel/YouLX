package com.youlx.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagFindServiceImpl implements TagFindService {
    private final TagFindRepository repository;

    @Override
    public List<Tag> search(String query) {
        return repository.search(query);
    }
}
