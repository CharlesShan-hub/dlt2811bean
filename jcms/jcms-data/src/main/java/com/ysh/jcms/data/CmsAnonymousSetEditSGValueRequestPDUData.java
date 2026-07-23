// Auto-generated. ASN.1 type: AnonymousSetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within SetEditSGValue-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousSetEditSGValueRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData value = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousSetEditSGValueRequestPDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousSetEditSGValueRequestPDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousSetEditSGValueRequestPDUData", enc, data), CmsAnonymousSetEditSGValueRequestPDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
