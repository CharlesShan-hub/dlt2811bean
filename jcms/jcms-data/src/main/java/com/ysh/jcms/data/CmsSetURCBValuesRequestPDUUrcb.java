// Auto-generated. ASN.1 type: SetURCBValuesRequestPDUUrcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetURCBValuesRequestPDUUrcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetURCBValuesRequestPDUUrcb> value;
    public CmsSetURCBValuesRequestPDUUrcb() {}
    public CmsSetURCBValuesRequestPDUUrcb(java.util.List<CmsAnonymousSetURCBValuesRequestPDUUrcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetURCBValuesRequestPDUUrcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetURCBValuesRequestPDUUrcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetURCBValuesRequestPDUUrcb", enc, data);
            CmsSetURCBValuesRequestPDUUrcb r = new CmsSetURCBValuesRequestPDUUrcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetURCBValuesRequestPDUUrcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
