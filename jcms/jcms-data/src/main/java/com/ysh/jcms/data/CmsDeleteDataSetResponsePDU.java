// Auto-generated. ASN.1 type: DeleteDataSetResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteDataSet-ResponsePDU ::= NULL
 * }</pre>
 */
@Data
public class CmsDeleteDataSetResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public Object value;
    public CmsDeleteDataSetResponsePDU() {}
    public CmsDeleteDataSetResponsePDU(Object value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("DeleteDataSetResponsePDU", enc, "");
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteDataSetResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("DeleteDataSetResponsePDU", enc, data);
            CmsDeleteDataSetResponsePDU r = new CmsDeleteDataSetResponsePDU();
            r.value = null;
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
