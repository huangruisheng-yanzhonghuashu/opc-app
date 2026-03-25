package com.opc.common.utils.file;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class FileTypeUtilsTest
{
    @Test
    public void testGetFileTypeFromFile()
    {
        File file = new File("test.txt");
        String type = FileTypeUtils.getFileType(file);
        assertEquals("txt", type);
    }

    @Test
    public void testGetFileTypeFromFileNull()
    {
        String type = FileTypeUtils.getFileType((File) null);
        assertEquals("", type);
    }

    @Test
    public void testGetFileTypeFromString()
    {
        assertEquals("txt", FileTypeUtils.getFileType("document.txt"));
        assertEquals("jpg", FileTypeUtils.getFileType("photo.jpg"));
        assertEquals("png", FileTypeUtils.getFileType("image.PNG"));
        assertEquals("java", FileTypeUtils.getFileType("Test.java"));
    }

    @Test
    public void testGetFileTypeNoExtension()
    {
        assertEquals("", FileTypeUtils.getFileType("README"));
        assertEquals("", FileTypeUtils.getFileType("Makefile"));
    }

    @Test
    public void testGetFileTypeEmpty()
    {
        assertEquals("", FileTypeUtils.getFileType(""));
    }

    @Test
    public void testGetFileTypeMultipleDots()
    {
        assertEquals("gz", FileTypeUtils.getFileType("archive.tar.gz"));
        assertEquals("js", FileTypeUtils.getFileType("script.min.js"));
    }

    @Test
    public void testGetFileExtendNameJPG()
    {
        byte[] photoByte = new byte[20];
        photoByte[6] = 74;
        photoByte[7] = 70;
        photoByte[8] = 73;
        photoByte[9] = 70;

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("JPG", type);
    }

    @Test
    public void testGetFileExtendNameGIF()
    {
        byte[] photoByte = new byte[20];
        photoByte[0] = 71;
        photoByte[1] = 73;
        photoByte[2] = 70;
        photoByte[3] = 56;
        photoByte[4] = 55;
        photoByte[5] = 97;

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("GIF", type);
    }

    @Test
    public void testGetFileExtendNameGIF89()
    {
        byte[] photoByte = new byte[20];
        photoByte[0] = 71;
        photoByte[1] = 73;
        photoByte[2] = 70;
        photoByte[3] = 56;
        photoByte[4] = 57;
        photoByte[5] = 97;

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("GIF", type);
    }

    @Test
    public void testGetFileExtendNameBMP()
    {
        byte[] photoByte = new byte[20];
        photoByte[0] = 66;
        photoByte[1] = 77;

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("BMP", type);
    }

    @Test
    public void testGetFileExtendNamePNG()
    {
        byte[] photoByte = new byte[20];
        photoByte[1] = 80;
        photoByte[2] = 78;
        photoByte[3] = 71;

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("PNG", type);
    }

    @Test
    public void testGetFileExtendNameDefaultJPG()
    {
        byte[] photoByte = new byte[20];

        String type = FileTypeUtils.getFileExtendName(photoByte);
        assertEquals("JPG", type);
    }
}
