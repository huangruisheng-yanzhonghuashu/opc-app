package com.opc.core.service.impl;

import com.opc.core.domain.CoreMaterialCategory;
import com.opc.core.mapper.CoreMaterialCategoryMapper;
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
public class CoreMaterialCategoryServiceImplTest {

    @Mock
    private CoreMaterialCategoryMapper categoryMapper;

    @InjectMocks
    private CoreMaterialCategoryServiceImpl categoryService;

    @Test
    public void testSelectCoreMaterialCategoryById() {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setId(1L);
        category.setCategoryName("Test Category");

        when(categoryMapper.selectCoreMaterialCategoryById(1L)).thenReturn(category);

        CoreMaterialCategory result = categoryService.selectCoreMaterialCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Category", result.getCategoryName());
    }

    @Test
    public void testSelectCoreMaterialCategoryList() {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setCategoryName("Test");

        CoreMaterialCategory resultCategory = new CoreMaterialCategory();
        resultCategory.setId(1L);
        resultCategory.setCategoryName("Test Category");

        when(categoryMapper.selectCoreMaterialCategoryList(any(CoreMaterialCategory.class)))
            .thenReturn(Arrays.asList(resultCategory));

        List<CoreMaterialCategory> list = categoryService.selectCoreMaterialCategoryList(category);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Category", list.get(0).getCategoryName());
    }

    @Test
    public void testSelectCoreMaterialCategoryByPackageType() {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setId(1L);
        category.setCategoryName("Category 1");
        category.setPackageType(1);

        when(categoryMapper.selectCoreMaterialCategoryByPackageType(1))
            .thenReturn(Arrays.asList(category));

        List<CoreMaterialCategory> list = categoryService.selectCoreMaterialCategoryByPackageType(1);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(1), list.get(0).getPackageType());
    }

    @Test
    public void testSelectAllActiveCategory() {
        CoreMaterialCategory category1 = new CoreMaterialCategory();
        category1.setId(1L);
        category1.setCategoryName("Active 1");
        category1.setStatus("0");

        CoreMaterialCategory category2 = new CoreMaterialCategory();
        category2.setId(2L);
        category2.setCategoryName("Active 2");
        category2.setStatus("0");

        when(categoryMapper.selectAllActiveCategory())
            .thenReturn(Arrays.asList(category1, category2));

        List<CoreMaterialCategory> list = categoryService.selectAllActiveCategory();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testInsertCoreMaterialCategory() {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setCategoryName("New Category");
        category.setPackageType(1);

        when(categoryMapper.insertCoreMaterialCategory(any(CoreMaterialCategory.class))).thenReturn(1);

        int result = categoryService.insertCoreMaterialCategory(category);

        assertEquals(1, result);
        verify(categoryMapper).insertCoreMaterialCategory(category);
    }

    @Test
    public void testUpdateCoreMaterialCategory() {
        CoreMaterialCategory category = new CoreMaterialCategory();
        category.setId(1L);
        category.setCategoryName("Updated Category");

        when(categoryMapper.updateCoreMaterialCategory(any(CoreMaterialCategory.class))).thenReturn(1);

        int result = categoryService.updateCoreMaterialCategory(category);

        assertEquals(1, result);
        verify(categoryMapper).updateCoreMaterialCategory(category);
    }

    @Test
    public void testDeleteCoreMaterialCategoryByIds() {
        Long[] ids = {1L, 2L, 3L};
        when(categoryMapper.deleteCoreMaterialCategoryByIds(ids)).thenReturn(3);

        int result = categoryService.deleteCoreMaterialCategoryByIds(ids);

        assertEquals(3, result);
        verify(categoryMapper).deleteCoreMaterialCategoryByIds(ids);
    }

    @Test
    public void testDeleteCoreMaterialCategoryById() {
        when(categoryMapper.deleteCoreMaterialCategoryById(1L)).thenReturn(1);

        int result = categoryService.deleteCoreMaterialCategoryById(1L);

        assertEquals(1, result);
        verify(categoryMapper).deleteCoreMaterialCategoryById(1L);
    }

    @Test
    public void testSelectCoreMaterialCategoryByIdNull() {
        when(categoryMapper.selectCoreMaterialCategoryById(999L)).thenReturn(null);

        CoreMaterialCategory result = categoryService.selectCoreMaterialCategoryById(999L);

        assertNull(result);
    }

    @Test
    public void testSelectCoreMaterialCategoryByPackageTypeEmpty() {
        when(categoryMapper.selectCoreMaterialCategoryByPackageType(999))
            .thenReturn(Arrays.asList());

        List<CoreMaterialCategory> list = categoryService.selectCoreMaterialCategoryByPackageType(999);

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
