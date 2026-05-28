#include "cmsper/cmsper.h"
#include "gen_cms.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
    uint8_t buf[1024];
    per_stream_t w, r;

    /* ================================================================
     *  案例 1: Associate_Request (关联请求，含 OPTIONAL 字段)
     * ================================================================ */
    printf("=== 编码 Associate_Request ===\n");
    per_stream_init_write(&w, buf, sizeof(buf));

    Associate_Request assoc;
    memset(&assoc, 0, sizeof(assoc));
    assoc.reqId = 1;
    assoc.serverAccessPointReference = "cmsServer";
    assoc._has_authenticationParameter = 0;

    printf("reqId:                      %lld\n", (long long)assoc.reqId);
    printf("serverAccessPointReference: %s\n", assoc.serverAccessPointReference);
    printf("authenticationParameter:    (absent)\n");

    encode_Associate_Request(&w, &assoc);
    printf("编码后 %zu 字节\n\n", per_stream_bytes_written(&w));

    Associate_Request assoc_dec;
    memset(&assoc_dec, 0, sizeof(assoc_dec));
    per_stream_init_read(&r, buf, sizeof(buf));

    printf("=== 解码 Associate_Request ===\n");
    decode_Associate_Request(&r, &assoc_dec);

    printf("reqId:                      %lld\n", (long long)assoc_dec.reqId);
    printf("serverAccessPointReference: %s\n", assoc_dec.serverAccessPointReference);
    printf("authenticationParameter:    %s\n", assoc_dec._has_authenticationParameter ? "present" : "absent");

    int ok1 = 1;
    if (assoc.reqId != assoc_dec.reqId) ok1 = 0;
    if (strcmp(assoc.serverAccessPointReference, assoc_dec.serverAccessPointReference) != 0) ok1 = 0;
    if (assoc._has_authenticationParameter != assoc_dec._has_authenticationParameter) ok1 = 0;

    free(assoc_dec.serverAccessPointReference);
    printf("  --> %s\n\n", ok1 ? "PASS" : "FAIL");

    /* ================================================================
     *  案例 2: Release_Request (释放请求，只有 reqId)
     * ================================================================ */
    printf("=== 编码 Release_Request ===\n");
    per_stream_init_write(&w, buf, sizeof(buf));

    Release_Request rel;
    memset(&rel, 0, sizeof(rel));
    rel.reqId = 2;

    printf("reqId: %lld\n", (long long)rel.reqId);
    encode_Release_Request(&w, &rel);
    printf("编码后 %zu 字节\n\n", per_stream_bytes_written(&w));

    Release_Request rel_dec;
    memset(&rel_dec, 0, sizeof(rel_dec));
    per_stream_init_read(&r, buf, sizeof(buf));

    printf("=== 解码 Release_Request ===\n");
    decode_Release_Request(&r, &rel_dec);
    printf("reqId: %lld\n", (long long)rel_dec.reqId);

    int ok2 = 1;
    if (rel.reqId != rel_dec.reqId) ok2 = 0;
    printf("  --> %s\n\n", ok2 ? "PASS" : "FAIL");

    /* ================================================================
     *  案例 3: Abort (中止服务，含枚举字段)
     * ================================================================ */
    printf("=== 编码 Abort ===\n");
    per_stream_init_write(&w, buf, sizeof(buf));

    Abort abrt;
    memset(&abrt, 0, sizeof(abrt));
    abrt.reqId = 3;
    abrt.abortReason = AbortReason_noReason;

    printf("reqId:       %lld\n", (long long)abrt.reqId);
    printf("abortReason: %lld\n", (long long)abrt.abortReason);

    encode_Abort(&w, &abrt);
    printf("编码后 %zu 字节\n\n", per_stream_bytes_written(&w));

    Abort abrt_dec;
    memset(&abrt_dec, 0, sizeof(abrt_dec));
    per_stream_init_read(&r, buf, sizeof(buf));

    printf("=== 解码 Abort ===\n");
    decode_Abort(&r, &abrt_dec);

    printf("reqId:       %lld\n", (long long)abrt_dec.reqId);
    printf("abortReason: %lld\n", (long long)abrt_dec.abortReason);

    int ok3 = 1;
    if (abrt.reqId != abrt_dec.reqId) ok3 = 0;
    if (abrt.abortReason != abrt_dec.abortReason) ok3 = 0;
    printf("  --> %s\n\n", ok3 ? "PASS" : "FAIL");

    /* ================================================================
     *  汇总结果
     * ================================================================ */
    int all_ok = ok1 && ok2 && ok3;
    printf("=== 汇总 ===\n");
    printf("  Associate_Request : %s\n", ok1 ? "PASS" : "FAIL");
    printf("  Release_Request   : %s\n", ok2 ? "PASS" : "FAIL");
    printf("  Abort             : %s\n", ok3 ? "PASS" : "FAIL");
    printf("\n%s\n", all_ok ? "ALL PASS" : "SOME FAILED");
    return all_ok ? 0 : 1;
}
