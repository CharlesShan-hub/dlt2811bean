// Auto-generated. ASN.1 type: GetLogicalDeviceDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetLogicalDeviceDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetLogicalDeviceDirectoryErrorPDU() {}
    public CmsGetLogicalDeviceDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetLogicalDeviceDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogicalDeviceDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLogicalDeviceDirectoryErrorPDU", enc, data);
            CmsGetLogicalDeviceDirectoryErrorPDU r = new CmsGetLogicalDeviceDirectoryErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
