// Auto-generated. ASN.1 type: GetAllDataValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllDataValues-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         value         [1] IMPLICIT Data
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetAllDataValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetAllDataValuesResponsePDUData> data = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataValuesResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataValuesResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllDataValuesResponsePDU", enc, data), CmsGetAllDataValuesResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
