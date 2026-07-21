// Auto-generated. ASN.1 type: DeleteDataSetErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteDataSet-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsDeleteDataSetErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsDeleteDataSetErrorPDU() {}
    public CmsDeleteDataSetErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("DeleteDataSetErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteDataSetErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("DeleteDataSetErrorPDU", enc, data);
            CmsDeleteDataSetErrorPDU r = new CmsDeleteDataSetErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
