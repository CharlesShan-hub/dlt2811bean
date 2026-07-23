// Auto-generated. ASN.1 type: GoCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GoCB ::= SEQUENCE {
 *     goEna           [1] IMPLICIT BOOLEAN,
 *     goID            [2] IMPLICIT VisibleString (SIZE (0..129)),
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT Int32U,
 *     ndsCom          [5] IMPLICIT BOOLEAN,
 *     dstAddress      [6] IMPLICIT PhyComAddr OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGoCB extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public boolean go_ena = false;
    @JsonProperty public String go_id = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public boolean nds_com = false;
    @JsonProperty public CmsPhyComAddr dst_address = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GoCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGoCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GoCB", enc, data), CmsGoCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
