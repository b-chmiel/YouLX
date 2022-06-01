package com.youlx.infrastructure.tag;

import com.youlx.domain.tag.Tag;
import com.youlx.domain.tag.TagRepository;
import com.youlx.domain.utils.exception.ApiConflictException;
import com.youlx.domain.utils.exception.ApiCustomException;
import com.youlx.domain.utils.exception.ApiException;
import com.youlx.domain.utils.exception.ApiNotFoundException;
import com.youlx.domain.utils.hashId.ApiHashIdException;
import com.youlx.domain.utils.hashId.HashId;
import com.youlx.infrastructure.offer.OfferTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    public interface Repo extends JpaRepository<TagTuple, Long> {
        boolean existsByName(String name);

        TagTuple getByName(String name);
    }

    public interface OfferRepo extends JpaRepository<OfferTuple, Long> {
    }

    private final Repo repo;
    private final OfferRepo offerRepo;
    private final HashId hashId;

    @Override
    public List<Tag> getAll() {
        return repo.findAll().stream().map(TagTuple::toDomain).toList();
    }

    @Override
    public void create(Tag tag) throws ApiException {
        if (repo.existsByName(tag.name())) {
            throw new ApiConflictException("Tag with the same name exists.");
        }

        repo.save(new TagTuple(tag));
    }

    @Override
    public void assignToOffer(String offerId, Tag tag) throws ApiException {
        final OfferTuple offer;

        try {
            offer = offerRepo.getById(hashId.decode(offerId));
        } catch (ApiHashIdException e) {
            throw new ApiNotFoundException("Offer not found: " + e.getMessage());
        }

        final TagTuple tagTuple;
        try {
            tagTuple = repo.getByName(tag.name());
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Tag not found: " + e.getMessage());
        } catch (Exception e) {
            throw new ApiCustomException(e.getMessage());
        }

        Set<TagTuple> tags;
        try {
            tags = offer.getTags();
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Offer not found: " + e.getMessage());
        }

        if (tags.contains(tagTuple)) {
            throw new ApiConflictException("Offer already contains this tag.");
        }

        try {
            tags.add(tagTuple);
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Tag not found: " + e.getMessage());
        }

        offerRepo.save(offer);
    }

    @Override
    public void clear() {
        repo.deleteAll();
    }
}
