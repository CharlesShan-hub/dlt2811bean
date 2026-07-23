// Auto-generated. ASN.1 type: GetRpcInterfaceDefinitionResponsePDUMethod

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetRpcInterfaceDefinitionResponsePDUMethod extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod> value;
    public CmsGetRpcInterfaceDefinitionResponsePDUMethod() {}
    public CmsGetRpcInterfaceDefinitionResponsePDUMethod(java.util.List<CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcInterfaceDefinitionResponsePDUMethod", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDefinitionResponsePDUMethod decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetRpcInterfaceDefinitionResponsePDUMethod", enc, data);
            CmsGetRpcInterfaceDefinitionResponsePDUMethod r = new CmsGetRpcInterfaceDefinitionResponsePDUMethod();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
