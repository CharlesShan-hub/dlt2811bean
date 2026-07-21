// Auto-generated. ASN.1 type: GetGoReferenceResponsePDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetGoReferenceResponsePDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetGoReferenceResponsePDUMemberData> value;
    public CmsGetGoReferenceResponsePDUMemberData() {}
    public CmsGetGoReferenceResponsePDUMemberData(java.util.List<CmsAnonymousGetGoReferenceResponsePDUMemberData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGoReferenceResponsePDUMemberData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoReferenceResponsePDUMemberData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGoReferenceResponsePDUMemberData", enc, data);
            CmsGetGoReferenceResponsePDUMemberData r = new CmsGetGoReferenceResponsePDUMemberData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetGoReferenceResponsePDUMemberData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
