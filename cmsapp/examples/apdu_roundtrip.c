#include "cms_services.h"
#include "cms_apdu.h"
#include "cmsper.h"
#include <stdio.h>
#include <string.h>

int main() {
    uint8_t buf[1024];
    int out_len;

    printf("=== APDU Associate-Request ===\n");
    out_len = (int)sizeof(buf);
    int ret = cms_encode_associate_request(1001, "cmsServer", 0, buf, &out_len);
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%d\n",
           buf[0], buf[1], (buf[2] << 8) | buf[3]);

    cms_apch_t apch;
    const uint8_t *asdu;
    size_t asdu_len;
    cms_apdu_decode(buf, (size_t)out_len, &apch, &asdu, &asdu_len);
    printf("  解码 APCH: CC=0x%02X SC=0x%02X FL=%u  ASDU=%zu bytes\n",
           apch.cc, apch.sc, apch.fl, asdu_len);

    int64_t req_id;
    char sap_ref[256];
    int sap_ref_cap = sizeof(sap_ref);
    int has_auth;
    ret = cms_decode_associate_request(buf, out_len, &req_id, sap_ref, &sap_ref_cap, &has_auth);
    printf("  解码: ret=%d  reqId=%lld  sapRef=%s  hasAuth=%d\n",
           ret, (long long)req_id, sap_ref, has_auth);

    int ok1 = (ret == 0 && req_id == 1001 &&
               strcmp(sap_ref, "cmsServer") == 0 && has_auth == 0 &&
               apch.sc == CMS_SVC_ASSOCIATE &&
               !cms_cc_is_resp(apch.cc) && !cms_cc_is_err(apch.cc));
    printf("  --> %s\n\n", ok1 ? "PASS" : "FAIL");

    printf("=== APDU Release-Request ===\n");
    out_len = (int)sizeof(buf);
    ret = cms_encode_release_request(2001, buf, &out_len);
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%d\n",
           buf[0], buf[1], (buf[2] << 8) | buf[3]);

    cms_apdu_decode(buf, (size_t)out_len, &apch, &asdu, &asdu_len);
    ret = cms_decode_release_request(buf, out_len, &req_id);
    int ok2 = (ret == 0 && req_id == 2001 && apch.sc == CMS_SVC_RELEASE);
    printf("  解码: reqId=%lld  --> %s\n\n", (long long)req_id, ok2 ? "PASS" : "FAIL");

    printf("=== APDU Abort ===\n");
    out_len = (int)sizeof(buf);
    ret = cms_encode_abort(3001, 2, buf, &out_len);
    printf("  编码: ret=%d  out_len=%d\n", ret, out_len);
    printf("  APCH: CC=0x%02X SC=0x%02X FL=%d\n",
           buf[0], buf[1], (buf[2] << 8) | buf[3]);

    int64_t abort_reason;
    cms_apdu_decode(buf, (size_t)out_len, &apch, &asdu, &asdu_len);
    ret = cms_decode_abort(buf, out_len, &req_id, &abort_reason);
    int ok3 = (ret == 0 && req_id == 3001 && abort_reason == 2 && apch.sc == CMS_SVC_ABORT);
    printf("  解码: reqId=%lld  abortReason=%lld  --> %s\n\n",
           (long long)req_id, (long long)abort_reason, ok3 ? "PASS" : "FAIL");

    int all_ok = ok1 && ok2 && ok3;
    printf("=== 汇总 ===\n");
    printf("  Associate-Request APDU : %s\n", ok1 ? "PASS" : "FAIL");
    printf("  Release-Request APDU   : %s\n", ok2 ? "PASS" : "FAIL");
    printf("  Abort APDU             : %s\n", ok3 ? "PASS" : "FAIL");
    printf("\n%s\n", all_ok ? "ALL PASS" : "SOME FAILED");
    return all_ok ? 0 : 1;
}
