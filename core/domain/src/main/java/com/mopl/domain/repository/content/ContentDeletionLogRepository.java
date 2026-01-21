package com.mopl.domain.repository.content;

import com.mopl.domain.repository.content.dto.ContentDeletionLogItem;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ContentDeletionLogRepository {

    int saveAll(Map<UUID, String> thumbnailPathsByContentId);

    List<ContentDeletionLogItem> findImageCleanupTargets(int limit);

    int markImageProcessed(List<UUID> logIds);

    List<UUID> findFullyProcessedLogIds(int limit);

    int deleteAllByIds(List<UUID> logIds);
}
