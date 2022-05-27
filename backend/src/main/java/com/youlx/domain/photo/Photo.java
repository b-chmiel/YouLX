package com.youlx.domain.photo;

import com.youlx.domain.utils.uuid.Uuid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Photo {
    private final String id;
    private final byte[] data;

    public Photo(Uuid uuid, MultipartFile file) throws IOException {
        this.id = uuid.generate();
        this.data = file.getInputStream().readAllBytes();
    }
}
