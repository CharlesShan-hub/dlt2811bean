// Auto-generated. ASN.1 type: DataDefinitionArray

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsDataDefinitionArray extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int number_of_element = 0;
    @JsonProperty public CmsDataDefinition element_type = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("DataDefinitionArray", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDataDefinitionArray decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("DataDefinitionArray", enc, data), CmsDataDefinitionArray.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
