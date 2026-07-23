// Auto-generated. ASN.1 type: SetURCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetURCBValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetURCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetURCBValuesResponsePDU() {}
    public CmsSetURCBValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetURCBValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetURCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetURCBValuesResponsePDU", enc, data);
            CmsSetURCBValuesResponsePDU r = new CmsSetURCBValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
