package com.youlx.infrastructure.tag;

import com.youlx.domain.tag.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "LX_TAG")
@NoArgsConstructor
@Getter
@Setter
@ToString
class TagTuple {
    @Id
    private String name;

    TagTuple(Tag tag) {
        this.name = tag.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagTuple tagTuple = (TagTuple) o;
        return Objects.equals(name, tagTuple.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Tag toDomain() {
        return new Tag(name);
    }
}
