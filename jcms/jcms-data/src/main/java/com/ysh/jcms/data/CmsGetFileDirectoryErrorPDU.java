// Auto-generated. ASN.1 type: GetFileDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFileDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetFileDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetFileDirectoryErrorPDU() {}
    public CmsGetFileDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetFileDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetFileDirectoryErrorPDU", enc, data);
            CmsGetFileDirectoryErrorPDU r = new CmsGetFileDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
