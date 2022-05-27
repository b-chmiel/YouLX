package com.youlx.domain.photo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Photo {
    private final String id;
    private final byte[] data;

    public Photo(MultipartFile file) throws IOException {
        this.id = null;
        this.data = file.getInputStream().readAllBytes();
    }
}
