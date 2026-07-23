// Auto-generated. ASN.1 type: AnonymousGetLogStatusValuesResponsePDULogValue

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetLogStatusValues-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetLogStatusValuesResponsePDULogValue extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] old_entr_tm = null;
    @JsonProperty public byte[] new_entr_tm = null;
    @JsonProperty public byte[] old_entr = null;
    @JsonProperty public byte[] new_entr = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetLogStatusValuesResponsePDULogValue", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetLogStatusValuesResponsePDULogValue decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetLogStatusValuesResponsePDULogValue", enc, data), CmsAnonymousGetLogStatusValuesResponsePDULogValue.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
