// Auto-generated. ASN.1 type: GetRpcMethodDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcMethodDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetRpcMethodDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetRpcMethodDirectoryErrorPDU() {}
    public CmsGetRpcMethodDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetRpcMethodDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcMethodDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetRpcMethodDirectoryErrorPDU", enc, data);
            CmsGetRpcMethodDirectoryErrorPDU r = new CmsGetRpcMethodDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
