package com.mopl.jpa.repository.content;

import com.mopl.jpa.entity.content.ContentDeletionLogEntity;
import com.mopl.jpa.repository.content.projection.ContentDeletionLogRow;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaContentDeletionLogRepository extends
    JpaRepository<ContentDeletionLogEntity, UUID> {

    @Query(
        value = """
                select BIN_TO_UUID(content_id)
                from content_deletion_logs
                where content_id in (:contentIds)
            """,
        nativeQuery = true
    )
    List<UUID> findExistingContentIds(@Param("contentIds") List<UUID> contentIds);

    @Query(
        value = """
                select
                    BIN_TO_UUID(l.id) as logId,
                    BIN_TO_UUID(l.content_id) as contentId,
                    l.thumbnail_path as thumbnailPath
                from content_deletion_logs l
                where l.image_processed_at is null
                  and l.thumbnail_path is not null
                  and l.thumbnail_path <> ''
                order by l.deleted_at, l.id
                limit :limit
            """,
        nativeQuery = true
    )
    List<ContentDeletionLogRow> findImageCleanupTargets(@Param("limit") int limit);

    @Modifying
    @Query(
        value = """
                update content_deletion_logs
                set image_processed_at = utc_timestamp(6)
                where id in (:logIds)
            """,
        nativeQuery = true
    )
    int markImageProcessed(@Param("logIds") List<UUID> logIds);

    @Query(
        value = """
                select BIN_TO_UUID(id)
                from content_deletion_logs
                where image_processed_at is not null
                order by deleted_at, id
                limit :limit
            """,
        nativeQuery = true
    )
    List<UUID> findFullyProcessedLogIds(@Param("limit") int limit);

    @Modifying
    @Query(
        value = """
                delete from content_deletion_logs
                where id in (:logIds)
            """,
        nativeQuery = true
    )
    int deleteAllByIds(@Param("logIds") List<UUID> logIds);
}
