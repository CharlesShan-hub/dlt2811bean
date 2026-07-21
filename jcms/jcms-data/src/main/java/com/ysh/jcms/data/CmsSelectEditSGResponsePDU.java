// Auto-generated. ASN.1 type: SelectEditSGResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SelectEditSG-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSelectEditSGResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSelectEditSGResponsePDU() {}
    public CmsSelectEditSGResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SelectEditSGResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectEditSGResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SelectEditSGResponsePDU", enc, data);
            CmsSelectEditSGResponsePDU r = new CmsSelectEditSGResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
