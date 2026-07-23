// Auto-generated. ASN.1 type: RpcCallRequestPDUReq

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsRpcCallRequestPDUReq extends CmsBase {
    public String _choice;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonIgnore public CmsData reqData;
    @JsonIgnore public byte[] callID;
    @JsonAnyGetter
    public java.util.Map<String, Object> serializeChoice() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        if (_choice != null) {
            map.put("_choice", _choice);
            if ("reqData".equals(_choice)) map.put("reqData", reqData);
            if ("callID".equals(_choice)) map.put("callID", callID);
        }
        return map;
    }
    @JsonAnySetter
    public void deserializeChoice(String key, Object value) {
        if ("_choice".equals(key)) return;
        this._choice = key;
        if ("reqData".equals(key)) {
            this.reqData = MAPPER.convertValue(value, CmsData.class);
        }
        if ("callID".equals(key)) {
            this.callID = MAPPER.convertValue(value, byte[].class);
        }
    }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("RpcCallRequestPDUReq", enc, MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsRpcCallRequestPDUReq decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("RpcCallRequestPDUReq", enc, data), CmsRpcCallRequestPDUReq.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
