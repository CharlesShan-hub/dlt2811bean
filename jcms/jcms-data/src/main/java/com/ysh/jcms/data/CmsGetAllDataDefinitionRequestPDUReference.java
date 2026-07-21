// Auto-generated. ASN.1 type: GetAllDataDefinitionRequestPDUReference

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetAllDataDefinitionRequestPDUReference extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public String ldName;
    @JsonIgnore public String lnReference;
    @JsonAnyGetter
    public java.util.Map<String, Object> serializeChoice() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        if (_choice != null) {
            map.put("_choice", _choice);
            if ("ldName".equals(_choice)) map.put("ldName", ldName);
            if ("lnReference".equals(_choice)) map.put("lnReference", lnReference);
        }
        return map;
    }
    @JsonAnySetter
    public void deserializeChoice(String key, Object value) {
        if ("_choice".equals(key)) return;
        this._choice = key;
        if ("ldName".equals(key)) {
            this.ldName = MAPPER.convertValue(value, String.class);
        }
        if ("lnReference".equals(key)) {
            this.lnReference = MAPPER.convertValue(value, String.class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataDefinitionRequestPDUReference", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataDefinitionRequestPDUReference decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllDataDefinitionRequestPDUReference", enc, data), CmsGetAllDataDefinitionRequestPDUReference.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
