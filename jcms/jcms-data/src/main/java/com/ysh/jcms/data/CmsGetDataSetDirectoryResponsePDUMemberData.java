// Auto-generated. ASN.1 type: GetDataSetDirectoryResponsePDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetDataSetDirectoryResponsePDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataSetDirectoryResponsePDUMemberData> value;
    public CmsGetDataSetDirectoryResponsePDUMemberData() {}
    public CmsGetDataSetDirectoryResponsePDUMemberData(java.util.List<CmsAnonymousGetDataSetDirectoryResponsePDUMemberData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataSetDirectoryResponsePDUMemberData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataSetDirectoryResponsePDUMemberData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataSetDirectoryResponsePDUMemberData", enc, data);
            CmsGetDataSetDirectoryResponsePDUMemberData r = new CmsGetDataSetDirectoryResponsePDUMemberData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetDataSetDirectoryResponsePDUMemberData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
