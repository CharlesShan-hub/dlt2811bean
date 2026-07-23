// Auto-generated. ASN.1 type: SetLCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetLCBValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetLCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetLCBValuesResponsePDU() {}
    public CmsSetLCBValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetLCBValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetLCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetLCBValuesResponsePDU", enc, data);
            CmsSetLCBValuesResponsePDU r = new CmsSetLCBValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
