#include "per_stream.h"
#include "test_utils.h"
#include <string.h>

void test_stream(void) {
    printf("[Stream]\n");

    TEST("single bit write/read");
    {
        uint8_t buf[16] = {0};
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 1));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 0));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 1));
        ASSERT_EQ(PER_OK, per_stream_write_bit(&w, 1));
        ASSERT_EQ(4, per_stream_tell(&w));

        per_stream_init_read(&r, buf, sizeof(buf));
        int v;
        per_stream_read_bit(&r, &v); ASSERT_EQ(1, v);
        per_stream_read_bit(&r, &v); ASSERT_EQ(0, v);
        per_stream_read_bit(&r, &v); ASSERT_EQ(1, v);
        per_stream_read_bit(&r, &v); ASSERT_EQ(1, v);
    }
    PASS();

    TEST("multi-bit write/read");
    {
        uint8_t buf[16] = {0};
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_stream_write_bits(&w, 0x1A5ULL, 13));
        per_stream_init_read(&r, buf, sizeof(buf));
        uint64_t val;
        per_stream_read_bits(&r, &val, 13);
        ASSERT_EQ(0x1A5, val);
    }
    PASS();

    TEST("byte align check");
    {
        uint8_t buf[16] = {0};
        per_stream_t w;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_stream_write_bits(&w, 5, 5);
        ASSERT_EQ(5, w.bit_pos);
        per_stream_align(&w);
        ASSERT_EQ(0, w.bit_pos);
        ASSERT_EQ(1, w.byte_pos);
    }
    PASS();

    TEST("write_byte_aligned auto-aligns");
    {
        uint8_t buf[16] = {0};
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_stream_write_bits(&w, 3, 3);
        per_stream_write_byte_aligned(&w, 0xAB);
        ASSERT_EQ(0, w.bit_pos);
        ASSERT_EQ(2, w.byte_pos);
        /* buf[0] should contain 3 << 5 = 0x60, buf[1] = 0xAB */

        per_stream_init_read(&r, buf, sizeof(buf));
        uint64_t v3;
        per_stream_read_bits(&r, &v3, 3);
        ASSERT_EQ(3, v3);
        per_stream_align(&r);
        uint8_t byte;
        per_stream_read_byte_aligned(&r, &byte);
        ASSERT_EQ(0xAB, byte);
    }
    PASS();

    printf("\n");
}
