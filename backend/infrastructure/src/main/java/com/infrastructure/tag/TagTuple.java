package com.infrastructure.tag;

import com.domain.tag.Tag;
import com.infrastructure.offer.OfferTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.util.List;
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

    @Field(termVector = TermVector.YES)
    private String name;
    private int references;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = TagTuple.class)
    private List<OfferTuple> offers;

    public TagTuple(Tag tag) {
        this.name = tag.name();
        this.references = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagTuple tagTuple = (TagTuple) o;
        return Objects.equals(id, tagTuple.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Tag toDomain() {
        return new Tag(name);
    }

    @Override
    public int compareTo(TagTuple o) {
        return Integer.compare(getReferences(), o.getReferences());
    }
}
