// Auto-generated. ASN.1 type: BRCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * BRCB ::= SEQUENCE {
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
 *     purgeBuf        [11] IMPLICIT BOOLEAN,
 *     entryID         [12] IMPLICIT EntryID,
 *     timeOfEntry     [13] IMPLICIT EntryTime,
 *     resvTms         [14] IMPLICIT Int16 OPTIONAL,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsBRCB extends CmsBase {
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
    @JsonProperty public boolean purge_buf = false;
    @JsonProperty public byte[] entry_id = null;
    @JsonProperty public byte[] time_of_entry = null;
    @JsonProperty public Integer resv_tms = null;
    @JsonProperty public byte[] owner = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("BRCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsBRCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("BRCB", enc, data), CmsBRCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
