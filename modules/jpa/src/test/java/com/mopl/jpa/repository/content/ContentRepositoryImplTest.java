package com.mopl.jpa.repository.content;

import com.mopl.domain.model.content.ContentModel;
import com.mopl.domain.model.tag.TagModel;
import com.mopl.domain.repository.content.ContentRepository;
import com.mopl.jpa.config.JpaConfig;
import com.mopl.jpa.entity.content.ContentEntityMapper;
import com.mopl.jpa.entity.tag.TagEntityMapper;
import com.mopl.jpa.repository.tag.TagRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
    JpaConfig.class,
    ContentRepositoryImpl.class,
    ContentEntityMapper.class,
    TagRepositoryImpl.class,
    TagEntityMapper.class
})
@DisplayName("ContentRepositoryImpl 슬라이스 테스트")
class ContentRepositoryImplTest {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private TagRepositoryImpl tagRepository;

    @Nested
    @DisplayName("save()")
    class SaveTest {

        @Test
        @DisplayName("태그 목록과 함께 콘텐츠 저장")
        void withContentAndTags_savesSuccessfully() {
            // given
            TagModel tag1 = tagRepository.save(TagModel.create("영화"));
            TagModel tag2 = tagRepository.save(TagModel.create("SF"));

            ContentModel contentModel = ContentModel.create(
                "영화",
                "인셉션",
                "꿈속의 꿈",
                "https://mopl.com/inception.png"
            );

            // when
            ContentModel savedContent = contentRepository.save(contentModel, List.of(tag1, tag2));

            // then
            assertThat(savedContent.getId()).isNotNull();
            assertThat(savedContent.getType()).isEqualTo("영화");
            assertThat(savedContent.getTitle()).isEqualTo("인셉션");
            assertThat(savedContent.getDescription()).isEqualTo("꿈속의 꿈");
            assertThat(savedContent.getThumbnailUrl()).isEqualTo("https://mopl.com/inception.png");
            assertThat(savedContent.getTags()).containsExactlyInAnyOrder("영화", "SF");
            assertThat(savedContent.getCreatedAt()).isNotNull();
            assertThat(savedContent.getUpdatedAt()).isNotNull();
            assertThat(savedContent.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("태그 없이 콘텐츠 저장")
        void withContentOnly_savesSuccessfully() {
            // given
            ContentModel contentModel = ContentModel.create(
                "영화",
                "인셉션",
                "꿈속의 꿈",
                "https://mopl.com/inception.png"
            );

            // when
            ContentModel savedContent = contentRepository.save(contentModel, null);

            assertThat(savedContent.getId()).isNotNull();
            assertThat(savedContent.getType()).isEqualTo("영화");
            assertThat(savedContent.getTitle()).isEqualTo("인셉션");
            assertThat(savedContent.getDescription()).isEqualTo("꿈속의 꿈");
            assertThat(savedContent.getThumbnailUrl()).isEqualTo("https://mopl.com/inception.png");
            assertThat(savedContent.getTags()).isEmpty();
            assertThat(savedContent.getCreatedAt()).isNotNull();
            assertThat(savedContent.getUpdatedAt()).isNotNull();
            assertThat(savedContent.getDeletedAt()).isNull();
        }
    }
}
