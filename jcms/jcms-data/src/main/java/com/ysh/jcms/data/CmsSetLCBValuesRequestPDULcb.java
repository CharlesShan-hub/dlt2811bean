// Auto-generated. ASN.1 type: SetLCBValuesRequestPDULcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetLCBValuesRequestPDULcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetLCBValuesRequestPDULcb> value;
    public CmsSetLCBValuesRequestPDULcb() {}
    public CmsSetLCBValuesRequestPDULcb(java.util.List<CmsAnonymousSetLCBValuesRequestPDULcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetLCBValuesRequestPDULcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetLCBValuesRequestPDULcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetLCBValuesRequestPDULcb", enc, data);
            CmsSetLCBValuesRequestPDULcb r = new CmsSetLCBValuesRequestPDULcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetLCBValuesRequestPDULcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
