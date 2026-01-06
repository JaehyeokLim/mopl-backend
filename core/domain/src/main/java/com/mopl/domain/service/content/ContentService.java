package com.mopl.domain.service.content;

import com.mopl.domain.exception.content.ContentNotFoundException;
import com.mopl.domain.model.content.ContentModel;
import com.mopl.domain.model.tag.TagModel;
import com.mopl.domain.repository.content.ContentRepository;
import com.mopl.domain.repository.content.ContentTagRepository;
import com.mopl.domain.service.tag.TagService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ContentService {

    private final TagService tagService;
    private final ContentRepository contentRepository;
    private final ContentTagRepository contentTagRepository;

    public ContentModel create(ContentModel content, List<String> tagNames) {
        ContentModel savedContent = contentRepository.save(content);
        return applyTags(savedContent, tagNames);
    }

    public boolean exists(UUID contentId) {
        return contentRepository.existsById(contentId);
    }

    public ContentModel getById(UUID contentId) {
        ContentModel content = contentRepository.findById(contentId)
            .orElseThrow(() -> ContentNotFoundException.withId(contentId));

        List<TagModel> tags = contentTagRepository.findTagsByContentId(contentId);

        return content.withTags(toTagNames(tags));
    }

    public ContentModel update(
        UUID contentId,
        String title,
        String description,
        String thumbnailUrl,
        List<String> tagNames
    ) {
        ContentModel contentModel = getById(contentId);

        String finalThumbnailUrl = (thumbnailUrl != null)
            ? thumbnailUrl
            : contentModel.getThumbnailUrl();

        ContentModel updatedContent = contentModel.update(
            title,
            description,
            finalThumbnailUrl
        );

        ContentModel savedContent = contentRepository.save(updatedContent);

        contentTagRepository.deleteAllByContentId(savedContent.getId());

        return applyTags(savedContent, tagNames);
    }

    private ContentModel applyTags(ContentModel content, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return content.withTags(List.of());
        }

        List<TagModel> tags = tagService.findOrCreateTags(tagNames);
        contentTagRepository.saveAll(content.getId(), tags);

        return content.withTags(toTagNames(tags));
    }

    private List<String> toTagNames(List<TagModel> tags) {
        if (tags == null) {
            return List.of();
        }
        return tags.stream()
            .map(TagModel::getName)
            .toList();
    }
}
