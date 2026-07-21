// Auto-generated. ASN.1 type: MSVCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * MSVCB ::= SEQUENCE {
 *     svEna           [1] IMPLICIT BOOLEAN,
 *     msvID           [2] IMPLICIT VisibleString (SIZE (0..129)),
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT Int32U,
 *     smpMod          [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate         [6] IMPLICIT Int16U,
 *     optFlds         [7] IMPLICIT MsvcbOptFlds,
 *     dstAddress      [8] IMPLICIT PhyComAddr OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsMSVCB extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public boolean sv_ena = false;
    @JsonProperty public String msv_id = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public Integer smp_mod = null;
    @JsonProperty public int smp_rate = 0;
    @JsonProperty public int opt_flds = 0;
    @JsonProperty public CmsPhyComAddr dst_address = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("MSVCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsMSVCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("MSVCB", enc, data), CmsMSVCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
