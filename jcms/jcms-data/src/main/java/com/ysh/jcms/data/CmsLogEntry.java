// Auto-generated. ASN.1 type: LogEntry

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * LogEntry ::= SEQUENCE {
 *     timeOfEntry     [0] IMPLICIT EntryTime,
 *     entryID         [1] IMPLICIT EntryID,
 *     entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         fc          [1] IMPLICIT FunctionalConstraint,
 *         value       [2] IMPLICIT Data,
 *         reason      [3] IMPLICIT ReasonCode
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsLogEntry extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] time_of_entry = null;
    @JsonProperty public byte[] entry_id = null;
    @JsonProperty public java.util.List<CmsAnonymousLogEntryEntryData> entry_data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("LogEntry", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsLogEntry decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("LogEntry", enc, data), CmsLogEntry.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
