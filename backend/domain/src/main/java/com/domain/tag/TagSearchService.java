package com.domain.tag;

import java.util.List;

public interface TagSearchService {
    List<Tag> search(String query);
}
