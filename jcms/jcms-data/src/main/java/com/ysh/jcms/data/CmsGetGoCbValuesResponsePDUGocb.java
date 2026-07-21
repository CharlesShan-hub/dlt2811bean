// Auto-generated. ASN.1 type: GetGoCbValuesResponsePDUGocb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetGoCbValuesResponsePDUGocb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetGoCbValuesResponsePDUGocb> value;
    public CmsGetGoCbValuesResponsePDUGocb() {}
    public CmsGetGoCbValuesResponsePDUGocb(java.util.List<CmsAnonymousGetGoCbValuesResponsePDUGocb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGoCbValuesResponsePDUGocb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoCbValuesResponsePDUGocb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGoCbValuesResponsePDUGocb", enc, data);
            CmsGetGoCbValuesResponsePDUGocb r = new CmsGetGoCbValuesResponsePDUGocb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetGoCbValuesResponsePDUGocb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
