package com.opc.common.utils.sql;

import com.opc.common.exception.UtilException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUtilTest {

    @Test
    public void testEscapeOrderBySqlValid() {
        String value = "create_time desc";
        String result = SqlUtil.escapeOrderBySql(value);
        assertEquals(value, result);
    }

    @Test
    public void testEscapeOrderBySqlEmpty() {
        assertEquals("", SqlUtil.escapeOrderBySql(""));
        assertNull(SqlUtil.escapeOrderBySql(null));
    }

    @Test
    public void testEscapeOrderBySqlInvalid() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.escapeOrderBySql("create_time; drop table users");
        });
    }

    @Test
    public void testEscapeOrderBySqlTooLong() {
        StringBuilder longValue = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            longValue.append("a");
        }
        assertThrows(UtilException.class, () -> {
            SqlUtil.escapeOrderBySql(longValue.toString());
        });
    }

    @Test
    public void testIsValidOrderBySqlValid() {
        assertTrue(SqlUtil.isValidOrderBySql("create_time desc"));
        assertTrue(SqlUtil.isValidOrderBySql("id asc, name desc"));
        assertTrue(SqlUtil.isValidOrderBySql("user.create_time"));
    }

    @Test
    public void testIsValidOrderBySqlInvalid() {
        assertFalse(SqlUtil.isValidOrderBySql("create_time; delete from users"));
        assertFalse(SqlUtil.isValidOrderBySql("create_time -- comment"));
        assertFalse(SqlUtil.isValidOrderBySql("create_time /* comment */"));
    }

    @Test
    public void testFilterKeywordSafe() {
        // Should not throw exception
        assertDoesNotThrow(() -> {
            SqlUtil.filterKeyword("safe_value");
        });
    }

    @Test
    public void testFilterKeywordEmpty() {
        assertDoesNotThrow(() -> {
            SqlUtil.filterKeyword("");
            SqlUtil.filterKeyword(null);
        });
    }

    @Test
    public void testFilterKeywordWithInjection() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("1 and 1=1");
        });
    }

    @Test
    public void testFilterKeywordWithSelect() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("select * from users");
        });
    }

    @Test
    public void testFilterKeywordWithUnion() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("1 union select * from users");
        });
    }

    @Test
    public void testFilterKeywordWithInsert() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("insert into users values");
        });
    }

    @Test
    public void testFilterKeywordWithDelete() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("delete from users");
        });
    }

    @Test
    public void testFilterKeywordWithUpdate() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("update users set");
        });
    }

    @Test
    public void testFilterKeywordWithDrop() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("drop table users");
        });
    }

    @Test
    public void testFilterKeywordCaseInsensitive() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("SELECT * FROM users");
        });
    }
}
