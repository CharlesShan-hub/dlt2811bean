// Auto-generated. ASN.1 type: GetBRCBValuesResponsePDUBrcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetBRCBValuesResponsePDUBrcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetBRCBValuesResponsePDUBrcb> value;
    public CmsGetBRCBValuesResponsePDUBrcb() {}
    public CmsGetBRCBValuesResponsePDUBrcb(java.util.List<CmsAnonymousGetBRCBValuesResponsePDUBrcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetBRCBValuesResponsePDUBrcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetBRCBValuesResponsePDUBrcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetBRCBValuesResponsePDUBrcb", enc, data);
            CmsGetBRCBValuesResponsePDUBrcb r = new CmsGetBRCBValuesResponsePDUBrcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetBRCBValuesResponsePDUBrcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
