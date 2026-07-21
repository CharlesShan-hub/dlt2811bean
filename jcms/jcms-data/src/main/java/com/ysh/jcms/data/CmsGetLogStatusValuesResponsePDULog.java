// Auto-generated. ASN.1 type: GetLogStatusValuesResponsePDULog

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetLogStatusValuesResponsePDULog extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetLogStatusValuesResponsePDULog> value;
    public CmsGetLogStatusValuesResponsePDULog() {}
    public CmsGetLogStatusValuesResponsePDULog(java.util.List<CmsAnonymousGetLogStatusValuesResponsePDULog> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLogStatusValuesResponsePDULog", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogStatusValuesResponsePDULog decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLogStatusValuesResponsePDULog", enc, data);
            CmsGetLogStatusValuesResponsePDULog r = new CmsGetLogStatusValuesResponsePDULog();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetLogStatusValuesResponsePDULog>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
