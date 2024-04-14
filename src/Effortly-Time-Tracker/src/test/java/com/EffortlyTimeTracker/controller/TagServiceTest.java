//package com.EffortlyTimeTracker.controller;
//
//import com.EffortlyTimeTracker.DTO.ProjectDTO;
//import com.EffortlyTimeTracker.DTO.TagDTO;
//import com.EffortlyTimeTracker.entity.ProjectEntity;
//import com.EffortlyTimeTracker.repository.TagRepository;
//
//import com.EffortlyTimeTracker.entity.TagEntity;
//
//import com.EffortlyTimeTracker.service.TagService;
//import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TagServiceTest {
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @InjectMocks
//    private TagService tagService;
//
//    private TagDTO tagDTO;
//    private ProjectDTO projectDTO;
//    private TagEntity tag;
//
//    @BeforeEach
//    void setUp() {
//        projectDTO = new ProjectDTO();
//        projectDTO.setName("Test Project");
//        ProjectEntity project = new ProjectEntity();
//        project.setName(projectDTO.getName());
//
//        tagDTO = new TagDTO("Tag Name", project); // This matches the constructor generated by Lombok.
//        tagDTO.setName("Test Tag");
//        tagDTO.setColor("Test Color");
//        // Assuming project and tasks are handled appropriately here
//
//        tag = TagEntity.builder()
//                .tagId(1)
//                .name(tagDTO.getName())
//                .color(tagDTO.getColor())
//                // Assuming project and tasks are set here
//                .build();
//    }
//
//    @Test
//    void addTag_ShouldReturnNewTag() {
//        when(tagRepository.save(any(TagEntity.class))).thenReturn(tag);
//
//        TagEntity createdTag = tagService.addTag(tagDTO);
//
//        assertNotNull(createdTag);
//        assertEquals(tagDTO.getName(), createdTag.getName());
//        verify(tagRepository).save(any(TagEntity.class));
//    }
//
//    @Test
//    void delTagById_ShouldDeleteTag() {
//        when(tagRepository.existsById(anyInt())).thenReturn(true);
//        doNothing().when(tagRepository).deleteById(anyInt());
//
//        tagService.delTagById(tag.getTagId());
//
//        verify(tagRepository).deleteById(tag.getTagId());
//    }
//
//    @Test
//    void getTagById_ShouldReturnTag() {
//        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
//
//        TagEntity foundTag = tagService.getTagkById(tag.getTagId());
//
//        assertNotNull(foundTag);
//        assertEquals(tag.getTagId(), foundTag.getTagId());
//        verify(tagRepository).findById(tag.getTagId());
//    }
//
//    @Test
//    void getTagById_ShouldThrowException_WhenTagNotFound() {
//        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        assertThrows(TagNotFoundException.class, () -> tagService.getTagkById(999));
//
//        verify(tagRepository).findById(999);
//    }
//
//    @Test
//    void getAllTag_ShouldReturnListOfTags() {
//        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag));
//
//        List<TagEntity> tags = tagService.getAllTag();
//
//        assertNotNull(tags);
//        assertFalse(tags.isEmpty());
//        assertEquals(1, tags.size());
//        verify(tagRepository).findAll();
//    }
//}
