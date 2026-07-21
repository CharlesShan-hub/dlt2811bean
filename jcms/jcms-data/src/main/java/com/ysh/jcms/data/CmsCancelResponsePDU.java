// Auto-generated. ASN.1 type: CancelResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Cancel-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT Int8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsCancelResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData ctl_val = null;
    @JsonProperty public byte[] oper_tm = null;
    @JsonProperty public CmsOriginator origin = null;
    @JsonProperty public int ctl_num = 0;
    @JsonProperty public byte[] t = null;
    @JsonProperty public boolean test = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("CancelResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCancelResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("CancelResponsePDU", enc, data), CmsCancelResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
