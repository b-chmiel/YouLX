package com.domain.offer.find;

import com.domain.offer.Offer;
import com.domain.offer.OfferRepository;
import com.domain.offer.OfferStatus;
import com.domain.offer.stateCheck.OfferStateCheckService;
import com.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<Offer> findBy(Pageable pageable, UserId user, OfferStatusQuery statusQuery, OfferTagQuery tagQuery, OfferSearchQuery searchQuery) {
        final var statuses = statusQuery.getStatuses();
        final var tags = tagQuery.getTags();

        if (searchQuery.isPresent()) {
            return search(user, searchQuery, pageable);
        } else if (user.getUsername() == null) {
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
    public Page<Offer> findOpen(Pageable pageable, UserId user, OfferTagQuery tagQuery, OfferSearchQuery searchQuery) {
        return findBy(pageable, user, new OfferStatusQuery("OPEN"), tagQuery, searchQuery);
    }

    private Page<Offer> search(UserId user, OfferSearchQuery query, Pageable pageable) {
        final var result = offerSearchRepository
                .search(query.getQuery())
                .stream()
                .filter(offer -> offerStateCheckService.isVisible(user, offer))
                .toList();

        return new PageImpl<>(result, pageable, result.size());
    }
}
