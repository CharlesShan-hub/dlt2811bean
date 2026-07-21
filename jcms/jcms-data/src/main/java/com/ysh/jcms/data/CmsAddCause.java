// Auto-generated. ASN.1 type: AddCause

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * AddCause ::= INTEGER {
 *     unknown                              (0),
 *     not-supported                        (1),
 *     blocked-by-switching-hierarchy       (2),
 *     select-failed                        (3),
 *     invalid-position                     (4),
 *     position-reached                     (5),
 *     parameter-change-in-execution        (6),
 *     step-limit                           (7),
 *     blocked-by-mode                      (8),
 *     blocked-by-process                   (9),
 *     blocked-by-interlocking              (10),
 *     blocked-by-syncheck                  (11),
 *     command-already-in-execution         (12),
 *     blocked-by-health                    (13),
 *     one-of-a-control                     (14),
 *     abortion-by-cancel                   (15),
 *     time-limit-over                      (16),
 *     abortion-by-trip                     (17),
 *     object-not-selected                  (18),
 *     object-already-selected              (19),
 *     no-access-authority                  (20),
 *     ended-with-overshoot                 (21),
 *     abortion-due-to-deviation            (22),
 *     abortion-by-communication-loss       (23),
 *     blocked-by-command                   (24),
 *     none                                 (25),
 *     locked-by-other-client               (26),
 *     inconsistent-parameters              (27)
 * } (0..27)
 * }</pre>
 */
@Data
public class CmsAddCause extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsAddCause() {}
    public CmsAddCause(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("AddCause", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAddCause decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("AddCause", enc, data);
            CmsAddCause r = new CmsAddCause();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
