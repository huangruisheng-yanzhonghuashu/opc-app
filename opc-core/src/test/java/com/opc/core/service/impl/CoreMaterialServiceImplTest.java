package com.opc.core.service.impl;

import com.opc.core.domain.CoreMaterial;
import com.opc.core.mapper.CoreMaterialMapper;
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

        CoreMaterial result = materialService.selectMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Material", result.getTitle());
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
    public void testUpdateMaterial()
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(1L);
        material.setTitle("Updated Material");

        when(materialMapper.updateMaterial(any(CoreMaterial.class))).thenReturn(1);

        int result = materialService.updateMaterial(material);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteMaterialById()
    {
        when(materialMapper.deleteMaterialById(1L)).thenReturn(1);

        int result = materialService.deleteMaterialById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteMaterialByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(materialMapper.deleteMaterialByIds(ids)).thenReturn(3);

        int result = materialService.deleteMaterialByIds(ids);

        assertEquals(3, result);
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
}
