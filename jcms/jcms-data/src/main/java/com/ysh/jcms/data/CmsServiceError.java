// Auto-generated. ASN.1 type: ServiceError

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ServiceError ::= INTEGER {
 *     no-error                                      (0),
 *     instance-not-available                        (1),
 *     instance-in-use                               (2),
 *     access-violation                              (3),
 *     access-not-allowed-in-current-state            (4),
 *     parameter-value-inappropriate                 (5),
 *     parameter-value-inconsistent                  (6),
 *     class-not-supported                           (7),
 *     instance-locked-by-other-client               (8),
 *     control-must-be-selected                      (9),
 *     type-conflict                                 (10),
 *     failed-due-to-communications-constraint       (11),
 *     failed-due-to-server-constraint               (12)
 * } (0..12)
 * }</pre>
 */
@Data
public class CmsServiceError extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsServiceError() {}
    public CmsServiceError(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ServiceError", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsServiceError decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ServiceError", enc, data);
            CmsServiceError r = new CmsServiceError();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
