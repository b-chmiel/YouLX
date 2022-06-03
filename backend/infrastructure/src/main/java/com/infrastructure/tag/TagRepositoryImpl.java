package com.infrastructure.tag;

import com.domain.tag.Tag;
import com.domain.tag.TagRepository;
import com.domain.utils.exception.ApiConflictException;
import com.domain.utils.exception.ApiCustomException;
import com.domain.utils.exception.ApiException;
import com.domain.utils.exception.ApiNotFoundException;
import com.domain.utils.hashId.ApiHashIdException;
import com.domain.utils.hashId.HashId;
import com.infrastructure.offer.JpaOfferRepository;
import com.infrastructure.offer.OfferTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final HashId hashId;
    private final JpaTagRepository repo;
    private final JpaOfferRepository offerRepo;

    @Override
    public List<Tag> getAll() {
        return repo.findAll()
                .stream()
                .sorted(Comparator.reverseOrder())
                .map(TagTuple::toDomain)
                .toList();
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

        Set<TagTuple> tags;
        try {
            tags = offer.getTags();
        } catch (EntityNotFoundException e) {
            throw new ApiNotFoundException("Offer not found: " + e.getMessage());
        }

        final Optional<TagTuple> tagTuple;
        try {
            tagTuple = repo.findByName(tag.name());
        } catch (DataIntegrityViolationException e) {
            throw new ApiCustomException("WTF: " + e.getMostSpecificCause().getMessage());
        }

        if (tagTuple.isEmpty()) {
            throw new ApiNotFoundException("Tag not found.");
        }

        if (tags.contains(tagTuple.get())) {
            throw new ApiConflictException("Offer already contains this com.infrastructure.tag.");
        }

        tags.add(tagTuple.get());

        incrementReferences(tag);
        offerRepo.save(offer);
    }

    private void incrementReferences(Tag tag) throws ApiException {
        final var tuple = repo.findByName(tag.name());
        if (tuple.isEmpty()) {
            throw new ApiNotFoundException("Tag not found.");
        }

        tuple.get().setReferences(tuple.get().getReferences() + 1);

        repo.save(tuple.get());
    }

    @Override
    public void clear() {
        repo.deleteAll();
    }
}
