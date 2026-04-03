package com.opc.core.service.impl;

import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreTag;
import com.opc.core.domain.CoreTag2;
import com.opc.core.mapper.CoreMaterialMapper;
import com.opc.core.mapper.CoreMaterialTag2Mapper;
import com.opc.core.mapper.CoreMaterialTagMapper;
import com.opc.core.mapper.CoreTag2Mapper;
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
public class CoreMaterialServiceImplTest
{
    @Mock
    private CoreMaterialMapper materialMapper;

    @Mock
    private CoreMaterialTagMapper materialTagMapper;

    @Mock
    private CoreMaterialTag2Mapper materialTag2Mapper;

    @Mock
    private CoreTag2Mapper tag2Mapper;

    @InjectMocks
    private CoreMaterialServiceImpl materialService;

    @Test
    public void testSelectMaterialList()
    {
        CoreMaterial material = new CoreMaterial();
        material.setTitle("Test Material");

        CoreMaterial resultMaterial = new CoreMaterial();
        resultMaterial.setId(1L);
        resultMaterial.setTitle("Test Material");

        when(materialMapper.selectMaterialList(any(CoreMaterial.class))).thenReturn(Arrays.asList(resultMaterial));

        List<CoreMaterial> list = materialService.selectMaterialList(material);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Material", list.get(0).getTitle());
    }

    @Test
    public void testSelectMaterialById()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Test Material");

        when(materialMapper.selectMaterialById(1L)).thenReturn(material);
        when(materialTagMapper.selectTagsByMaterialId(1L)).thenReturn(Arrays.asList());
        when(materialTag2Mapper.selectTags2ByMaterialId(1L)).thenReturn(Arrays.asList());

        CoreMaterial result = materialService.selectMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Material", result.getTitle());
    }

    @Test
    public void testSelectMaterialByIdWithTags()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Test Material");

        CoreTag tag = new CoreTag();
        tag.setId(1L);
        tag.setTagName("Test Tag");

        CoreTag2 tag2 = new CoreTag2();
        tag2.setId(1L);
        tag2.setTagName("Test Tag 2");

        when(materialMapper.selectMaterialById(1L)).thenReturn(material);
        when(materialTagMapper.selectTagsByMaterialId(1L)).thenReturn(Arrays.asList(tag));
        when(materialTag2Mapper.selectTags2ByMaterialId(1L)).thenReturn(Arrays.asList(tag2));

        CoreMaterial result = materialService.selectMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getTags());
        assertEquals(1, result.getTags().size());
        assertEquals("Test Tag", result.getTags().get(0).getTagName());
    }

    @Test
    public void testInsertMaterial()
    {
        CoreMaterial material = new CoreMaterial();
        material.setTitle("New Material");
        material.setContent("Material content");

        when(materialMapper.insertMaterial(any(CoreMaterial.class))).thenReturn(1);

        int result = materialService.insertMaterial(material);

        assertEquals(1, result);
    }

    @Test
    public void testInsertMaterialWithTags()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("New Material");
        material.setTagIds(Arrays.asList(1L, 2L));

        when(materialMapper.insertMaterial(any(CoreMaterial.class))).thenReturn(1);
        when(materialTagMapper.deleteMaterialTagByMaterialId(1L)).thenReturn(1);
        when(materialMapper.insertMaterialTag(anyLong(), anyLong())).thenReturn(1);
        when(tag2Mapper.selectAllActiveTags()).thenReturn(Arrays.asList());

        int result = materialService.insertMaterial(material);

        assertEquals(1, result);
        verify(materialTagMapper).deleteMaterialTagByMaterialId(1L);
        verify(materialMapper, times(2)).insertMaterialTag(anyLong(), anyLong());
    }

    @Test
    public void testUpdateMaterial()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Updated Material");

        when(materialMapper.updateMaterial(any(CoreMaterial.class))).thenReturn(1);
        when(tag2Mapper.selectAllActiveTags()).thenReturn(Arrays.asList());

        int result = materialService.updateMaterial(material);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateMaterialWithTags()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Updated Material");
        material.setTagIds(Arrays.asList(1L, 2L));

        when(materialMapper.updateMaterial(any(CoreMaterial.class))).thenReturn(1);
        when(materialTagMapper.deleteMaterialTagByMaterialId(1L)).thenReturn(1);
        when(materialMapper.insertMaterialTag(anyLong(), anyLong())).thenReturn(1);
        when(tag2Mapper.selectAllActiveTags()).thenReturn(Arrays.asList());

        int result = materialService.updateMaterial(material);

        assertEquals(1, result);
        verify(materialTagMapper).deleteMaterialTagByMaterialId(1L);
        verify(materialMapper, times(2)).insertMaterialTag(anyLong(), anyLong());
    }

    @Test
    public void testDeleteMaterialById()
    {
        when(materialTagMapper.deleteMaterialTagByMaterialId(1L)).thenReturn(1);
        when(materialMapper.deleteMaterialById(1L)).thenReturn(1);

        int result = materialService.deleteMaterialById(1L);

        assertEquals(1, result);
        verify(materialTagMapper).deleteMaterialTagByMaterialId(1L);
    }

    @Test
    public void testDeleteMaterialByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(materialTagMapper.deleteMaterialTagByMaterialId(anyLong())).thenReturn(1);
        when(materialMapper.deleteMaterialByIds(ids)).thenReturn(3);

        int result = materialService.deleteMaterialByIds(ids);

        assertEquals(3, result);
        verify(materialTagMapper, times(3)).deleteMaterialTagByMaterialId(anyLong());
    }

    @Test
    public void testChangeStatus()
    {
        when(materialMapper.changeStatus(any(CoreMaterial.class))).thenReturn(1);

        int result = materialService.changeStatus(1L, "1");

        assertEquals(1, result);
        verify(materialMapper).changeStatus(argThat(material ->
            material.getId().equals(1L) && material.getStatus().equals("1")
        ));
    }

    @Test
    public void testChangeTop()
    {
        when(materialMapper.changeTop(any(CoreMaterial.class))).thenReturn(1);

        int result = materialService.changeTop(1L, "1");

        assertEquals(1, result);
        verify(materialMapper).changeTop(argThat(material ->
            material.getId().equals(1L) && material.getIsTop().equals("1")
        ));
    }

    @Test
    public void testSelectMaterialByIdNull()
    {
        when(materialMapper.selectMaterialById(999L)).thenReturn(null);

        CoreMaterial result = materialService.selectMaterialById(999L);

        assertNull(result);
    }
}
