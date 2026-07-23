// Auto-generated. ASN.1 type: DeleteDataSetRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteDataSet-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsDeleteDataSetRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String dataset_reference = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("DeleteDataSetRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteDataSetRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("DeleteDataSetRequestPDU", enc, data), CmsDeleteDataSetRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
