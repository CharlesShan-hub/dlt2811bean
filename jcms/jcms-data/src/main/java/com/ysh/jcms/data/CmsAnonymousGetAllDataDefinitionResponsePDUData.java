// Auto-generated. ASN.1 type: AnonymousGetAllDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetAllDataDefinition-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousGetAllDataDefinitionResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String cdc_type = null;
    @JsonProperty public CmsDataDefinition definition = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetAllDataDefinitionResponsePDUData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetAllDataDefinitionResponsePDUData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetAllDataDefinitionResponsePDUData", enc, data), CmsAnonymousGetAllDataDefinitionResponsePDUData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
