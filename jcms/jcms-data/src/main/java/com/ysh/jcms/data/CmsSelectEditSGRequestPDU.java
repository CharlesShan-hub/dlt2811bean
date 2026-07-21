// Auto-generated. ASN.1 type: SelectEditSGRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SelectEditSG-RequestPDU ::= SEQUENCE {
 *     sgcbReference       [0] IMPLICIT ObjectReference,
 *     settingGroupNumber  [1] IMPLICIT Int8U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSelectEditSGRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String sgcb_reference = null;
    @JsonProperty public int setting_group_number = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SelectEditSGRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectEditSGRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SelectEditSGRequestPDU", enc, data), CmsSelectEditSGRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
