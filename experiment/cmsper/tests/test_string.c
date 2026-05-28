#include "cmsper/per_string.h"
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
        per_encode_visible_string(&w, "Hello", 129);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_visible_string(&r, out, 129);
        ASSERT_EQ(0, strcmp("Hello", out));
    }
    PASS();

    TEST("visible string empty");
    {
        uint8_t buf[32];
        char out[130];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_visible_string(&w, "", 129);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_visible_string(&r, out, 129);
        ASSERT_EQ(0, strcmp("", out));
    }
    PASS();

    TEST("open type round-trip");
    {
        uint8_t buf[32];
        uint8_t inner[] = {0xDE, 0xAD, 0xBE, 0xEF};
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_open_type(&w, inner, 4);
        per_stream_init_read(&r, buf, sizeof(buf));
        const uint8_t *out;
        size_t out_len;
        per_decode_open_type(&r, &out, &out_len);
        ASSERT_EQ(4, out_len);
        ASSERT_EQ(0, memcmp(inner, out, out_len));
    }
    PASS();

    printf("\n");
}
