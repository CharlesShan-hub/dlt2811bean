// Auto-generated. ASN.1 type: SetBRCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetBRCBValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetBRCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetBRCBValuesResponsePDU() {}
    public CmsSetBRCBValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetBRCBValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetBRCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetBRCBValuesResponsePDU", enc, data);
            CmsSetBRCBValuesResponsePDU r = new CmsSetBRCBValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
