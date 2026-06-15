package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsRpcTest {
    @Test
    public void get_rpc_interface_dir_request() {
        CmsGetRpcInterfaceDirectoryRequest a = new CmsGetRpcInterfaceDirectoryRequest();
        a.reqId.value(1);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryRequest b = new CmsGetRpcInterfaceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_rpc_interface_dir_response() {
        CmsGetRpcInterfaceDirectoryResponse a = new CmsGetRpcInterfaceDirectoryResponse();
        a.reqId.value(10);
        CmsSubReference r1 = new CmsSubReference("int1".getBytes());
        CmsSubReference r2 = new CmsSubReference("int2".getBytes());
        a.reference.add(r1).add(r2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryResponse b = new CmsGetRpcInterfaceDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_rpc_method_dir_request() {
        CmsGetRpcMethodDirectoryRequest a = new CmsGetRpcMethodDirectoryRequest();
        a.reqId.value(20);
        a.interfacePresent.value(false);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDirectoryRequest b = new CmsGetRpcMethodDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_rpc_method_dir_response() {
        CmsGetRpcMethodDirectoryResponse a = new CmsGetRpcMethodDirectoryResponse();
        a.reqId.value(21);
        CmsSubReference r1 = new CmsSubReference("m1".getBytes());
        CmsSubReference r2 = new CmsSubReference("m2".getBytes());
        a.reference.add(r1).add(r2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDirectoryResponse b = new CmsGetRpcMethodDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_rpc_if_def_request() {
        CmsGetRpcInterfaceDefinitionRequest a = new CmsGetRpcInterfaceDefinitionRequest();
        a.reqId.value(30);
        a.interfaceName.value("ifDef".getBytes());
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDefinitionRequest b = new CmsGetRpcInterfaceDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_rpc_method_def_request() {
        CmsGetRpcMethodDefinitionRequest a = new CmsGetRpcMethodDefinitionRequest();
        CmsSubReference r1 = new CmsSubReference("md1".getBytes());
        a.reference.add(r1);
        a.reqId.value(40);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDefinitionRequest b = new CmsGetRpcMethodDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void rpc_call_request() {
        CmsRpcCallRequest a = new CmsRpcCallRequest();
        a.reqId.value(50);
        a.method.value("myMethod".getBytes());
        a.req.choice.value(CmsRpcCallReqChoice.REQ_DATA);
        a.req.altReqData.choice.value(CmsData.CHOICE_BOOLEAN);
        a.req.altReqData.alt_boolean.value(true);
        byte[] encoded = a.encode();

        CmsRpcCallRequest b = new CmsRpcCallRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void rpc_call_response() {
        CmsRpcCallResponse a = new CmsRpcCallResponse();
        a.reqId.value(60);
        a.rspData.choice.value(CmsData.CHOICE_INT32);
        a.rspData.alt_int32.value(12345);
        a.nextCallIdPresent.value(false);
        byte[] encoded = a.encode();

        CmsRpcCallResponse b = new CmsRpcCallResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetRpcInterfaceDirectoryError a = new CmsGetRpcInterfaceDirectoryError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryError b = new CmsGetRpcInterfaceDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
