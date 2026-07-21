// Auto-generated. ASN.1 type: TriggerConditions

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * TriggerConditions ::= BIT STRING {
 *     reserved                    (0),
 *     data-change                 (1),
 *     quality-change              (2),
 *     data-update                 (3),
 *     integrity                   (4),
 *     general-interrogation       (5)
 * } (SIZE(6))
 * }</pre>
 */
@Data
public class CmsTriggerConditions extends CmsBase {
    public static final int RESERVED = 0;
    public static final int DATA_CHANGE = 1;
    public static final int QUALITY_CHANGE = 2;
    public static final int DATA_UPDATE = 3;
    public static final int INTEGRITY = 4;
    public static final int GENERAL_INTERROGATION = 5;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsTriggerConditions() {}
    public CmsTriggerConditions(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("TriggerConditions", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsTriggerConditions decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("TriggerConditions", enc, data);
            CmsTriggerConditions r = new CmsTriggerConditions();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
