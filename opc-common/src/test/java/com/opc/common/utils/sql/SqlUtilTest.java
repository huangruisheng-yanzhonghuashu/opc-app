package com.opc.common.utils.sql;

import com.opc.common.exception.UtilException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SQL操作工具类测试
 */
public class SqlUtilTest {

    @Test
    @DisplayName("验证ORDER BY SQL-有效值")
    public void testIsValidOrderBySql_Valid() {
        assertTrue(SqlUtil.isValidOrderBySql("create_time"));
        assertTrue(SqlUtil.isValidOrderBySql("create_time desc"));
        assertTrue(SqlUtil.isValidOrderBySql("create_time asc"));
        assertTrue(SqlUtil.isValidOrderBySql("user_name, create_time desc"));
        assertTrue(SqlUtil.isValidOrderBySql("dept_id asc, user_name desc"));
        assertTrue(SqlUtil.isValidOrderBySql("field1, field2, field3"));
    }

    @Test
    @DisplayName("验证ORDER BY SQL-无效值")
    public void testIsValidOrderBySql_Invalid() {
        assertFalse(SqlUtil.isValidOrderBySql("create_time; drop table user;"));
        assertFalse(SqlUtil.isValidOrderBySql("1=1"));
        assertFalse(SqlUtil.isValidOrderBySql("user_name'"));
        assertFalse(SqlUtil.isValidOrderBySql("user_name--"));
        assertFalse(SqlUtil.isValidOrderBySql("user_name/*"));
        assertFalse(SqlUtil.isValidOrderBySql(""));
    }

    @Test
    @DisplayName("转义ORDER BY SQL-有效值")
    public void testEscapeOrderBySql_Valid() {
        assertEquals("create_time", SqlUtil.escapeOrderBySql("create_time"));
        assertEquals("create_time desc", SqlUtil.escapeOrderBySql("create_time desc"));
        assertEquals("user_name, create_time", SqlUtil.escapeOrderBySql("user_name, create_time"));
    }

    @Test
    @DisplayName("转义ORDER BY SQL-无效值抛出异常")
    public void testEscapeOrderBySql_Invalid() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.escapeOrderBySql("create_time; drop table user;");
        });
    }

    @Test
    @DisplayName("转义ORDER BY SQL-超长值抛出异常")
    public void testEscapeOrderBySql_TooLong() {
        StringBuilder longSql = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            longSql.append("a");
        }
        assertThrows(UtilException.class, () -> {
            SqlUtil.escapeOrderBySql(longSql.toString());
        });
    }

    @Test
    @DisplayName("转义ORDER BY SQL-空值")
    public void testEscapeOrderBySql_Empty() {
        assertNull(SqlUtil.escapeOrderBySql(null));
        assertEquals("", SqlUtil.escapeOrderBySql(""));
    }

    @Test
    @DisplayName("SQL关键字过滤-正常值")
    public void testFilterKeyword_Normal() {
        // 正常值不应抛出异常
        assertDoesNotThrow(() -> {
            SqlUtil.filterKeyword("normal_value");
            SqlUtil.filterKeyword("user_name");
            SqlUtil.filterKeyword("test123");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含and")
    public void testFilterKeyword_And() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("1 and 1=1");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含select")
    public void testFilterKeyword_Select() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("select * from user");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含insert")
    public void testFilterKeyword_Insert() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("insert into user values");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含delete")
    public void testFilterKeyword_Delete() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("delete from user");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含update")
    public void testFilterKeyword_Update() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("update user set");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含drop")
    public void testFilterKeyword_Drop() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("drop table user");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含union")
    public void testFilterKeyword_Union() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("union select");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含sleep")
    public void testFilterKeyword_Sleep() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("sleep(5)");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-空值")
    public void testFilterKeyword_Empty() {
        assertDoesNotThrow(() -> {
            SqlUtil.filterKeyword(null);
            SqlUtil.filterKeyword("");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-大小写不敏感")
    public void testFilterKeyword_CaseInsensitive() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("SELECT * FROM user");
        });
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("Select * From user");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含exec")
    public void testFilterKeyword_Exec() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("exec xp_cmdshell");
        });
    }

    @Test
    @DisplayName("SQL关键字过滤-包含information_schema")
    public void testFilterKeyword_InformationSchema() {
        assertThrows(UtilException.class, () -> {
            SqlUtil.filterKeyword("information_schema.tables");
        });
    }
}
