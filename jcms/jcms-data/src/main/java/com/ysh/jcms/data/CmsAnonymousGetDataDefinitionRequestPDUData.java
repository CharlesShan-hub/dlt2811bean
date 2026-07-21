// Auto-generated. ASN.1 type: AnonymousGetDataDefinitionRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetDataDefinition-RequestPDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousGetDataDefinitionRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetDataDefinitionRequestPDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetDataDefinitionRequestPDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetDataDefinitionRequestPDUData", enc, data), CmsAnonymousGetDataDefinitionRequestPDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
