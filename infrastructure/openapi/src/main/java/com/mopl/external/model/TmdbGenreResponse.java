package com.mopl.external.model;

import java.util.List;

public record TmdbGenreResponse(
    List<TmdbGenreItem> genres
) {
}
