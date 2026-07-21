// Auto-generated. ASN.1 type: AnonymousGetGoReferenceResponsePDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetGoReference-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetGoReferenceResponsePDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetGoReferenceResponsePDUMemberData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetGoReferenceResponsePDUMemberData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetGoReferenceResponsePDUMemberData", enc, data), CmsAnonymousGetGoReferenceResponsePDUMemberData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
