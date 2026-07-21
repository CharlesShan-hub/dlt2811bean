// Auto-generated. ASN.1 type: AnonymousLogEntryEntryData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within LogEntry ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousLogEntryEntryData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    @JsonProperty public String fc = null;
    @JsonProperty public CmsData value = null;
    @JsonProperty public int reason = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousLogEntryEntryData", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousLogEntryEntryData decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousLogEntryEntryData", enc, data), CmsAnonymousLogEntryEntryData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
