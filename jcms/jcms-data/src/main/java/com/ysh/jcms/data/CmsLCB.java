// Auto-generated. ASN.1 type: LCB

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * LCB ::= SEQUENCE {
 *     logEna          [1] IMPLICIT BOOLEAN,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     trgOps          [3] IMPLICIT TriggerConditions,
 *     intgPd          [4] IMPLICIT Int32U,
 *     logRef          [5] IMPLICIT ObjectReference,
 *     optFlds         [6] IMPLICIT LcbOptFlds OPTIONAL,
 *     bufTm           [7] IMPLICIT Int32U OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsLCB extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public boolean log_ena = false;
    @JsonProperty public String dat_set = null;
    @JsonProperty public int trg_ops = 0;
    @JsonProperty public int intg_pd = 0;
    @JsonProperty public String log_ref = null;
    @JsonProperty public Integer opt_flds = null;
    @JsonProperty public Integer buf_tm = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("LCB", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsLCB decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("LCB", enc, data), CmsLCB.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
