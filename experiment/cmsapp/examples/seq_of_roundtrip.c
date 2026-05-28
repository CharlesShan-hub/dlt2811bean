#include "cmsper/cmsper.h"
#include "gen_dlt2811b_test_seq.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
    uint8_t buf[256];
    per_stream_t w, r;

    /* ========== 编码 ========== */
    per_stream_init_write(&w, buf, sizeof(buf));

    Container c;
    memset(&c, 0, sizeof(c));
    c.name = "TestContainer";
    c.items_count = 3;

    /* 动态分配 items 数组 */
    c.items = calloc(c.items_count, sizeof(Item));
    c.items[0].id    = 1;
    c.items[0].value = 100;
    c.items[1].id    = 2;
    c.items[1].value = 200;
    c.items[2].id    = 3;
    c.items[2].value = 300;

    printf("=== 编码 Container ===\n");
    printf("name:  %s\n", c.name);
    printf("items: %d 个\n", c.items_count);
    for (int i = 0; i < c.items_count; i++) {
        printf("  [%d] id=%lld  value=%lld\n", i,
               (long long)c.items[i].id, (long long)c.items[i].value);
    }

    encode_Container(&w, &c);
    printf("编码后 %zu 字节\n\n", per_stream_bytes_written(&w));

    /* ========== 解码 ========== */
    Container result;
    memset(&result, 0, sizeof(result));
    per_stream_init_read(&r, buf, sizeof(buf));

    printf("=== 解码 Container ===\n");
    decode_Container(&r, &result);

    printf("name:  %s\n", result.name);
    printf("items: %d 个\n", result.items_count);
    for (int i = 0; i < result.items_count; i++) {
        printf("  [%d] id=%lld  value=%lld\n", i,
               (long long)result.items[i].id, (long long)result.items[i].value);
    }

    /* 验证 */
    int ok = 1;
    if (strcmp(c.name, result.name) != 0) ok = 0;
    if (c.items_count != result.items_count) ok = 0;
    for (int i = 0; i < c.items_count; i++) {
        if (c.items[i].id != result.items[i].id) ok = 0;
        if (c.items[i].value != result.items[i].value) ok = 0;
    }

    /* 清理 */
    free(c.items);
    free(result.items);
    free(result.name);

    printf("\n%s\n", ok ? "PASS" : "FAIL");
    return ok ? 0 : 1;
}
