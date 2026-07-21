// Auto-generated. ASN.1 type: DeleteFileResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteFile-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsDeleteFileResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsDeleteFileResponsePDU() {}
    public CmsDeleteFileResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("DeleteFileResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteFileResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("DeleteFileResponsePDU", enc, data);
            CmsDeleteFileResponsePDU r = new CmsDeleteFileResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
