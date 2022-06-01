package com.youlx.domain.tag;

import java.util.List;

public interface TagSearchRepository {
    List<Tag> search(String query);
}
