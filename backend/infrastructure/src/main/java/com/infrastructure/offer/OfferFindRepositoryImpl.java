package com.infrastructure.offer;

import com.domain.offer.Offer;
import com.domain.offer.OfferStatus;
import com.domain.offer.find.OfferFindRepository;
import com.domain.tag.Tag;
import com.domain.user.UserId;
import com.domain.utils.hashId.HashId;
import com.infrastructure.tag.JpaTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OfferFindRepositoryImpl implements OfferFindRepository {
    private final HashId hashId;
    private final OfferPagedRepository offerPagedRepository;
    private final JpaTagRepository tagRepository;

    @Override
    public Page<Offer> findAllByStatusIn(Pageable pageable, List<OfferStatus> status) {
        return offerPagedRepository
                .findAllByStatusIn(pageable, status)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserIdAndStatusIn(Pageable pageable, UserId user, List<OfferStatus> statuses) {
        return offerPagedRepository
                .findAllByUserIdAndStatusIn(pageable, user.getUsername(), statuses)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserIdAndStatusInAndTagsIn(Pageable pageable, UserId user, List<OfferStatus> statuses, List<Tag> tags) {
        final var tagTuples = tags
                .stream()
                .map(Tag::name)
                .map(tagRepository::findByName)
                .flatMap(Optional::stream)
                .toList();

        return offerPagedRepository
                .findAllByUserIdAndStatusInAndTagsIn(pageable, user.getUsername(), statuses, tagTuples)
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByUserId(Pageable pageable, UserId user) {
        return offerPagedRepository
                .findAllByUserId(pageable, user.getUsername())
                .map(o -> o.toDomain(hashId));
    }

    @Override
    public Page<Offer> findAllByStatusInAndTagsIn(Pageable pageable, List<OfferStatus> statuses, List<Tag> tags) {
        final var tagTuples = tags
                .stream()
                .map(Tag::name)
                .map(tagRepository::findByName)
                .flatMap(Optional::stream)
                .toList();

        return offerPagedRepository
                .findAllByStatusInAndTagsIn(pageable, statuses, tagTuples)
                .map(o -> o.toDomain(hashId));
    }
}