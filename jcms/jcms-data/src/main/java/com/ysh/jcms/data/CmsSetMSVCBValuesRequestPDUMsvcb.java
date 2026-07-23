// Auto-generated. ASN.1 type: SetMSVCBValuesRequestPDUMsvcb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetMSVCBValuesRequestPDUMsvcb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetMSVCBValuesRequestPDUMsvcb> value;
    public CmsSetMSVCBValuesRequestPDUMsvcb() {}
    public CmsSetMSVCBValuesRequestPDUMsvcb(java.util.List<CmsAnonymousSetMSVCBValuesRequestPDUMsvcb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetMSVCBValuesRequestPDUMsvcb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetMSVCBValuesRequestPDUMsvcb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetMSVCBValuesRequestPDUMsvcb", enc, data);
            CmsSetMSVCBValuesRequestPDUMsvcb r = new CmsSetMSVCBValuesRequestPDUMsvcb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetMSVCBValuesRequestPDUMsvcb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
