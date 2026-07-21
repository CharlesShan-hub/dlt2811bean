// Auto-generated. ASN.1 type: GetLogicalNodeDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogicalNodeDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetLogicalNodeDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetLogicalNodeDirectoryErrorPDU() {}
    public CmsGetLogicalNodeDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetLogicalNodeDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogicalNodeDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLogicalNodeDirectoryErrorPDU", enc, data);
            CmsGetLogicalNodeDirectoryErrorPDU r = new CmsGetLogicalNodeDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
