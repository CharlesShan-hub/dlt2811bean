// Auto-generated. ASN.1 type: Int16U

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int16U ::= INTEGER (0..65535)
 * }</pre>
 */
@Data
public class CmsInt16U extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt16U() {}
    public CmsInt16U(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int16U", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt16U decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int16U", enc, data);
            CmsInt16U r = new CmsInt16U();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
