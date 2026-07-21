// Auto-generated. ASN.1 type: SelectActiveSGErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SelectActiveSG-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsSelectActiveSGErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsSelectActiveSGErrorPDU() {}
    public CmsSelectActiveSGErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SelectActiveSGErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectActiveSGErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SelectActiveSGErrorPDU", enc, data);
            CmsSelectActiveSGErrorPDU r = new CmsSelectActiveSGErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
