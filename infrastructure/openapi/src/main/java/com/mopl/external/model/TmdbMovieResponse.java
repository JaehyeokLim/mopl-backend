package com.mopl.external.model;

import java.util.List;

public record TmdbMovieResponse(
    List<TmdbMovieItem> results
) {
}
