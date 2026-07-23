// Auto-generated. ASN.1 type: GetGOOSEElementNumberRequestPDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetGOOSEElementNumberRequestPDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData> value;
    public CmsGetGOOSEElementNumberRequestPDUMemberData() {}
    public CmsGetGOOSEElementNumberRequestPDUMemberData(java.util.List<CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGOOSEElementNumberRequestPDUMemberData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGOOSEElementNumberRequestPDUMemberData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGOOSEElementNumberRequestPDUMemberData", enc, data);
            CmsGetGOOSEElementNumberRequestPDUMemberData r = new CmsGetGOOSEElementNumberRequestPDUMemberData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
