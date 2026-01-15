package com.mopl.jpa.repository.tag;

import com.mopl.domain.model.tag.GenreModel;
import com.mopl.domain.repository.tag.GenreRepository;
import com.mopl.jpa.entity.tag.GenreEntity;
import com.mopl.jpa.entity.tag.GenreEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    private final JpaGenreRepository jpaGenreRepository;
    private final GenreEntityMapper genreEntityMapper;

    @Override
    public GenreModel save(GenreModel genreModel) {
        GenreEntity entity = genreEntityMapper.toEntity(genreModel);
        GenreEntity savedEntity = jpaGenreRepository.save(entity);
        return genreEntityMapper.toModel(savedEntity);
    }

    @Override
    public Optional<GenreModel> findByTmdbId(Long tmdbId) {
        return jpaGenreRepository.findByTmdbId(tmdbId)
            .map(genreEntityMapper::toModel);
    }

    @Override
    public long count() {
        return jpaGenreRepository.count();
    }
}
