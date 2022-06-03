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
    private final OfferStateCheckService offerStateCheckService;

    @Override
    public List<Tag> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(Tag tag) throws ApiException {
        repository.create(tag);
    }

    @Override
    public void assignToOffer(UserId user, String offerId, Tag tag) throws ApiException {
        if (!offerStateCheckService.isOwnerOf(offerId, user)) {
            throw new ApiUnauthorizedException("User must be owner of com.infrastructure.offer to change it's tags.");
        }

        repository.assignToOffer(offerId, tag);
    }
}
