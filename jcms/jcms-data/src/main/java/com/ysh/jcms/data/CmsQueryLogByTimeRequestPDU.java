// Auto-generated. ASN.1 type: QueryLogByTimeRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * QueryLogByTime-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     stopTime        [2] IMPLICIT EntryTime OPTIONAL,
 *     entryAfter      [3] IMPLICIT EntryID OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsQueryLogByTimeRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String log_reference = null;
    @JsonProperty public byte[] start_time = null;
    @JsonProperty public byte[] stop_time = null;
    @JsonProperty public byte[] entry_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("QueryLogByTimeRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQueryLogByTimeRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("QueryLogByTimeRequestPDU", enc, data), CmsQueryLogByTimeRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
