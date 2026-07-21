// Auto-generated. ASN.1 type: GetDataDefinitionErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDefinition-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetDataDefinitionErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetDataDefinitionErrorPDU() {}
    public CmsGetDataDefinitionErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetDataDefinitionErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDefinitionErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataDefinitionErrorPDU", enc, data);
            CmsGetDataDefinitionErrorPDU r = new CmsGetDataDefinitionErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
