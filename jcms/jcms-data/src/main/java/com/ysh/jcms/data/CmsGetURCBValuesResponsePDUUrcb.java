// Auto-generated. ASN.1 type: GetURCBValuesResponsePDUUrcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetURCBValuesResponsePDUUrcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetURCBValuesResponsePDUUrcb> value;
    public CmsGetURCBValuesResponsePDUUrcb() {}
    public CmsGetURCBValuesResponsePDUUrcb(java.util.List<CmsAnonymousGetURCBValuesResponsePDUUrcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetURCBValuesResponsePDUUrcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetURCBValuesResponsePDUUrcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetURCBValuesResponsePDUUrcb", enc, data);
            CmsGetURCBValuesResponsePDUUrcb r = new CmsGetURCBValuesResponsePDUUrcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetURCBValuesResponsePDUUrcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
