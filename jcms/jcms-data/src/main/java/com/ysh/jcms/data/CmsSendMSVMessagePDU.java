// Auto-generated. ASN.1 type: SendMSVMessagePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SendMSVMessage-PDU ::= SEQUENCE {
 *     msvID           [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     smpCnt          [2] IMPLICIT Int16U,
 *     confRev         [3] IMPLICIT Int32U,
 *     refTm           [4] IMPLICIT TimeStamp OPTIONAL,
 *     smpSynch        [5] IMPLICIT Int8U,
 *     smpRate         [6] IMPLICIT Int16U OPTIONAL,
 *     simulation      [7] IMPLICIT BOOLEAN,
 *     sample          [8] IMPLICIT SEQUENCE OF Data,
 *     smpMod          [9] IMPLICIT SmpMod OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsSendMSVMessagePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String msv_id = null;
    @JsonProperty public String dat_set = null;
    @JsonProperty public int smp_cnt = 0;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public byte[] ref_tm = null;
    @JsonProperty public int smp_synch = 0;
    @JsonProperty public Integer smp_rate = null;
    @JsonProperty public boolean simulation = false;
    @JsonProperty public java.util.List<CmsData> sample = new java.util.ArrayList<>();
    @JsonProperty public Integer smp_mod = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SendMSVMessagePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSendMSVMessagePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SendMSVMessagePDU", enc, data), CmsSendMSVMessagePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
