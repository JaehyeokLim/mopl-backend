package com.mopl.external.model;

import java.util.List;

public record TmdbTvResponse(
    List<TmdbTvItem> results
) {
}
