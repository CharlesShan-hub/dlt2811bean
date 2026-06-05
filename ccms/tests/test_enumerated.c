#include "data/basic/cms_enumerated.h"
#include "per/cms_integer.h"
#include "test_utils.h"

void test_enumerated(void) {
    printf("[Enumerated]\n");

    TEST("enum 2 values (1 bit)");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_constrained_int(&w, 1, 0, 1));
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        ASSERT_EQ(PER_OK, per_decode_constrained_int(&r, &v, 0, 1));
        ASSERT_EQ(1, v);
    }
    PASS();

    TEST("enum 6 values (3 bits)");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_constrained_int(&w, 4, 0, 5));
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        ASSERT_EQ(PER_OK, per_decode_constrained_int(&r, &v, 0, 5));
        ASSERT_EQ(4, v);
    }
    PASS();

    printf("\n");
}
