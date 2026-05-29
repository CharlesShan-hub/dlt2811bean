#include "per_boolean.h"
#include "test_utils.h"

void test_boolean(void) {
    printf("[Boolean]\n");

    TEST("encode/decode true");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_boolean(&w, true));
        per_stream_init_read(&r, buf, sizeof(buf));
        bool v;
        ASSERT_EQ(PER_OK, per_decode_boolean(&r, &v));
        ASSERT_EQ(true, v);
    }
    PASS();

    TEST("encode/decode false");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_boolean(&w, false));
        per_stream_init_read(&r, buf, sizeof(buf));
        bool v;
        ASSERT_EQ(PER_OK, per_decode_boolean(&r, &v));
        ASSERT_EQ(false, v);
    }
    PASS();

    TEST("alternating bits");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_boolean(&w, true);
        per_encode_boolean(&w, false);
        per_encode_boolean(&w, true);
        ASSERT_EQ(3, per_stream_tell(&w));
        per_stream_init_read(&r, buf, sizeof(buf));
        bool v;
        per_decode_boolean(&r, &v); ASSERT_EQ(true, v);
        per_decode_boolean(&r, &v); ASSERT_EQ(false, v);
        per_decode_boolean(&r, &v); ASSERT_EQ(true, v);
    }
    PASS();

    printf("\n");
}
