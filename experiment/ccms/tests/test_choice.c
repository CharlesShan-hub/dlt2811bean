#include "ccms/per_choice.h"
#include "test_utils.h"

void test_choice(void) {
    printf("[Choice]\n");

    TEST("choice index 42 (small non-negative)");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_choice(&w, 42));
        ASSERT_EQ(7, per_stream_tell(&w));
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_choice(&r, &v);
        ASSERT_EQ(42, v);
    }
    PASS();

    TEST("choice index 200 (large form)");
    {
        uint8_t buf[8];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_choice(&w, 200));
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_choice(&r, &v);
        ASSERT_EQ(200, v);
    }
    PASS();

    printf("\n");
}
