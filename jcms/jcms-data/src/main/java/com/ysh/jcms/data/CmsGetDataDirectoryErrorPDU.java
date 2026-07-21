// Auto-generated. ASN.1 type: GetDataDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetDataDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetDataDirectoryErrorPDU() {}
    public CmsGetDataDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetDataDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataDirectoryErrorPDU", enc, data);
            CmsGetDataDirectoryErrorPDU r = new CmsGetDataDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
