// Auto-generated. ASN.1 type: AnonymousGetAllDataValuesResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetAllDataValues-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetAllDataValuesResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public CmsData value = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetAllDataValuesResponsePDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetAllDataValuesResponsePDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetAllDataValuesResponsePDUData", enc, data), CmsAnonymousGetAllDataValuesResponsePDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
