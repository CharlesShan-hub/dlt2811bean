#include "ccms/per_bit_string.h"
#include "test_utils.h"
#include <string.h>

void test_bit_string(void) {
    printf("[BitString]\n");

    TEST("fixed 10 bits round-trip");
    {
        uint8_t buf[16], out[4];
        uint8_t data[2] = {0xFF, 0xC0};
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_bit_string_fixed(&w, data, 10);
        per_stream_init_read(&r, buf, sizeof(buf));
        per_decode_bit_string_fixed(&r, out, 10);
        ASSERT_EQ(0xFF, out[0]);
        ASSERT_EQ(0xC0, out[1]);
    }
    PASS();

    printf("\n");
}
