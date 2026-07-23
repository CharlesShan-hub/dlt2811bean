// Auto-generated. ASN.1 type: GetSGCBValuesResponsePDUSgscb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetSGCBValuesResponsePDUSgscb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetSGCBValuesResponsePDUSgscb> value;
    public CmsGetSGCBValuesResponsePDUSgscb() {}
    public CmsGetSGCBValuesResponsePDUSgscb(java.util.List<CmsAnonymousGetSGCBValuesResponsePDUSgscb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetSGCBValuesResponsePDUSgscb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetSGCBValuesResponsePDUSgscb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetSGCBValuesResponsePDUSgscb", enc, data);
            CmsGetSGCBValuesResponsePDUSgscb r = new CmsGetSGCBValuesResponsePDUSgscb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetSGCBValuesResponsePDUSgscb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
