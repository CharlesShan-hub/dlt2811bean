// Auto-generated. ASN.1 type: GetRpcMethodDefinitionResponsePDUReference

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetRpcMethodDefinitionResponsePDUReference extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetRpcMethodDefinitionResponsePDUReference> value;
    public CmsGetRpcMethodDefinitionResponsePDUReference() {}
    public CmsGetRpcMethodDefinitionResponsePDUReference(java.util.List<CmsAnonymousGetRpcMethodDefinitionResponsePDUReference> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcMethodDefinitionResponsePDUReference", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcMethodDefinitionResponsePDUReference decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetRpcMethodDefinitionResponsePDUReference", enc, data);
            CmsGetRpcMethodDefinitionResponsePDUReference r = new CmsGetRpcMethodDefinitionResponsePDUReference();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetRpcMethodDefinitionResponsePDUReference>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
