// Auto-generated. ASN.1 type: SmpMod

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SmpMod ::= INTEGER {
 *     samples-per-nominal-period    (0),
 *     samples-per-second            (1),
 *     seconds-per-sample            (2)
 * } (0..2)
 * }</pre>
 */
@Data
public class CmsSmpMod extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsSmpMod() {}
    public CmsSmpMod(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SmpMod", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSmpMod decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SmpMod", enc, data);
            CmsSmpMod r = new CmsSmpMod();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
