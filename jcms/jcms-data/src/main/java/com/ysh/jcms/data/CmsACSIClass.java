// Auto-generated. ASN.1 type: ACSIClass

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ACSIClass ::= INTEGER {
 *     reserved       (0),
 *     data-object    (1),
 *     data-set       (2),
 *     brcb           (3),
 *     urcb           (4),
 *     lcb            (5),
 *     log            (6),
 *     sgcb          (7),
 *     gocb           (8),
 *     msvcb          (10)
 * } (0..10)
 * }</pre>
 */
@Data
public class CmsACSIClass extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsACSIClass() {}
    public CmsACSIClass(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ACSIClass", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsACSIClass decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ACSIClass", enc, data);
            CmsACSIClass r = new CmsACSIClass();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
