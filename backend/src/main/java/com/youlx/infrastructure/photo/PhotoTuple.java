package com.youlx.infrastructure.photo;

import com.youlx.domain.photo.Photo;
import com.youlx.domain.utils.hashId.HashId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;

@Table(name = "LX_PHOTO")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public
class PhotoTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String index;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;

    public PhotoTuple(Photo photo) throws IOException {
        index = photo.getId();
        data = photo.getData();
    }

    public Photo toDomain() {
        return new Photo(index, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoTuple userTuple = (PhotoTuple) o;
        return Objects.equals(id, userTuple.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
