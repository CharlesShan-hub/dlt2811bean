// Auto-generated. ASN.1 type: GetDataSetDirectoryErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataSetDirectory-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetDataSetDirectoryErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetDataSetDirectoryErrorPDU() {}
    public CmsGetDataSetDirectoryErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetDataSetDirectoryErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataSetDirectoryErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataSetDirectoryErrorPDU", enc, data);
            CmsGetDataSetDirectoryErrorPDU r = new CmsGetDataSetDirectoryErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
