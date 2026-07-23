// Auto-generated. ASN.1 type: EntryID

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * EntryID ::= OCTET STRING (SIZE(8))
 * }</pre>
 */
@Data
public class CmsEntryID extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsEntryID() {}
    public CmsEntryID(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("EntryID", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsEntryID decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("EntryID", enc, data);
            CmsEntryID r = new CmsEntryID();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
