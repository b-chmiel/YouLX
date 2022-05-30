package com.youlx.domain.offer;

import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.exception.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferModifyService {
    private final OfferRepository offerRepository;
    private final OfferFindService offerFindService;
    private final OfferStateCheckService offerStateCheckService;

    @Override
    public Offer create(Offer offer) throws ApiException {
        return offerRepository.create(offer);
    }

    @Override
    public Offer close(String id, OfferClose offerClose, String username) throws ApiException {
        final var offer = offerRepository.findById(id);
        if (offer.isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }
        if (!offerStateCheckService.isClosable(username, offer.get())) {
            throw new ApiCustomException("Offer is not closable.");
        }

        final var result = offerRepository.close(id, offerClose);

        if (result.isEmpty()) {
            throw new ApiNotFoundException("Offer not found.");
        }

        return result.get();
    }


    @Override
    public void modify(String id, OfferModify offer, String username) throws ApiException {
        if (!offerFindService.exists(id)) {
            throw new ApiNotFoundException("Offer not found.");
        } else if (!offerStateCheckService.isOwnerOf(id, username)) {
            throw new ApiUnauthorizedException("User is not the owner of offer.");
        }

        offerRepository.modify(id, offer);
    }

    @Override
    public void publish(String username, String offerId) throws ApiException {
        if (!offerStateCheckService.isPublishable(username, offerId)) {
            throw new ApiCustomException("Offer is not publishable.");
        }

        offerRepository.publish(offerId);
    }
}
