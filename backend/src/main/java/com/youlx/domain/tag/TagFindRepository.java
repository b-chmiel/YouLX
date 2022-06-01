package com.youlx.domain.tag;

import java.util.List;

public interface TagFindRepository {
    List<Tag> search(String query);
}
