package com.youlx.api.rest.tag;

import com.youlx.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class TagDto {
    private String name;

    TagDto(Tag tag) {
        this.name = tag.name();
    }

    public Tag toDomain() {
        return new Tag(name);
    }
}
