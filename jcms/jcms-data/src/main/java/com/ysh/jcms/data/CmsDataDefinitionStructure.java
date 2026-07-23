// Auto-generated. ASN.1 type: DataDefinitionStructure

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsDataDefinitionStructure extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousDataDefinitionStructure> value;
    public CmsDataDefinitionStructure() {}
    public CmsDataDefinitionStructure(java.util.List<CmsAnonymousDataDefinitionStructure> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("DataDefinitionStructure", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDataDefinitionStructure decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("DataDefinitionStructure", enc, data);
            CmsDataDefinitionStructure r = new CmsDataDefinitionStructure();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousDataDefinitionStructure>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
