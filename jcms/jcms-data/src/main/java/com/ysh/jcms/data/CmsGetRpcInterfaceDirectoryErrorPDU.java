// Auto-generated. ASN.1 type: GetRpcInterfaceDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetRpcInterfaceDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetRpcInterfaceDirectoryErrorPDU() {}
    public CmsGetRpcInterfaceDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetRpcInterfaceDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetRpcInterfaceDirectoryErrorPDU", enc, data);
            CmsGetRpcInterfaceDirectoryErrorPDU r = new CmsGetRpcInterfaceDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
