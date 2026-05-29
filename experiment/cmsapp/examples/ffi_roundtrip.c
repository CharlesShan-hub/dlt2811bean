#include "cms_services.h"
#include "cms_apdu.h"
#include <stdio.h>
#include <string.h>

int main() {
    uint8_t buf[1024];
    int out_len;

    printf("=== FFI Associate-Request ===\n");
    out_len = (int)sizeof(buf);
    int ret = cms_encode_associate_request(
        1001, "cmsServer", 0,
        buf, &out_len
    );
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);

    int64_t req_id;
    char sap_ref[256];
    int sap_ref_cap = sizeof(sap_ref);
    int has_auth;
    ret = cms_decode_associate_request(
        buf, out_len,
        &req_id, sap_ref, &sap_ref_cap, &has_auth
    );
    printf("  解码: ret=%d  reqId=%lld  sapRef=%s  hasAuth=%d\n",
           ret, (long long)req_id, sap_ref, has_auth);
    int ok1 = (ret == 0 && req_id == 1001 &&
               strcmp(sap_ref, "cmsServer") == 0 && has_auth == 0);
    printf("  --> %s\n\n", ok1 ? "PASS" : "FAIL");

    printf("=== FFI Release-Request ===\n");
    out_len = (int)sizeof(buf);
    ret = cms_encode_release_request(2001, buf, &out_len);
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);
    ret = cms_decode_release_request(buf, out_len, &req_id);
    printf("  解码: ret=%d  reqId=%lld\n", ret, (long long)req_id);
    int ok2 = (ret == 0 && req_id == 2001);
    printf("  --> %s\n\n", ok2 ? "PASS" : "FAIL");

    printf("=== FFI Abort ===\n");
    out_len = (int)sizeof(buf);
    ret = cms_encode_abort(3001, 2, buf, &out_len);
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);
    int64_t abort_reason;
    ret = cms_decode_abort(buf, out_len, &req_id, &abort_reason);
    printf("  解码: ret=%d  reqId=%lld  abortReason=%lld\n",
           ret, (long long)req_id, (long long)abort_reason);
    int ok3 = (ret == 0 && req_id == 3001 && abort_reason == 2);
    printf("  --> %s\n\n", ok3 ? "PASS" : "FAIL");

    int all_ok = ok1 && ok2 && ok3;
    printf("=== 汇总 ===\n");
    printf("  Associate-Request : %s\n", ok1 ? "PASS" : "FAIL");
    printf("  Release-Request   : %s\n", ok2 ? "PASS" : "FAIL");
    printf("  Abort             : %s\n", ok3 ? "PASS" : "FAIL");
    printf("\n%s\n", all_ok ? "ALL PASS" : "SOME FAILED");
    return all_ok ? 0 : 1;
}
