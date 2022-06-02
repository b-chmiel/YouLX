package com.youlx.domain.offer.find;

import com.youlx.domain.offer.Offer;
import com.youlx.domain.offer.OfferRepository;
import com.youlx.domain.offer.OfferStatus;
import com.youlx.domain.offer.stateCheck.OfferStateCheckService;
import com.youlx.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferFindServiceImpl implements OfferFindService {
    private final OfferRepository offerRepository;
    private final OfferSearchRepository offerSearchRepository;
    private final OfferStateCheckService offerStateCheckService;
    private final OfferFindRepository offerFindRepository;

    @Override
    public boolean exists(String id) {
        return offerRepository.exists(id);
    }

    @Override
    public Optional<Offer> findById(String id) {
        return offerRepository.findById(id);
    }

    @Override
    @Transactional
    public Page<Offer> findBy(Pageable pageable, UserId user, OfferStatusQuery statusQuery, OfferTagQuery tagQuery) {
        final var statuses = statusQuery.getStatuses();
        final var tags = tagQuery.getTags();

        if (user.getUsername() == null) {
            if (tags.isEmpty()) {
                return offerFindRepository.findAllByStatusIn(pageable, List.of(OfferStatus.OPEN));
            } else {
                return offerFindRepository.findAllByStatusInAndTagsIn(pageable, List.of(OfferStatus.OPEN), tags);
            }
        } else {
            if (tags.isEmpty() && statuses.isEmpty()) {
                return offerFindRepository.findAllByUserId(pageable, user);
            } else if (tags.isEmpty()) {
                return offerFindRepository.findAllByUserIdAndStatusIn(pageable, user, statuses);
            } else if (statuses.isEmpty()) {
                return offerFindRepository.findAllByUserIdAndStatusInAndTagsIn(pageable, user, List.of(OfferStatus.OPEN), tags);
            } else {
                return offerFindRepository.findAllByUserIdAndStatusInAndTagsIn(pageable, user, statuses, tags);
            }
        }
    }

    @Override
    @Transactional
    public Page<Offer> findOpen(Pageable pageable, UserId user, OfferTagQuery tagQuery) {
        return findBy(pageable, user, new OfferStatusQuery("OPEN"), tagQuery);
    }

    @Override
    public List<Offer> search(UserId user, String query) {
        return offerSearchRepository
                .search(query)
                .stream()
                .filter(offer -> offerStateCheckService.isVisible(user, offer))
                .toList();
    }
}
