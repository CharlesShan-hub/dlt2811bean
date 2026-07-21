// Auto-generated. ASN.1 type: GetDataDirectoryResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDirectory-ResponsePDU ::= SEQUENCE {
 *     dataAttribute    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetDataDirectoryResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDirectoryResponsePDUDataAttribute> data_attribute = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDirectoryResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDirectoryResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetDataDirectoryResponsePDU", enc, data), CmsGetDataDirectoryResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
