package com.opc.common.utils.file;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MimeTypeUtilsTest
{
    @Test
    public void testImagePng()
    {
        assertEquals("image/png", MimeTypeUtils.IMAGE_PNG);
    }

    @Test
    public void testImageJpg()
    {
        assertEquals("image/jpg", MimeTypeUtils.IMAGE_JPG);
    }

    @Test
    public void testImageJpeg()
    {
        assertEquals("image/jpeg", MimeTypeUtils.IMAGE_JPEG);
    }

    @Test
    public void testImageBmp()
    {
        assertEquals("image/bmp", MimeTypeUtils.IMAGE_BMP);
    }

    @Test
    public void testImageGif()
    {
        assertEquals("image/gif", MimeTypeUtils.IMAGE_GIF);
    }

    @Test
    public void testImageExtension()
    {
        assertNotNull(MimeTypeUtils.IMAGE_EXTENSION);
        assertEquals(5, MimeTypeUtils.IMAGE_EXTENSION.length);
        assertTrue(contains(MimeTypeUtils.IMAGE_EXTENSION, "bmp"));
        assertTrue(contains(MimeTypeUtils.IMAGE_EXTENSION, "gif"));
        assertTrue(contains(MimeTypeUtils.IMAGE_EXTENSION, "jpg"));
        assertTrue(contains(MimeTypeUtils.IMAGE_EXTENSION, "jpeg"));
        assertTrue(contains(MimeTypeUtils.IMAGE_EXTENSION, "png"));
    }

    @Test
    public void testFlashExtension()
    {
        assertNotNull(MimeTypeUtils.FLASH_EXTENSION);
        assertEquals(2, MimeTypeUtils.FLASH_EXTENSION.length);
        assertTrue(contains(MimeTypeUtils.FLASH_EXTENSION, "swf"));
        assertTrue(contains(MimeTypeUtils.FLASH_EXTENSION, "flv"));
    }

    @Test
    public void testMediaExtension()
    {
        assertNotNull(MimeTypeUtils.MEDIA_EXTENSION);
        assertTrue(MimeTypeUtils.MEDIA_EXTENSION.length > 0);
        assertTrue(contains(MimeTypeUtils.MEDIA_EXTENSION, "swf"));
        assertTrue(contains(MimeTypeUtils.MEDIA_EXTENSION, "flv"));
    }

    @Test
    public void testVideoExtension()
    {
        assertNotNull(MimeTypeUtils.VIDEO_EXTENSION);
        assertTrue(MimeTypeUtils.VIDEO_EXTENSION.length > 0);
        assertTrue(contains(MimeTypeUtils.VIDEO_EXTENSION, "mp4"));
    }

    @Test
    public void testDefaultAllowedExtension()
    {
        assertNotNull(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        assertTrue(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION.length > 0);
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "jpg"));
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "png"));
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "pdf"));
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "doc"));
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "xls"));
        assertTrue(contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "zip"));
    }

    @Test
    public void testGetExtensionPng()
    {
        assertEquals("png", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_PNG));
    }

    @Test
    public void testGetExtensionJpg()
    {
        assertEquals("jpg", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_JPG));
    }

    @Test
    public void testGetExtensionJpeg()
    {
        assertEquals("jpeg", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_JPEG));
    }

    @Test
    public void testGetExtensionBmp()
    {
        assertEquals("bmp", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_BMP));
    }

    @Test
    public void testGetExtensionGif()
    {
        assertEquals("gif", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_GIF));
    }

    @Test
    public void testGetExtensionUnknown()
    {
        assertEquals("", MimeTypeUtils.getExtension("application/unknown"));
    }

    @Test
    public void testGetExtensionEmpty()
    {
        assertEquals("", MimeTypeUtils.getExtension(""));
    }

    private boolean contains(String[] array, String value)
    {
        for (String s : array)
        {
            if (s.equals(value))
            {
                return true;
            }
        }
        return false;
    }
}
