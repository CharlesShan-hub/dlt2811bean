// Auto-generated. ASN.1 type: GetFileErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFile-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetFileErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetFileErrorPDU() {}
    public CmsGetFileErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetFileErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetFileErrorPDU", enc, data);
            CmsGetFileErrorPDU r = new CmsGetFileErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
