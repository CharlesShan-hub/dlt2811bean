// Auto-generated. ASN.1 type: GetDataDirectoryResponsePDUDataAttribute

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetDataDirectoryResponsePDUDataAttribute extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDirectoryResponsePDUDataAttribute> value;
    public CmsGetDataDirectoryResponsePDUDataAttribute() {}
    public CmsGetDataDirectoryResponsePDUDataAttribute(java.util.List<CmsAnonymousGetDataDirectoryResponsePDUDataAttribute> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDirectoryResponsePDUDataAttribute", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDirectoryResponsePDUDataAttribute decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataDirectoryResponsePDUDataAttribute", enc, data);
            CmsGetDataDirectoryResponsePDUDataAttribute r = new CmsGetDataDirectoryResponsePDUDataAttribute();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetDataDirectoryResponsePDUDataAttribute>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
