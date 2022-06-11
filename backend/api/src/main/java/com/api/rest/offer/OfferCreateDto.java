package com.api.rest.offer;

import com.api.rest.tag.TagDto;
import com.domain.offer.Offer;
import com.domain.tag.Tag;
import com.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class OfferCreateDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Set<TagDto> tags;

    Offer toDomain(User user) {
        final Set<Tag> tags = this.tags == null ? Set.of() : this.tags.stream().map(TagDto::toDomain).collect(Collectors.toSet());

        return new Offer(
                name,
                description,
                user,
                price,
                tags
        );
    }
}
