// Auto-generated. ASN.1 type: TimeActivatedOperateTerminationRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * TimeActivatedOperateTermination-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT Int8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsTimeActivatedOperateTerminationRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData ctl_val = null;
    @JsonProperty public byte[] oper_tm = null;
    @JsonProperty public CmsOriginator origin = null;
    @JsonProperty public int ctl_num = 0;
    @JsonProperty public byte[] t = null;
    @JsonProperty public boolean test = false;
    @JsonProperty public int check = 0;
    @JsonProperty public Integer add_cause = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("TimeActivatedOperateTerminationRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsTimeActivatedOperateTerminationRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("TimeActivatedOperateTerminationRequestPDU", enc, data), CmsTimeActivatedOperateTerminationRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
