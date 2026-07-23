// Auto-generated. ASN.1 type: GetAllCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllCBValues-ResponsePDU ::= SEQUENCE {
 *     cbValue          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         value         [1] IMPLICIT CHOICE {
 *             brcb        [0] IMPLICIT BRCB,
 *             urcb        [1] IMPLICIT URCB,
 *             lcb         [2] IMPLICIT LCB,
 *             sgcb        [3] IMPLICIT SGCB,
 *             gocb        [4] IMPLICIT GoCB,
 *             msvcb       [5] IMPLICIT MSVCB
 *         }
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetAllCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetAllCBValuesResponsePDUCbValue> cb_value = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllCBValuesResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllCBValuesResponsePDU", enc, data), CmsGetAllCBValuesResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
