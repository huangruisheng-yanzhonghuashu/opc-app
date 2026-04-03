package com.opc.core.service.impl;

import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreTagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreTagServiceImplTest {

    @Mock
    private CoreTagMapper tagMapper;

    @InjectMocks
    private CoreTagServiceImpl tagService;

    @Test
    public void testSelectTagList() {
        CoreTag tag = new CoreTag();
        tag.setTagName("Test");

        CoreTag resultTag = new CoreTag();
        resultTag.setId(1L);
        resultTag.setTagName("Test Tag");

        when(tagMapper.selectTagList(any(CoreTag.class))).thenReturn(Arrays.asList(resultTag));

        List<CoreTag> list = tagService.selectTagList(tag);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Tag", list.get(0).getTagName());
    }

    @Test
    public void testSelectTagById() {
        CoreTag tag = new CoreTag();
        tag.setId(1L);
        tag.setTagName("Test Tag");

        when(tagMapper.selectTagById(1L)).thenReturn(tag);

        CoreTag result = tagService.selectTagById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Tag", result.getTagName());
    }

    @Test
    public void testSelectTagByName() {
        CoreTag tag = new CoreTag();
        tag.setId(1L);
        tag.setTagName("Java");

        when(tagMapper.selectTagByName("Java")).thenReturn(tag);

        CoreTag result = tagService.selectTagByName("Java");

        assertNotNull(result);
        assertEquals("Java", result.getTagName());
    }

    @Test
    public void testInsertTag() {
        CoreTag tag = new CoreTag();
        tag.setTagName("New Tag");

        when(tagMapper.insertTag(any(CoreTag.class))).thenReturn(1);

        int result = tagService.insertTag(tag);

        assertEquals(1, result);
        verify(tagMapper).insertTag(tag);
    }

    @Test
    public void testUpdateTag() {
        CoreTag tag = new CoreTag();
        tag.setId(1L);
        tag.setTagName("Updated Tag");

        when(tagMapper.updateTag(any(CoreTag.class))).thenReturn(1);

        int result = tagService.updateTag(tag);

        assertEquals(1, result);
        verify(tagMapper).updateTag(tag);
    }

    @Test
    public void testDeleteTagById() {
        when(tagMapper.deleteTagById(1L)).thenReturn(1);

        int result = tagService.deleteTagById(1L);

        assertEquals(1, result);
        verify(tagMapper).deleteTagById(1L);
    }

    @Test
    public void testDeleteTagByIds() {
        Long[] ids = {1L, 2L, 3L};
        when(tagMapper.deleteTagByIds(ids)).thenReturn(3);

        int result = tagService.deleteTagByIds(ids);

        assertEquals(3, result);
        verify(tagMapper).deleteTagByIds(ids);
    }

    @Test
    public void testSelectAllActiveTags() {
        CoreTag tag1 = new CoreTag();
        tag1.setId(1L);
        tag1.setTagName("Tag 1");
        tag1.setStatus("0");

        CoreTag tag2 = new CoreTag();
        tag2.setId(2L);
        tag2.setTagName("Tag 2");
        tag2.setStatus("0");

        when(tagMapper.selectAllActiveTags()).thenReturn(Arrays.asList(tag1, tag2));

        List<CoreTag> list = tagService.selectAllActiveTags();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testSelectTagByIdNull() {
        when(tagMapper.selectTagById(999L)).thenReturn(null);

        CoreTag result = tagService.selectTagById(999L);

        assertNull(result);
    }

    @Test
    public void testSelectTagByNameNotFound() {
        when(tagMapper.selectTagByName("NonExistent")).thenReturn(null);

        CoreTag result = tagService.selectTagByName("NonExistent");

        assertNull(result);
    }

    @Test
    public void testSelectAllActiveTagsEmpty() {
        when(tagMapper.selectAllActiveTags()).thenReturn(Arrays.asList());

        List<CoreTag> list = tagService.selectAllActiveTags();

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
