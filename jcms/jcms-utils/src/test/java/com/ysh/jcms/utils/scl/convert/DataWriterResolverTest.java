package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class DataWriterResolverTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl.reader.SclReader reader = new com.ysh.jcms.utils.scl.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSetValueDaLevel() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        int err = DataWriterResolver.setValue(nav, "42");
        assertEquals(CmsServiceError.NO_ERROR, err);
        Navigator nav2 = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        assertEquals("42", nav2.daiValue());
    }

    @Test
    public void testSetValueInvalidBoolean() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        int err = DataWriterResolver.setValue(nav, "not-a-boolean");
        // stVal is INT32 not BOOLEAN, "not-a-boolean" can't be parsed as int
        assertEquals(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT, err);
    }

    @Test
    public void testValidateAndConvertBoolean() {
        assertEquals("true", DataWriterResolver.validateAndConvert("true", "BOOLEAN"));
        assertEquals("false", DataWriterResolver.validateAndConvert("0", "BOOLEAN"));
        assertNull(DataWriterResolver.validateAndConvert("x", "BOOLEAN"));
    }

    @Test
    public void testValidateAndConvertInt32() {
        assertEquals("42", DataWriterResolver.validateAndConvert("42", "INT32"));
        assertNull(DataWriterResolver.validateAndConvert("abc", "INT32"));
    }

    @Test
    public void testValidateAndConvertInt8Range() {
        assertEquals("128", DataWriterResolver.validateAndConvert("128", "INT8U"));
        assertNull(DataWriterResolver.validateAndConvert("256", "INT8U"));
    }

    @Test
    public void testSetValueInvalidRef() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LLN0"); // LN level
        int err = DataWriterResolver.setValue(nav, "x");
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, err);
    }
}
