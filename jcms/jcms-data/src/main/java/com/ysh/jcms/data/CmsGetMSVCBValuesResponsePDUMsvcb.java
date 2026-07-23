// Auto-generated. ASN.1 type: GetMSVCBValuesResponsePDUMsvcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetMSVCBValuesResponsePDUMsvcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetMSVCBValuesResponsePDUMsvcb> value;
    public CmsGetMSVCBValuesResponsePDUMsvcb() {}
    public CmsGetMSVCBValuesResponsePDUMsvcb(java.util.List<CmsAnonymousGetMSVCBValuesResponsePDUMsvcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetMSVCBValuesResponsePDUMsvcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetMSVCBValuesResponsePDUMsvcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetMSVCBValuesResponsePDUMsvcb", enc, data);
            CmsGetMSVCBValuesResponsePDUMsvcb r = new CmsGetMSVCBValuesResponsePDUMsvcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetMSVCBValuesResponsePDUMsvcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
