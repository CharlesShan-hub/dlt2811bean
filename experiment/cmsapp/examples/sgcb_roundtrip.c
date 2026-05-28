#include "cmsper/cmsper.h"
#include "gen_dlt2811b_datatypes.h"
#include <stdio.h>
#include <string.h>

int main() {
    uint8_t buf[256];
    per_stream_t w, r;

    /* ========== 编码 ========== */
    per_stream_init_write(&w, buf, sizeof(buf));

    SGCB cb;
    memset(&cb, 0, sizeof(cb));
    cb.sgcbName   = "LD1/LLN0.SG";
    cb.sgcbRef    = "LD1/LLN0.SG.sgcb";
    cb.numOfSG    = 4;
    cb.actSG      = 1;
    cb.editSG     = 0;
    cb.cnfEdit    = 0;
    cb.lActTm.secondsSinceEpoch = 1700000000;
    cb.lActTm._has_fractional   = 0;
    cb._has_resvTms = 0;

    printf("=== 编码 SGCB ===\n");
    encode_SGCB(&w, &cb);
    printf("编码后 %zu 字节\n\n", per_stream_bytes_written(&w));

    /* ========== 解码 ========== */
    SGCB result;
    memset(&result, 0, sizeof(result));
    per_stream_init_read(&r, buf, sizeof(buf));

    printf("=== 解码 SGCB ===\n");
    decode_SGCB(&r, &result);

    printf("sgcbName:         %s\n", result.sgcbName);
    printf("sgcbRef:          %s\n", result.sgcbRef);
    printf("numOfSG:          %lld\n", (long long)result.numOfSG);
    printf("actSG:            %lld\n", (long long)result.actSG);
    printf("editSG:           %lld\n", (long long)result.editSG);
    printf("cnfEdit:          %d\n", result.cnfEdit);
    printf("lActTm:           %lld\n", (long long)result.lActTm.secondsSinceEpoch);
    printf("resvTms present:  %d\n", result._has_resvTms);

    int ok = 1;
    if (strcmp(cb.sgcbName, result.sgcbName) != 0) ok = 0;
    if (cb.numOfSG != result.numOfSG) ok = 0;
    if (cb._has_resvTms != result._has_resvTms) ok = 0;

    printf("\n%s\n", ok ? "PASS" : "FAIL");
    return ok ? 0 : 1;
}
