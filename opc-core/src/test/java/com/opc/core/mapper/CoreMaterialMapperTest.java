package com.opc.core.mapper;

import com.opc.core.domain.CoreMaterial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreMaterialMapperTest
{
    @Mock
    private CoreMaterialMapper materialMapper;

    @Test
    public void testSelectMaterialList()
    {
        CoreMaterial material = new CoreMaterial();
        material.setTitle("Test Material");

        CoreMaterial resultMaterial = new CoreMaterial();
        resultMaterial.setId(1L);
        resultMaterial.setTitle("Test Material");

        when(materialMapper.selectMaterialList(any(CoreMaterial.class))).thenReturn(Arrays.asList(resultMaterial));

        List<CoreMaterial> list = materialMapper.selectMaterialList(material);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Material", list.get(0).getTitle());
        verify(materialMapper, times(1)).selectMaterialList(material);
    }

    @Test
    public void testSelectMaterialById()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Test Material");

        when(materialMapper.selectMaterialById(1L)).thenReturn(material);

        CoreMaterial result = materialMapper.selectMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Material", result.getTitle());
        verify(materialMapper, times(1)).selectMaterialById(1L);
    }

    @Test
    public void testInsertMaterial()
    {
        CoreMaterial material = new CoreMaterial();
        material.setTitle("New Material");
        material.setContent("Material content");

        when(materialMapper.insertMaterial(any(CoreMaterial.class))).thenReturn(1);

        int result = materialMapper.insertMaterial(material);

        assertEquals(1, result);
        verify(materialMapper, times(1)).insertMaterial(material);
    }

    @Test
    public void testUpdateMaterial()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Updated Material");

        when(materialMapper.updateMaterial(any(CoreMaterial.class))).thenReturn(1);

        int result = materialMapper.updateMaterial(material);

        assertEquals(1, result);
        verify(materialMapper, times(1)).updateMaterial(material);
    }

    @Test
    public void testDeleteMaterialById()
    {
        when(materialMapper.deleteMaterialById(1L)).thenReturn(1);

        int result = materialMapper.deleteMaterialById(1L);

        assertEquals(1, result);
        verify(materialMapper, times(1)).deleteMaterialById(1L);
    }

    @Test
    public void testDeleteMaterialByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(materialMapper.deleteMaterialByIds(ids)).thenReturn(3);

        int result = materialMapper.deleteMaterialByIds(ids);

        assertEquals(3, result);
        verify(materialMapper, times(1)).deleteMaterialByIds(ids);
    }

    @Test
    public void testChangeStatus()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setStatus("1");

        when(materialMapper.changeStatus(any(CoreMaterial.class))).thenReturn(1);

        int result = materialMapper.changeStatus(material);

        assertEquals(1, result);
        verify(materialMapper, times(1)).changeStatus(material);
    }

    @Test
    public void testChangeTop()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setIsTop("1");

        when(materialMapper.changeTop(any(CoreMaterial.class))).thenReturn(1);

        int result = materialMapper.changeTop(material);

        assertEquals(1, result);
        verify(materialMapper, times(1)).changeTop(material);
    }
}
