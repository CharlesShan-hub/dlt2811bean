// Auto-generated. ASN.1 type: OperateErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Operate-ErrorPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT Int8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsOperateErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData ctl_val = null;
    @JsonProperty public CmsOriginator origin = null;
    @JsonProperty public int ctl_num = 0;
    @JsonProperty public byte[] t = null;
    @JsonProperty public boolean test = false;
    @JsonProperty public int check = 0;
    @JsonProperty public int add_cause = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("OperateErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsOperateErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("OperateErrorPDU", enc, data), CmsOperateErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
