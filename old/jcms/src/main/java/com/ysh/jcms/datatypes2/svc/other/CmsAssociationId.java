package com.ysh.jcms.datatypes2.svc.other;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_association_id_t — OCTET STRING (SIZE(0..64)).
 *
 * C: typedef struct { uint8_t data[64]; int len; } cms_association_id_t;
 */
public class CmsAssociationId extends Structure {
    public static final int MAX_LEN = 64;

    public byte[] data = new byte[MAX_LEN];
    public int len;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("data", "len");
    }
}
