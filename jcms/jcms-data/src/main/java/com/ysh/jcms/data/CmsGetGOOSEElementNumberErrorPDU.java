// Auto-generated. ASN.1 type: GetGOOSEElementNumberErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGOOSEElementNumber-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetGOOSEElementNumberErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetGOOSEElementNumberErrorPDU() {}
    public CmsGetGOOSEElementNumberErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetGOOSEElementNumberErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGOOSEElementNumberErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGOOSEElementNumberErrorPDU", enc, data);
            CmsGetGOOSEElementNumberErrorPDU r = new CmsGetGOOSEElementNumberErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
