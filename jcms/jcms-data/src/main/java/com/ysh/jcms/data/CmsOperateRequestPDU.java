// Auto-generated. ASN.1 type: OperateRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Operate-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT Int8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsOperateRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData ctl_val = null;
    @JsonProperty public CmsOriginator origin = null;
    @JsonProperty public int ctl_num = 0;
    @JsonProperty public byte[] t = null;
    @JsonProperty public boolean test = false;
    @JsonProperty public int check = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("OperateRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsOperateRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("OperateRequestPDU", enc, data), CmsOperateRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
