package com.domain.tag;

import com.domain.user.UserId;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiUnauthorizedException;
import com.domain.offer.stateCheck.OfferStateCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final TagSearchRepository searchRepository;

    @Override
    public List<Tag> getAll(String query) {
        if (query == null || query.isBlank()) {
            return repository.getAll();
        }

        return searchRepository.search(query);
    }

    @Override
    public void create(Tag tag) throws ApiException {
        repository.create(tag);
    }
}
