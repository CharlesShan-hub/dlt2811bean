// Auto-generated. ASN.1 type: DeleteFileErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteFile-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsDeleteFileErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsDeleteFileErrorPDU() {}
    public CmsDeleteFileErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("DeleteFileErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteFileErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("DeleteFileErrorPDU", enc, data);
            CmsDeleteFileErrorPDU r = new CmsDeleteFileErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
