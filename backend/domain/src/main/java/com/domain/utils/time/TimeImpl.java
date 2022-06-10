package com.domain.utils.time;

import java.time.LocalDateTime;

public class TimeImpl implements Time {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
