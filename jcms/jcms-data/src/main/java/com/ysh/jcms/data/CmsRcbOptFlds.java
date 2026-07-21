// Auto-generated. ASN.1 type: RcbOptFlds

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * RcbOptFlds ::= BIT STRING {
 *     reserved                    (0),
 *     sequence-number             (1),
 *     report-time-stamp           (2),
 *     reason-for-inclusion        (3),
 *     data-set-name               (4),
 *     data-reference              (5),
 *     buffer-overflow             (6),
 *     entryID                     (7),
 *     conf-revision               (8),
 *     segmentation                (9)
 * } (SIZE(10))
 * }</pre>
 */
@Data
public class CmsRcbOptFlds extends CmsBase {
    public static final int RESERVED = 0;
    public static final int SEQUENCE_NUMBER = 1;
    public static final int REPORT_TIME_STAMP = 2;
    public static final int REASON_FOR_INCLUSION = 3;
    public static final int DATA_SET_NAME = 4;
    public static final int DATA_REFERENCE = 5;
    public static final int BUFFER_OVERFLOW = 6;
    public static final int ENTRYID = 7;
    public static final int CONF_REVISION = 8;
    public static final int SEGMENTATION = 9;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsRcbOptFlds() {}
    public CmsRcbOptFlds(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("RcbOptFlds", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsRcbOptFlds decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("RcbOptFlds", enc, data);
            CmsRcbOptFlds r = new CmsRcbOptFlds();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
