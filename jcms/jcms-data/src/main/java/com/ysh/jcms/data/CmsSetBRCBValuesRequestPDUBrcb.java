// Auto-generated. ASN.1 type: SetBRCBValuesRequestPDUBrcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetBRCBValuesRequestPDUBrcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetBRCBValuesRequestPDUBrcb> value;
    public CmsSetBRCBValuesRequestPDUBrcb() {}
    public CmsSetBRCBValuesRequestPDUBrcb(java.util.List<CmsAnonymousSetBRCBValuesRequestPDUBrcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetBRCBValuesRequestPDUBrcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetBRCBValuesRequestPDUBrcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetBRCBValuesRequestPDUBrcb", enc, data);
            CmsSetBRCBValuesRequestPDUBrcb r = new CmsSetBRCBValuesRequestPDUBrcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetBRCBValuesRequestPDUBrcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
