package com.ysh.jcms.datatypes.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDataDefinitionMember")
class CmsDataDefinitionMemberTest {

    @Test
    void constructorWithNameAndType() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember member = new CmsDataDefinitionMember("temperature", type);
        assertEquals("temperature", member.name);
        assertFalse(member.hasFc);
        assertSame(type, member.type);
    }

    @Test
    void constructorWithFc() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember member = new CmsDataDefinitionMember("voltage", "MX", type);
        assertEquals("voltage", member.name);
        assertEquals("MX", member.fc);
        assertTrue(member.hasFc);
        assertSame(type, member.type);
    }

    @Test
    void constructorWithNullFc() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember member = new CmsDataDefinitionMember("status", type);
        assertNull(member.fc);
        assertFalse(member.hasFc);
    }

    @Test
    void copy() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember original = new CmsDataDefinitionMember("current", "ST", type);
        CmsDataDefinitionMember cloned = original.copy();

        assertNotSame(original, cloned);
        assertEquals(original.name, cloned.name);
        assertEquals(original.fc, cloned.fc);
        assertEquals(original.hasFc, cloned.hasFc);
        assertNotNull(cloned.type);
    }

    @Test
    void toString_withFc() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember member = new CmsDataDefinitionMember("power", "MX", type);
        String str = member.toString();
        assertTrue(str.contains("power"));
        assertTrue(str.contains("MX"));
    }

    @Test
    void toString_withoutFc() {
        CmsDataDefinition type = new CmsDataDefinition();
        CmsDataDefinitionMember member = new CmsDataDefinitionMember("status", type);
        String str = member.toString();
        assertTrue(str.contains("status"));
        assertFalse(str.contains("fc="));
    }
}
