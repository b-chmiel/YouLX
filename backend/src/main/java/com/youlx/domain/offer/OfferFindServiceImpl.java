package com.youlx.domain.offer;

import com.youlx.domain.user.UserShallow;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferPagedRepository;
import com.youlx.infrastructure.offer.OfferTuple;
import com.youlx.infrastructure.tag.TagTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferFindServiceImpl implements OfferFindService {
    private final HashId hashId;
    private final OfferPagedRepository offerPagedRepository;
    private final OfferRepository offerRepository;
    private final OfferSearchRepository offerSearchRepository;
    private final OfferStateCheckService offerStateCheckService;

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
    public Page<Offer> findBy(Pageable pageable, UserShallow user, String status, String tag) {
        final var username = user == null ? null : user.username();
        final var statuses = Arrays.stream(status.split(";"))
                .filter(t -> !t.isBlank())
                .map(OfferStatus::fromString)
                .filter(t -> !t.equals(OfferStatus.ERROR))
                .toList();

        final var tags = Arrays.stream(tag.split(";"))
                .filter(t -> !t.isBlank())
                .map(TagTuple::new)
                .toList();

        final Page<OfferTuple> offersTuple;
        if (username == null) {
            if (tags.isEmpty()) {
                offersTuple = offerPagedRepository.findAllByStatusIn(pageable, List.of(OfferStatus.OPEN));
            } else {
                offersTuple = offerPagedRepository.findAllByStatusInAndTagsIn(pageable, List.of(OfferStatus.OPEN), tags);
            }
        } else {
            if (tags.isEmpty() && statuses.isEmpty()) {
                offersTuple = offerPagedRepository.findAllByUserId(pageable, username);
            } else if (tags.isEmpty()) {
                offersTuple = offerPagedRepository.findAllByUserIdAndStatusIn(pageable, username, statuses);
            } else if (statuses.isEmpty()) {
                offersTuple = offerPagedRepository.findAllByUserIdAndAndStatusInAndTagsIn(pageable, username, List.of(OfferStatus.OPEN), tags);
            } else {
                offersTuple = offerPagedRepository.findAllByUserIdAndAndStatusInAndTagsIn(pageable, username, statuses, tags);
            }
        }

        return offersTuple.map(
                t -> t.toDomain(hashId)
        );
    }

    @Override
    @Transactional
    public Page<Offer> findOpen(Pageable pageable, UserShallow user, String tags) {
        return findBy(pageable, user, "OPEN", tags);
    }

    @Override
    public List<Offer> search(UserShallow user, String query) {
        return offerSearchRepository
                .search(query)
                .stream()
                .filter(offer -> offerStateCheckService.isVisible(user.username(), offer))
                .toList();
    }
}
