package com.mopl.jpa.repository.content;

import com.mopl.domain.repository.content.ContentDeletionLogRepository;
import com.mopl.domain.repository.content.dto.ContentDeletionLogItem;
import com.mopl.jpa.entity.content.ContentDeletionLogEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ContentDeletionLogRepositoryImpl implements ContentDeletionLogRepository {

    private final JpaContentDeletionLogRepository jpaContentDeletionLogRepository;

    @Override
    public int saveAll(Map<UUID, String> thumbnailPathsByContentId) {
        if (thumbnailPathsByContentId == null || thumbnailPathsByContentId.isEmpty()) {
            return 0;
        }

        List<UUID> contentIds = new ArrayList<>(thumbnailPathsByContentId.keySet());
        List<UUID> existingIds = jpaContentDeletionLogRepository.findExistingContentIds(contentIds);
        Set<UUID> existingIdSet = new HashSet<>(existingIds);

        List<ContentDeletionLogEntity> entities = new ArrayList<>();

        for (UUID contentId : contentIds) {
            if (existingIdSet.contains(contentId)) {
                continue;
            }

            String thumbnailPath = thumbnailPathsByContentId.get(contentId);

            ContentDeletionLogEntity entity = ContentDeletionLogEntity.builder()
                .contentId(contentId)
                .thumbnailPath(thumbnailPath)
                .build();

            entities.add(entity);
        }

        if (entities.isEmpty()) {
            return 0;
        }

        jpaContentDeletionLogRepository.saveAll(entities);
        return entities.size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentDeletionLogItem> findImageCleanupTargets(int limit) {
        return jpaContentDeletionLogRepository.findImageCleanupTargets(limit).stream()
            .map(row -> new ContentDeletionLogItem(
                row.getLogId(),
                row.getContentId(),
                row.getThumbnailPath()
            ))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int markImageProcessed(List<UUID> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            return 0;
        }
        return jpaContentDeletionLogRepository.markImageProcessed(logIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> findFullyProcessedLogIds(int limit) {
        return jpaContentDeletionLogRepository.findFullyProcessedLogIds(limit);
    }

    @Override
    @Transactional
    public int deleteAllByIds(List<UUID> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            return 0;
        }
        return jpaContentDeletionLogRepository.deleteAllByIds(logIds);
    }
}
