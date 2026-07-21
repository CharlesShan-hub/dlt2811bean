// Auto-generated. ASN.1 type: GetAllDataDefinitionErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllDataDefinition-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetAllDataDefinitionErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetAllDataDefinitionErrorPDU() {}
    public CmsGetAllDataDefinitionErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetAllDataDefinitionErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataDefinitionErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetAllDataDefinitionErrorPDU", enc, data);
            CmsGetAllDataDefinitionErrorPDU r = new CmsGetAllDataDefinitionErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
