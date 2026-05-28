#include "cmsper/cmsper.h"
#include "gen_cms.h"
#include "cms_apdu.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
    uint8_t buf[1024];
    per_stream_t w, r;

    /* ================================================================
     *  案例 1: Associate_Request APDU (APCH + ASDU)
     * ================================================================ */
    printf("=== 编码 Associate_Request APDU ===\n");

    /* 1. 编码 ASDU */
    per_stream_init_write(&w, buf, sizeof(buf));
    Associate_Request assoc;
    memset(&assoc, 0, sizeof(assoc));
    assoc.reqId = 1;
    assoc.serverAccessPointReference = "cmsServer";
    assoc._has_authenticationParameter = 0;
    encode_Associate_Request(&w, &assoc);
    size_t asdu_len = per_stream_bytes_written(&w);

    /* 2. 封装 APDU (APCH + ASDU) */
    uint8_t apdu_buf[1024];
    size_t apdu_len = 0;
    cms_apdu_encode(apdu_buf, sizeof(apdu_buf), &apdu_len,
                    false, false, CMS_SVC_ASSOCIATE,
                    buf, asdu_len);

    printf("  ASDU: %zu 字节\n", asdu_len);
    printf("  APDU: %zu 字节 (APCH 4B + ASDU)\n", apdu_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%zu\n",
           apdu_buf[0], apdu_buf[1], (size_t)((apdu_buf[2] << 8) | apdu_buf[3]));

    /* 3. 解码 APDU */
    cms_apch_t apch;
    const uint8_t *asdu_data;
    size_t asdu_data_len;
    cms_apdu_decode(apdu_buf, apdu_len, &apch, &asdu_data, &asdu_data_len);

    printf("  解码 APCH: CC=0x%02X SC=0x%02X FL=%u\n",
           apch.cc, apch.sc, apch.fl);
    printf("  解码 ASDU: %zu 字节\n", asdu_data_len);

    /* 4. 解码 ASDU */
    Associate_Request assoc_dec;
    memset(&assoc_dec, 0, sizeof(assoc_dec));
    per_stream_init_read(&r, asdu_data, asdu_data_len);
    decode_Associate_Request(&r, &assoc_dec);

    printf("  解码 Associate_Request:\n");
    printf("    reqId: %lld\n", (long long)assoc_dec.reqId);
    printf("    serverAccessPointReference: %s\n", assoc_dec.serverAccessPointReference);

    int ok1 = 1;
    if (assoc.reqId != assoc_dec.reqId) ok1 = 0;
    if (strcmp(assoc.serverAccessPointReference, assoc_dec.serverAccessPointReference) != 0) ok1 = 0;
    if (apch.sc != CMS_SVC_ASSOCIATE) ok1 = 0;
    if (cms_cc_is_resp(apch.cc) || cms_cc_is_err(apch.cc)) ok1 = 0;

    free(assoc_dec.serverAccessPointReference);
    printf("  --> %s\n\n", ok1 ? "PASS" : "FAIL");

    /* ================================================================
     *  案例 2: Release_Request APDU
     * ================================================================ */
    printf("=== 编码 Release_Request APDU ===\n");

    per_stream_init_write(&w, buf, sizeof(buf));
    Release_Request rel;
    memset(&rel, 0, sizeof(rel));
    rel.reqId = 2;
    encode_Release_Request(&w, &rel);
    asdu_len = per_stream_bytes_written(&w);

    cms_apdu_encode(apdu_buf, sizeof(apdu_buf), &apdu_len,
                    false, false, CMS_SVC_RELEASE,
                    buf, asdu_len);

    printf("  APDU: %zu 字节\n", apdu_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%zu\n",
           apdu_buf[0], apdu_buf[1], (size_t)((apdu_buf[2] << 8) | apdu_buf[3]));

    cms_apdu_decode(apdu_buf, apdu_len, &apch, &asdu_data, &asdu_data_len);

    Release_Request rel_dec;
    memset(&rel_dec, 0, sizeof(rel_dec));
    per_stream_init_read(&r, asdu_data, asdu_data_len);
    decode_Release_Request(&r, &rel_dec);

    int ok2 = 1;
    if (rel.reqId != rel_dec.reqId) ok2 = 0;
    if (apch.sc != CMS_SVC_RELEASE) ok2 = 0;
    printf("  reqId: %lld  --> %s\n\n", (long long)rel_dec.reqId, ok2 ? "PASS" : "FAIL");

    /* ================================================================
     *  案例 3: Abort APDU (含枚举字段)
     * ================================================================ */
    printf("=== 编码 Abort APDU ===\n");

    per_stream_init_write(&w, buf, sizeof(buf));
    Abort abrt;
    memset(&abrt, 0, sizeof(abrt));
    abrt.reqId = 3;
    abrt.abortReason = AbortReason_noReason;
    encode_Abort(&w, &abrt);
    asdu_len = per_stream_bytes_written(&w);

    cms_apdu_encode(apdu_buf, sizeof(apdu_buf), &apdu_len,
                    false, false, CMS_SVC_ABORT,
                    buf, asdu_len);

    printf("  APDU: %zu 字节\n", apdu_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%zu\n",
           apdu_buf[0], apdu_buf[1], (size_t)((apdu_buf[2] << 8) | apdu_buf[3]));

    cms_apdu_decode(apdu_buf, apdu_len, &apch, &asdu_data, &asdu_data_len);

    Abort abrt_dec;
    memset(&abrt_dec, 0, sizeof(abrt_dec));
    per_stream_init_read(&r, asdu_data, asdu_data_len);
    decode_Abort(&r, &abrt_dec);

    int ok3 = 1;
    if (abrt.reqId != abrt_dec.reqId) ok3 = 0;
    if (abrt.abortReason != abrt_dec.abortReason) ok3 = 0;
    if (apch.sc != CMS_SVC_ABORT) ok3 = 0;
    printf("  reqId: %lld  abortReason: %lld  --> %s\n\n",
           (long long)abrt_dec.reqId, (long long)abrt_dec.abortReason, ok3 ? "PASS" : "FAIL");

    /* ================================================================
     *  汇总
     * ================================================================ */
    int all_ok = ok1 && ok2 && ok3;
    printf("=== 汇总 ===\n");
    printf("  Associate_Request APDU : %s\n", ok1 ? "PASS" : "FAIL");
    printf("  Release_Request APDU   : %s\n", ok2 ? "PASS" : "FAIL");
    printf("  Abort APDU             : %s\n", ok3 ? "PASS" : "FAIL");
    printf("\n%s\n", all_ok ? "ALL PASS" : "SOME FAILED");
    return all_ok ? 0 : 1;
}
