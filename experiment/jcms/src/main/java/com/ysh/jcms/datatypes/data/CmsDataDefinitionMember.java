package com.ysh.jcms.datatypes.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * CmsDataDefinitionMember — a single field in a DataDefinition structure.
 *
 * <p>Maps the C struct {@code cms_data_definition_member_t}:
 * {@code { char name[65]; uint8_t fc[3]; int has_fc; struct cms_data_definition *type; }}
 *
 * <p>Thread-safety: not guaranteed.
 */
@Getter
@Accessors(fluent = true)
public class CmsDataDefinitionMember {

    /** Native struct mapping {@code cms_data_definition_member_t}. */
    public static class MemberStruct extends Structure {
        public byte[] name = new byte[65];   // ObjectName (0..64)
        public byte[] fc = new byte[3];       // FunctionalConstraint (2 chars + null)
        public int has_fc;                    // 1 = fc present
        public Pointer type;                  // struct cms_data_definition*

        public MemberStruct() {}

        public MemberStruct(Pointer p) {
            super(p);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("name", "fc", "has_fc", "type");
        }
    }

    /** Java-level fields. */
    public String name;
    public String fc;
    public boolean hasFc;
    public CmsDataDefinition type;

    public CmsDataDefinitionMember() {}

    public CmsDataDefinitionMember(String name, CmsDataDefinition type) {
        this.name = name;
        this.type = type;
    }

    public CmsDataDefinitionMember(String name, String fc, CmsDataDefinition type) {
        this.name = name;
        this.fc = fc;
        this.hasFc = fc != null;
        this.type = type;
    }

    public CmsDataDefinitionMember copy() {
        CmsDataDefinitionMember c = new CmsDataDefinitionMember();
        c.name = name;
        c.fc = fc;
        c.hasFc = hasFc;
        c.type = type != null ? type.copy() : null;
        return c;
    }

    @Override
    public String toString() {
        if (hasFc) {
            return "Member(name=" + name + ", fc=" + fc + ", type=" + type + ")";
        }
        return "Member(name=" + name + ", type=" + type + ")";
    }
}
