// Auto-generated. ASN.1 type: QueryLogByTimeResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * QueryLogByTime-ResponsePDU ::= SEQUENCE {
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsQueryLogByTimeResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsLogEntry> log_entry = new java.util.ArrayList<>();
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("QueryLogByTimeResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQueryLogByTimeResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("QueryLogByTimeResponsePDU", enc, data), CmsQueryLogByTimeResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
