// Auto-generated. ASN.1 type: SetGoCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetGoCBValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetGoCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetGoCBValuesResponsePDU() {}
    public CmsSetGoCBValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetGoCBValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetGoCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetGoCBValuesResponsePDU", enc, data);
            CmsSetGoCBValuesResponsePDU r = new CmsSetGoCBValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
