#include "cmsper/per_enumerated.h"
#include "test_utils.h"

void test_enumerated(void) {
    printf("[Enumerated]\n");

    TEST("enum 2 values (1 bit)");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_enumerated(&w, 1, 2));
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_enumerated(&r, &v, 2);
        ASSERT_EQ(1, v);
    }
    PASS();

    TEST("enum 6 values (3 bits)");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_enumerated(&w, 4, 6);
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_enumerated(&r, &v, 6);
        ASSERT_EQ(4, v);
    }
    PASS();

    printf("\n");
}
