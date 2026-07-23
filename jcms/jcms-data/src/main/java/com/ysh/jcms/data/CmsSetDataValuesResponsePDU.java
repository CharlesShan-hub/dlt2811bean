// Auto-generated. ASN.1 type: SetDataValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetDataValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetDataValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetDataValuesResponsePDU() {}
    public CmsSetDataValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetDataValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetDataValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetDataValuesResponsePDU", enc, data);
            CmsSetDataValuesResponsePDU r = new CmsSetDataValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
