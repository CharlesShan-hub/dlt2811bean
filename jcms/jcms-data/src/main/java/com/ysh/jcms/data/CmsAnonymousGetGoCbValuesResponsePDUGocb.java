// Auto-generated. ASN.1 type: AnonymousGetGoCbValuesResponsePDUGocb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetGoCbValues-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetGoCbValuesResponsePDUGocb extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public int error;
    @JsonIgnore public CmsGoCB value;
    @JsonAnyGetter
    public java.util.Map<String, Object> serializeChoice() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        if (_choice != null) {
            map.put("_choice", _choice);
            if ("error".equals(_choice)) map.put("error", error);
            if ("value".equals(_choice)) map.put("value", value);
        }
        return map;
    }
    @JsonAnySetter
    public void deserializeChoice(String key, Object value) {
        if ("_choice".equals(key)) return;
        this._choice = key;
        if ("error".equals(key)) {
            this.error = MAPPER.convertValue(value, int.class);
        }
        if ("value".equals(key)) {
            this.value = MAPPER.convertValue(value, CmsGoCB.class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetGoCbValuesResponsePDUGocb", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetGoCbValuesResponsePDUGocb decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetGoCbValuesResponsePDUGocb", enc, data), CmsAnonymousGetGoCbValuesResponsePDUGocb.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
