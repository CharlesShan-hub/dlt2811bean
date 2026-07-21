// Auto-generated. ASN.1 type: URCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * URCB ::= SEQUENCE {
 *     rptID           [1] IMPLICIT VisibleString (SIZE (0..129)),
 *     rptEna          [2] IMPLICIT BOOLEAN,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT Int32U,
 *     optFlds         [5] IMPLICIT RcbOptFlds,
 *     bufTm           [6] IMPLICIT Int32U,
 *     sqNum           [7] IMPLICIT Int16U,
 *     trgOps          [8] IMPLICIT TriggerConditions,
 *     intgPd          [9] IMPLICIT Int32U,
 *     gi              [10] IMPLICIT BOOLEAN,
 *     resv            [14] IMPLICIT BOOLEAN,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsURCB extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String rpt_id = null;
    @JsonProperty public boolean rpt_ena = false;
    @JsonProperty public String dat_set = null;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public int opt_flds = 0;
    @JsonProperty public int buf_tm = 0;
    @JsonProperty public int sq_num = 0;
    @JsonProperty public int trg_ops = 0;
    @JsonProperty public int intg_pd = 0;
    @JsonProperty public boolean gi = false;
    @JsonProperty public boolean resv = false;
    @JsonProperty public byte[] owner = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("URCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsURCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("URCB", enc, data), CmsURCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
