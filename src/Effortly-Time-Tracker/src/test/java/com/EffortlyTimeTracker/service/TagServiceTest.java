package com.EffortlyTimeTracker.service;


import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
//todo add get by project and
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private TagEntity tag;

    @BeforeEach
    void setUp() {
        tag = new TagEntity();
        tag.setTagId(1);
        tag.setName("Important");
    }

    @Test
    void addTagTestSuccess() {
        when(tagRepository.save(any(TagEntity.class))).thenReturn(tag);

        TagEntity savedTag = tagService.addTag(tag);
        assertNotNull(savedTag);
        assertEquals("Important", savedTag.getName());

        verify(tagRepository).save(tag);
    }

    @Test
    void deleteTagByIdTestExists() {
        when(tagRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(tagRepository).deleteById(anyInt());

        tagService.delTagById(1);

        verify(tagRepository).deleteById(1);
    }

    @Test
    void deleteTagByIdTestNotExists() {
        when(tagRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(TagNotFoundException.class, () -> tagService.delTagById(1));
    }

    @Test
    void getTagByIdTestFound() {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));

        TagEntity foundTag = tagService.getTagkById(1);

        assertNotNull(foundTag);
        assertEquals("Important", foundTag.getName());
    }

    @Test
    void getTagByIdTestNotFound() {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTagkById(1));
    }

//    @Test
//    void getAllTagTest() {
//        Set<TagEntity> tags = new HashSet<>();
//        tags.add(tag);
//        when(tagRepository.findAll()).thenReturn(tags);
//
//        Set<TagEntity> fetchedTags = tagService.getAllTag();
//
//        assertFalse(fetchedTags.isEmpty());
//        assertTrue(fetchedTags.contains(tag));
//    }
}