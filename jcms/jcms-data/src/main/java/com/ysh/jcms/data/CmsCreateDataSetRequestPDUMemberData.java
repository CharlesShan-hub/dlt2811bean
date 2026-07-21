// Auto-generated. ASN.1 type: CreateDataSetRequestPDUMemberData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsCreateDataSetRequestPDUMemberData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousCreateDataSetRequestPDUMemberData> value;
    public CmsCreateDataSetRequestPDUMemberData() {}
    public CmsCreateDataSetRequestPDUMemberData(java.util.List<CmsAnonymousCreateDataSetRequestPDUMemberData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("CreateDataSetRequestPDUMemberData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCreateDataSetRequestPDUMemberData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("CreateDataSetRequestPDUMemberData", enc, data);
            CmsCreateDataSetRequestPDUMemberData r = new CmsCreateDataSetRequestPDUMemberData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousCreateDataSetRequestPDUMemberData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
