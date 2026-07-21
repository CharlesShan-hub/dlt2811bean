// Auto-generated. ASN.1 type: SetDataSetValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetDataSetValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetDataSetValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetDataSetValuesResponsePDU() {}
    public CmsSetDataSetValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetDataSetValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetDataSetValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetDataSetValuesResponsePDU", enc, data);
            CmsSetDataSetValuesResponsePDU r = new CmsSetDataSetValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
