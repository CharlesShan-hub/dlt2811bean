// Auto-generated. ASN.1 type: LcbOptFlds

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * LcbOptFlds ::= BIT STRING (SIZE(1))
 * }</pre>
 */
@Data
public class CmsLcbOptFlds extends CmsBase {
    public static final int REFRESH_TIME = 0;
    public static final int RESERVED = 1;
    public static final int SAMPLE_RATE = 2;
    public static final int DATA_SET_NAME = 3;
    public static final int SECURITY = 4;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsLcbOptFlds() {}
    public CmsLcbOptFlds(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("LcbOptFlds", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsLcbOptFlds decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("LcbOptFlds", enc, data);
            CmsLcbOptFlds r = new CmsLcbOptFlds();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
