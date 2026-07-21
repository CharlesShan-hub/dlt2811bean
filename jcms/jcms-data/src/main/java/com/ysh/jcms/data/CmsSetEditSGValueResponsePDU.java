// Auto-generated. ASN.1 type: SetEditSGValueResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetEditSGValue-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetEditSGValueResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetEditSGValueResponsePDU() {}
    public CmsSetEditSGValueResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetEditSGValueResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetEditSGValueResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetEditSGValueResponsePDU", enc, data);
            CmsSetEditSGValueResponsePDU r = new CmsSetEditSGValueResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
