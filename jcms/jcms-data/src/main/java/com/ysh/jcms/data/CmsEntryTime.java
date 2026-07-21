// Auto-generated. ASN.1 type: EntryTime

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * EntryTime ::= BinaryTime
 * }</pre>
 */
@Data
public class CmsEntryTime extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsEntryTime() {}
    public CmsEntryTime(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("EntryTime", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsEntryTime decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("EntryTime", enc, data);
            CmsEntryTime r = new CmsEntryTime();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
