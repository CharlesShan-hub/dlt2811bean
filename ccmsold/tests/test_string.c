#include "per/cms_string.h"
#include "test_utils.h"
#include <string.h>

void test_string(void) {
    printf("[String]\n");

    TEST("octet string fixed 8 bytes");
    {
        uint8_t buf[32], data[8] = {0,1,2,3,4,5,6,7}, out[8];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_octet_string_fixed(&w, data, 8);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_octet_string_fixed(&r, out, 8);
        ASSERT_EQ(0, memcmp(data, out, 8));
    }
    PASS();

    TEST("visible string round-trip");
    {
        uint8_t buf[32];
        char out[130];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_visible_string(&w, (const uint8_t *)"Hello", 129);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_visible_string(&r, (uint8_t *)out, 129);
        ASSERT_EQ(0, strcmp("Hello", out));
    }
    PASS();

    TEST("visible string empty");
    {
        uint8_t buf[32];
        char out[130];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_visible_string(&w, (const uint8_t *)"", 129);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_visible_string(&r, (uint8_t *)out, 129);
        ASSERT_EQ(0, strcmp("", out));
    }
    PASS();

    printf("\n");
}
