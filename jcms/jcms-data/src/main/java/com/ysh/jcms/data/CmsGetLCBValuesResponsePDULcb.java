// Auto-generated. ASN.1 type: GetLCBValuesResponsePDULcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetLCBValuesResponsePDULcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetLCBValuesResponsePDULcb> value;
    public CmsGetLCBValuesResponsePDULcb() {}
    public CmsGetLCBValuesResponsePDULcb(java.util.List<CmsAnonymousGetLCBValuesResponsePDULcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLCBValuesResponsePDULcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLCBValuesResponsePDULcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLCBValuesResponsePDULcb", enc, data);
            CmsGetLCBValuesResponsePDULcb r = new CmsGetLCBValuesResponsePDULcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetLCBValuesResponsePDULcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
