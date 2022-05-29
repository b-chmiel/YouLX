package com.youlx.domain.tag;

import com.youlx.domain.offer.OfferService;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final OfferService offerService;

    @Override
    public List<Tag> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(Tag tag) throws ApiException {
        repository.create(tag);
    }

    @Override
    public void assignToOffer(String username, String offerId, Tag tag) throws ApiException {
        if (!offerService.isOwnerOf(offerId, username)) {
            throw new ApiUnauthorizedException("User must be owner of offer to change it's tags.");
        }

        repository.assignToOffer(offerId, tag);
    }
}
