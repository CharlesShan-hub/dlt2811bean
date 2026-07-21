// Auto-generated. ASN.1 type: ConfirmEditSGValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ConfirmEditSGValues-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsConfirmEditSGValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsConfirmEditSGValuesResponsePDU() {}
    public CmsConfirmEditSGValuesResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ConfirmEditSGValuesResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsConfirmEditSGValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ConfirmEditSGValuesResponsePDU", enc, data);
            CmsConfirmEditSGValuesResponsePDU r = new CmsConfirmEditSGValuesResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
