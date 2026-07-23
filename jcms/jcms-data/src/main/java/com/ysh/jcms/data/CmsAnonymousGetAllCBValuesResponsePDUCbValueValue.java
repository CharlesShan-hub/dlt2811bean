// Auto-generated. ASN.1 type: AnonymousGetAllCBValuesResponsePDUCbValueValue

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
public class CmsAnonymousGetAllCBValuesResponsePDUCbValueValue extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public CmsBRCB brcb;
    @JsonIgnore public CmsURCB urcb;
    @JsonIgnore public CmsLCB lcb;
    @JsonIgnore public CmsSGCB sgcb;
    @JsonIgnore public CmsGoCB gocb;
    @JsonIgnore public CmsMSVCB msvcb;
    @JsonAnyGetter
    public java.util.Map<String, Object> serializeChoice() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        if (_choice != null) {
            map.put("_choice", _choice);
            if ("brcb".equals(_choice)) map.put("brcb", brcb);
            if ("urcb".equals(_choice)) map.put("urcb", urcb);
            if ("lcb".equals(_choice)) map.put("lcb", lcb);
            if ("sgcb".equals(_choice)) map.put("sgcb", sgcb);
            if ("gocb".equals(_choice)) map.put("gocb", gocb);
            if ("msvcb".equals(_choice)) map.put("msvcb", msvcb);
        }
        return map;
    }
    @JsonAnySetter
    public void deserializeChoice(String key, Object value) {
        if ("_choice".equals(key)) return;
        this._choice = key;
        if ("brcb".equals(key)) {
            this.brcb = MAPPER.convertValue(value, CmsBRCB.class);
        }
        if ("urcb".equals(key)) {
            this.urcb = MAPPER.convertValue(value, CmsURCB.class);
        }
        if ("lcb".equals(key)) {
            this.lcb = MAPPER.convertValue(value, CmsLCB.class);
        }
        if ("sgcb".equals(key)) {
            this.sgcb = MAPPER.convertValue(value, CmsSGCB.class);
        }
        if ("gocb".equals(key)) {
            this.gocb = MAPPER.convertValue(value, CmsGoCB.class);
        }
        if ("msvcb".equals(key)) {
            this.msvcb = MAPPER.convertValue(value, CmsMSVCB.class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetAllCBValuesResponsePDUCbValueValue", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetAllCBValuesResponsePDUCbValueValue decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetAllCBValuesResponsePDUCbValueValue", enc, data), CmsAnonymousGetAllCBValuesResponsePDUCbValueValue.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
