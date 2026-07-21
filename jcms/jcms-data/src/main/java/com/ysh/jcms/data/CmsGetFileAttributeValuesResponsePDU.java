// Auto-generated. ASN.1 type: GetFileAttributeValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFileAttributeValues-ResponsePDU ::= FileEntry
 * }</pre>
 */
@Data
public class CmsGetFileAttributeValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsFileEntry value;
    public CmsGetFileAttributeValuesResponsePDU() {}
    public CmsGetFileAttributeValuesResponsePDU(CmsFileEntry value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetFileAttributeValuesResponsePDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileAttributeValuesResponsePDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetFileAttributeValuesResponsePDU", enc, data);
            CmsGetFileAttributeValuesResponsePDU r = new CmsGetFileAttributeValuesResponsePDU();
            r.value = MAPPER.readValue(json.trim(), CmsFileEntry.class);
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
