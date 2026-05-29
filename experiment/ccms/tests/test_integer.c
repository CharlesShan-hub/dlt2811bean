#include "per_integer.h"
#include "test_utils.h"
#include <string.h>

void test_integer(void) {
    printf("[Integer]\n");

    /* range=11 (2..12): 4 bits */
    TEST("constrained 2..12 range=11 -> 4 bits");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_constrained_int(&w, 7, 2, 12));
        ASSERT_EQ(4, per_stream_tell(&w));
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        per_decode_constrained_int(&r, &v, 2, 12);
        ASSERT_EQ(7, v);
    }
    PASS();

    /* single value: 0 bits */
    TEST("constrained single value range=1 -> 0 bits");
    {
        uint8_t buf[4];
        per_stream_t w;
        per_stream_init_write(&w, buf, sizeof(buf));
        ASSERT_EQ(PER_OK, per_encode_constrained_int(&w, 42, 42, 42));
        ASSERT_EQ(0, per_stream_tell(&w));
    }
    PASS();

    /* range=256: align + 1 byte */
    TEST("range 256 (0..255) -> align + 1 byte");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_constrained_int(&w, 200, 0, 255);
        ASSERT_EQ(0, w.bit_pos);
        ASSERT_EQ(1, w.byte_pos);
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        per_decode_constrained_int(&r, &v, 0, 255);
        ASSERT_EQ(200, v);
    }
    PASS();

    /* range=65536: align + 2 bytes */
    TEST("range 65536 (0..65535) -> align + 2 bytes");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_constrained_int(&w, 50000, 0, 65535);
        ASSERT_EQ(0, w.bit_pos);
        ASSERT_EQ(2, w.byte_pos);
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        per_decode_constrained_int(&r, &v, 0, 65535);
        ASSERT_EQ(50000, v);
    }
    PASS();

    /* length: 0..127 -> 1 byte */
    TEST("length 127 -> 1 byte");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_length(&w, 127);
        ASSERT_EQ(1, w.byte_pos);
        ASSERT_EQ(buf[0], 127);
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_length(&r, &v);
        ASSERT_EQ(127, v);
    }
    PASS();

    /* length: 128..16383 -> 2 bytes */
    TEST("length 16383 -> 2 bytes");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_length(&w, 16383);
        ASSERT_EQ(2, w.byte_pos);
        ASSERT_EQ(buf[0], 0xBF);
        ASSERT_EQ(buf[1], 0xFF);
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_length(&r, &v);
        ASSERT_EQ(16383, v);
    }
    PASS();

    /* small non-negative: 0..63 -> 7 bits */
    TEST("small non-negative: 42 -> 7 bits");
    {
        uint8_t buf[4];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_small_non_negative(&w, 42);
        ASSERT_EQ(7, per_stream_tell(&w));
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_small_non_negative(&r, &v);
        ASSERT_EQ(42, v);
    }
    PASS();

    /* small non-negative: 64 -> 1bit(1) + length + content */
    TEST("small non-negative: 64 -> large form");
    {
        uint8_t buf[8];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_small_non_negative(&w, 200);
        per_stream_init_read(&r, buf, sizeof(buf));
        uint32_t v;
        per_decode_small_non_negative(&r, &v);
        ASSERT_EQ(200, v);
    }
    PASS();

    /* semi-constrained (unbounded) */
    TEST("semi-constrained 65535");
    {
        uint8_t buf[8];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_semi_constrained(&w, 65535, 0);
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        per_decode_semi_constrained(&r, &v, 0);
        ASSERT_EQ(65535, v);
    }
    PASS();

    /* unconstrained signed */
    TEST("unconstrained signed -42");
    {
        uint8_t buf[8];
        per_stream_t w, r;
        per_stream_init_write(&w, buf, sizeof(buf));
        per_encode_unconstrained_int(&w, -42);
        per_stream_init_read(&r, buf, sizeof(buf));
        int64_t v;
        per_decode_unconstrained_int(&r, &v);
        ASSERT_EQ(-42, v);
    }
    PASS();

    printf("\n");
}
