package com.youlx.infrastructure.tag;

import com.youlx.domain.tag.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "LX_TAG")
@Indexed
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TagTuple implements Comparable<TagTuple> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field(termVector = TermVector.WITH_POSITION_OFFSETS)
    private String name;
    private int references;

    TagTuple(Tag tag) {
        this.name = tag.name();
        this.references = 0;
    }

    public TagTuple(String name) {
        this.name = name;
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

    @Override
    public int compareTo(TagTuple o) {
        return Integer.compare(getReferences(), o.getReferences());
    }
}
