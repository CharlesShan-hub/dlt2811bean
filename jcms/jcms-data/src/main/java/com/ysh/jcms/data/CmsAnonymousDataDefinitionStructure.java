// Auto-generated. ASN.1 type: AnonymousDataDefinitionStructure

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within DataDefinition ::= CHOICE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAnonymousDataDefinitionStructure extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String name = null;
    @JsonProperty public String fc = null;
    @JsonProperty public CmsDataDefinition r_type = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousDataDefinitionStructure", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousDataDefinitionStructure decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousDataDefinitionStructure", enc, data), CmsAnonymousDataDefinitionStructure.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
