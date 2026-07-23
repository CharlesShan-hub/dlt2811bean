// Auto-generated. ASN.1 type: AnonymousGetDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetDataDefinition-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousGetDataDefinitionResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String cdc_type = null;
    @JsonProperty public CmsDataDefinition definition = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetDataDefinitionResponsePDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetDataDefinitionResponsePDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetDataDefinitionResponsePDUData", enc, data), CmsAnonymousGetDataDefinitionResponsePDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
