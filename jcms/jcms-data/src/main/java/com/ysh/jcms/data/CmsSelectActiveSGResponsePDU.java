// Auto-generated. ASN.1 type: SelectActiveSGResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SelectActiveSG-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSelectActiveSGResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSelectActiveSGResponsePDU() {}
    public CmsSelectActiveSGResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SelectActiveSGResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectActiveSGResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SelectActiveSGResponsePDU", enc, data);
            CmsSelectActiveSGResponsePDU r = new CmsSelectActiveSGResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
