// Auto-generated. ASN.1 type: SetGoCBValuesRequestPDUGocb

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetGoCBValuesRequestPDUGocb extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetGoCBValuesRequestPDUGocb> value;
    public CmsSetGoCBValuesRequestPDUGocb() {}
    public CmsSetGoCBValuesRequestPDUGocb(java.util.List<CmsAnonymousSetGoCBValuesRequestPDUGocb> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetGoCBValuesRequestPDUGocb", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetGoCBValuesRequestPDUGocb decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetGoCBValuesRequestPDUGocb", enc, data);
            CmsSetGoCBValuesRequestPDUGocb r = new CmsSetGoCBValuesRequestPDUGocb();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetGoCBValuesRequestPDUGocb>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
