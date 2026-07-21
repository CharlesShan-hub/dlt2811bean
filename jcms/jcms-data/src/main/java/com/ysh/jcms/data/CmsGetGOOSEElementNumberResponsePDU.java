// Auto-generated. ASN.1 type: GetGOOSEElementNumberResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT Int32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberOffset    [3] IMPLICIT SEQUENCE OF Int16U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetGOOSEElementNumberResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String gocb_reference = null;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public String dat_set = null;
    @JsonProperty public java.util.List<Integer> member_offset = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGOOSEElementNumberResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGOOSEElementNumberResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetGOOSEElementNumberResponsePDU", enc, data), CmsGetGOOSEElementNumberResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
