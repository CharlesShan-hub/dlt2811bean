// Auto-generated. ASN.1 type: SetFileErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetFile-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsSetFileErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsSetFileErrorPDU() {}
    public CmsSetFileErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SetFileErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetFileErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetFileErrorPDU", enc, data);
            CmsSetFileErrorPDU r = new CmsSetFileErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
