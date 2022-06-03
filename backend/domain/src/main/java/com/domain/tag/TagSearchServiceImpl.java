package com.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagSearchServiceImpl implements TagSearchService {
    private final TagSearchRepository repository;

    @Override
    public List<Tag> search(String query) {
        return repository.search(query);
    }
}
