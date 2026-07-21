// Auto-generated. ASN.1 type: SetMSVCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetMSVCBValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetMSVCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetMSVCBValuesResponsePDU() {}
    public CmsSetMSVCBValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetMSVCBValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetMSVCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetMSVCBValuesResponsePDU", enc, data);
            CmsSetMSVCBValuesResponsePDU r = new CmsSetMSVCBValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
