// Auto-generated. ASN.1 type: AnonymousGetAllCBValuesResponsePDUCbValue

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetAllCBValues-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetAllCBValuesResponsePDUCbValue extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsAnonymousGetAllCBValuesResponsePDUCbValueValue value = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetAllCBValuesResponsePDUCbValue", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetAllCBValuesResponsePDUCbValue decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetAllCBValuesResponsePDUCbValue", enc, data), CmsAnonymousGetAllCBValuesResponsePDUCbValue.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
