// Auto-generated. ASN.1 type: QueryLogAfterRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * QueryLogAfter-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     entry           [2] IMPLICIT EntryID
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsQueryLogAfterRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String log_reference = null;
    @JsonProperty public byte[] start_time = null;
    @JsonProperty public byte[] entry = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("QueryLogAfterRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQueryLogAfterRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("QueryLogAfterRequestPDU", enc, data), CmsQueryLogAfterRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
