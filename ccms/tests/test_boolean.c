#include "per/cms_stream.h"
#include "test_utils.h"

void test_boolean(void) {
    printf("[Boolean]\n");

    TEST("encode/decode 1");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 1));
        per_stream_init_read(&r, buf, sizeof(buf));
        int v;
        ASSERT_EQ(PER_OK, per_stream_read_bit(&r, &v));
        ASSERT_EQ(1, v);
    }
    PASS();

    TEST("encode/decode 0");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 0));
        per_stream_init_read(&r, buf, sizeof(buf));
        int v;
        ASSERT_EQ(PER_OK, per_stream_read_bit(&r, &v));
        ASSERT_EQ(0, v);
    }
    PASS();

    TEST("alternating bits");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_stream_write_bit(&w, 1);
        per_stream_write_bit(&w, 0);
        per_stream_write_bit(&w, 1);
        ASSERT_EQ(3, per_stream_tell(&w));
        per_stream_init_read(&r, buf, sizeof(buf));
        int v;
        per_stream_read_bit(&r, &v); ASSERT_EQ(1, v);
        per_stream_read_bit(&r, &v); ASSERT_EQ(0, v);
        per_stream_read_bit(&r, &v); ASSERT_EQ(1, v);
    }
    PASS();

    printf("\n");
}
