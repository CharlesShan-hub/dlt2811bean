// Auto-generated. ASN.1 type: GetDataDefinitionResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDefinition-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *         definition    [1] IMPLICIT DataDefinition
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetDataDefinitionResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDefinitionResponsePDUData> data = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDefinitionResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDefinitionResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetDataDefinitionResponsePDU", enc, data), CmsGetDataDefinitionResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
