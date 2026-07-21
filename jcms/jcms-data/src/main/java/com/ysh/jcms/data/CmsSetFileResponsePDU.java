// Auto-generated. ASN.1 type: SetFileResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetFile-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsSetFileResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsSetFileResponsePDU() {}
    public CmsSetFileResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetFileResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetFileResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetFileResponsePDU", enc, data);
            CmsSetFileResponsePDU r = new CmsSetFileResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
