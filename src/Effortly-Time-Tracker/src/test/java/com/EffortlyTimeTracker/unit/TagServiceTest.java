package com.EffortlyTimeTracker.unit;


import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private ITagRepository tagRepository;

    @Mock
    private IProjectRepository projectRepository;  // Add the missing mock

    @InjectMocks
    private TagService tagService;

    private TagEntity tag;
    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        project = new ProjectEntity();
        project.setProjectId(1);
        project.setName("Test Project");

        tag = new TagEntity();
        tag.setTagId(1);
        tag.setName("Important");
        tag.setProject(project);
    }

//    todo @Test
//    void addTagTestSuccess() {
//        // Mocking the projectRepository to return the project when findById is called
//        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
//
//        // Mocking the tagRepository to return the tag when save is called
//        when(tagRepository.save(any(TagEntity.class))).thenReturn(tag);
//
//        // Call addTag method in TagService
//        TagEntity savedTag = tagService.addTag(tag, 1);
//
//        // Assertions
//        assertNotNull(savedTag);
//        assertEquals("Important", savedTag.getName());
//
//        // Verify interactions
//        verify(projectRepository).findById(1);  // Ensure findById is called with the correct projectId
//        verify(tagRepository).save(tag);  // Ensure save is called on the tagRepository
//    }

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