#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_choice.h"
#include "per/cms_sequence.h"

static int failed = 0;

#define TEST(name, expr) do { \
    if (!(expr)) { printf("  FAIL: %s\n", name); failed++; } \
    else { printf("  PASS: %s\n", name); } \
} while(0)

/* ==================== stream ==================== */

static void test_stream_bit_io(void) {
    printf("=== stream: bit I/O ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));

    /* write 3 bits: 1, 0, 1 */
    TEST("write_bit 1", per_stream_write_bit(&s, 1) == PER_OK);
    TEST("write_bit 0", per_stream_write_bit(&s, 0) == PER_OK);
    TEST("write_bit 1", per_stream_write_bit(&s, 1) == PER_OK);
    TEST("tell after 3 bits", per_stream_tell(&s) == 3);

    /* write 5 more to align (total 8 bits = 1 byte) */
    TEST("write_bits 0x1e (5 bits)", per_stream_write_bits(&s, 0x1e, 5) == PER_OK);
    TEST("tell after 8 bits", per_stream_tell(&s) == 8);
    TEST("bytes_written after 1 byte", per_stream_bytes_written(&s) == 1);

    /* verify byte 0: bits 1,0,1,1,1,1,1,0 = 0xBE */
    TEST("byte 0 value", buf[0] == 0xBE);

    /* read back */
    per_stream_init_read(&s, buf, sizeof(buf));
    int bit;
    TEST("read_bit 1", per_stream_read_bit(&s, &bit) == PER_OK && bit == 1);
    TEST("read_bit 0", per_stream_read_bit(&s, &bit) == PER_OK && bit == 0);
    TEST("read_bit 1", per_stream_read_bit(&s, &bit) == PER_OK && bit == 1);
    uint64_t val;
    TEST("read_bits 5", per_stream_read_bits(&s, &val, 5) == PER_OK && val == 0x1e);
}

static void test_stream_align(void) {
    printf("=== stream: align ===\n");
    uint8_t buf[16];
    per_stream_t s;
    uint64_t val;
    per_stream_init_write(&s, buf, sizeof(buf));

    per_stream_write_bits(&s, 0x55, 4);
    TEST("tell before align", per_stream_tell(&s) == 4);
    per_stream_align(&s);
    TEST("tell after  align", per_stream_tell(&s) == 8);
    TEST("byte_pos after align", s.byte_pos == 1);

    /* write a byte - should be at byte 1 */
    per_stream_write_byte_aligned(&s, 0xAB);
    TEST("byte 1 value", buf[1] == 0xAB);

    /* read back: skip 4 bits, align, read byte */
    per_stream_init_read(&s, buf, sizeof(buf));
    per_stream_read_bits(&s, &val, 4);
    per_stream_align(&s);
    uint8_t byte;
    per_stream_read_byte_aligned(&s, &byte);
    TEST("read aligned byte", byte == 0xAB);
}

static void test_stream_dynamic(void) {
    printf("=== stream: dynamic mode ===\n");
    per_stream_t s;
    TEST("init_dynamic", per_stream_init_dynamic(&s, 16) == PER_OK);

    /* write more than initial capacity */
    uint8_t data[32];
    memset(data, 0xAA, sizeof(data));
    TEST("write 32 bytes", per_stream_write_bytes(&s, data, 32) == PER_OK);
    TEST("bytes_written", per_stream_bytes_written(&s) == 32);

    size_t out_len;
    uint8_t *buf = per_stream_detach(&s, &out_len);
    TEST("detach length", out_len == 32);
    TEST("detach content", buf && buf[0] == 0xAA && buf[31] == 0xAA);
    free(buf);
}

static void test_stream_byte_io(void) {
    printf("=== stream: byte I/O ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));

    uint8_t data[] = { 0x01, 0x02, 0x03 };
    TEST("write bytes", per_stream_write_bytes(&s, data, 3) == PER_OK);
    TEST("tell after 3 bytes", per_stream_tell(&s) == 24);

    uint8_t out[3];
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("read bytes", per_stream_read_bytes(&s, out, 3) == PER_OK);
    TEST("read content", out[0] == 0x01 && out[1] == 0x02 && out[2] == 0x03);
}

/* ==================== integer ==================== */

static void test_integer_constrained_small(void) {
    printf("=== integer: constrained (range < 256) ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode 5 (0..9)", per_encode_constrained_int(&s, 5, 0, 9) == PER_OK);
    /* range=10, bits_needed=4, expect 4 bits = 0101 */
    TEST("tell after 4 bits", per_stream_tell(&s) == 4);

    int64_t val;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode 5 (0..9)", per_decode_constrained_int(&s, &val, 0, 9) == PER_OK && val == 5);
}

static void test_integer_constrained_range1(void) {
    printf("=== integer: constrained (range == 1) ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode range=1", per_encode_constrained_int(&s, 42, 42, 42) == PER_OK);
    TEST("tell 0 bits", per_stream_tell(&s) == 0);

    int64_t val;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode range=1", per_decode_constrained_int(&s, &val, 42, 42) == PER_OK && val == 42);
}

static void test_integer_constrained_medium(void) {
    printf("=== integer: constrained (256 <= range <= 65536) ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode 30000 (0..40000)", per_encode_constrained_int(&s, 30000, 0, 40000) == PER_OK);
    /* range=40001, bytes_for_range=2, align + 2 bytes */
    TEST("bit_pos 0 after align", s.bit_pos == 0);

    int64_t val;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode 30000 (0..40000)", per_decode_constrained_int(&s, &val, 0, 40000) == PER_OK && val == 30000);
}

static void test_integer_constrained_large(void) {
    printf("=== integer: constrained (range > 65536) ===\n");
    uint8_t buf[32];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));

    /* 100000 in range 0..200000 → range=200001, bytes_for_range=3 */
    TEST("encode 100000 (0..200000)", per_encode_constrained_int(&s, 100000, 0, 200000) == PER_OK);

    int64_t val;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode 100000 (0..200000)", per_decode_constrained_int(&s, &val, 0, 200000) == PER_OK && val == 100000);
}

static void test_integer_out_of_range(void) {
    printf("=== integer: out of range ===\n");
    uint8_t buf[16];
    per_stream_t s;
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode out of range", per_encode_constrained_int(&s, 20, 0, 10) == PER_ERR_RANGE);
}

static void test_integer_length(void) {
    printf("=== integer: length determinant ===\n");
    uint8_t buf[16];
    per_stream_t s;

    /* short form: <= 127 */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode length 42", per_encode_length(&s, 42) == PER_OK);
    TEST("tell after 1 byte", per_stream_tell(&s) == 8);
    uint32_t len;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode length 42", per_decode_length(&s, &len) == PER_OK && len == 42);

    /* long form: 128..16383 */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode length 1000", per_encode_length(&s, 1000) == PER_OK);
    TEST("tell after 2 bytes", per_stream_tell(&s) == 16);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode length 1000", per_decode_length(&s, &len) == PER_OK && len == 1000);

    /* > 16383 not supported */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode length >16383", per_encode_length(&s, 20000) == PER_ERR_RANGE);
}

static void test_integer_small_non_negative(void) {
    printf("=== integer: small non-negative ===\n");
    uint8_t buf[16];
    per_stream_t s;

    /* small: 42 */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode SNN 42", per_encode_small_non_negative(&s, 42) == PER_OK);
    uint32_t val;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode SNN 42", per_decode_small_non_negative(&s, &val) == PER_OK && val == 42);

    /* large: 100 */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode SNN 100", per_encode_small_non_negative(&s, 100) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode SNN 100", per_decode_small_non_negative(&s, &val) == PER_OK && val == 100);
}

static void test_integer_unconstrained(void) {
    printf("=== integer: unconstrained signed ===\n");
    uint8_t buf[16];
    per_stream_t s;
    int64_t val;

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode unconstrained 42", per_encode_unconstrained_int(&s, 42) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode unconstrained 42", per_decode_unconstrained_int(&s, &val) == PER_OK && val == 42);

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode unconstrained -128", per_encode_unconstrained_int(&s, -128) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode unconstrained -128", per_decode_unconstrained_int(&s, &val) == PER_OK && val == -128);

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode unconstrained -999999", per_encode_unconstrained_int(&s, -999999) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode unconstrained -999999", per_decode_unconstrained_int(&s, &val) == PER_OK && val == -999999);
}

static void test_integer_semi_constrained(void) {
    printf("=== integer: semi-constrained ===\n");
    uint8_t buf[16];
    per_stream_t s;
    int64_t val;

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode semi 10000 (0..MAX)", per_encode_semi_constrained(&s, 10000, 0) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode semi 10000 (0..MAX)", per_decode_semi_constrained(&s, &val, 0) == PER_OK && val == 10000);
}

static void test_integer_unsigned_to_bytes(void) {
    printf("=== integer: unsigned_to_bytes ===\n");
    uint8_t out[8];
    int n = per_unsigned_to_bytes(0, out, 8);
    TEST("zero → 1 byte", n == 1 && out[0] == 0);
    n = per_unsigned_to_bytes(255, out, 8);
    TEST("255 → 1 byte", n == 1 && out[0] == 0xFF);
    n = per_unsigned_to_bytes(256, out, 8);
    TEST("256 → 2 bytes", n == 2 && out[0] == 0x01 && out[1] == 0x00);
    n = per_unsigned_to_bytes(0xABCD, out, 8);
    TEST("0xABCD → 2 bytes", n == 2 && out[0] == 0xAB && out[1] == 0xCD);
}

/* ==================== string ==================== */

static void test_string_octet(void) {
    printf("=== string: octet ===\n");
    uint8_t buf[64];
    per_stream_t s;
    uint8_t data[] = { 0xCA, 0xFE, 0xBA, 0xBE };

    /* fixed length */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode octet fixed", per_encode_octet_string_fixed(&s, data, 4) == PER_OK);
    uint8_t out[4];
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode octet fixed", per_decode_octet_string_fixed(&s, out, 4) == PER_OK);
    TEST("octet fixed content", memcmp(data, out, 4) == 0);

    /* variable length SIZE(0..10) */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode octet var", per_encode_octet_string(&s, data, 4, 10) == PER_OK);
    size_t out_len;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode octet var", per_decode_octet_string(&s, out, &out_len, 10) == PER_OK);
    TEST("octet var length", out_len == 4);
    TEST("octet var content", memcmp(data, out, 4) == 0);

    /* unconstrained */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode octet unconstrained", per_encode_octet_string_unconstrained(&s, data, 4) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode octet unconstrained", per_decode_octet_string_unconstrained(&s, out, &out_len) == PER_OK);
    TEST("octet unconstrained length", out_len == 4);
    TEST("octet unconstrained content", memcmp(data, out, 4) == 0);
}

static void test_string_visible(void) {
    printf("=== string: visible ===\n");
    uint8_t buf[64];
    per_stream_t s;
    const uint8_t *str = (const uint8_t *)"hello";

    /* constrained variable SIZE(0..10) */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode visible", per_encode_visible_string(&s, str, 10) == PER_OK);
    uint8_t out[16] = {0};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode visible", per_decode_visible_string(&s, out, 10) == PER_OK);
    TEST("visible content", strcmp((const char *)out, "hello") == 0);

    /* fixed length */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode visible fixed", per_encode_visible_string_fixed(&s, str, 8) == PER_OK);
    memset(out, 0, sizeof(out));
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode visible fixed", per_decode_visible_string_fixed(&s, out, 8) == PER_OK);
    TEST("visible fixed content", strcmp((const char *)out, "hello") == 0);
    /* trailing bytes should be zero-padded */
    TEST("visible fixed pad", out[5] == 0 && out[7] == 0);

    /* unconstrained */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode visible unconstrained", per_encode_visible_string_unconstrained(&s, str) == PER_OK);
    memset(out, 0, sizeof(out));
    uint32_t out_len;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode visible unconstrained", per_decode_visible_string_unconstrained(&s, out, &out_len) == PER_OK);
    TEST("visible unconstrained length", out_len == 5);
    TEST("visible unconstrained content", strcmp((const char *)out, "hello") == 0);
}

static void test_string_utf8(void) {
    printf("=== string: utf8 ===\n");
    uint8_t buf[64];
    per_stream_t s;
    const uint8_t *str = (const uint8_t *)"héllo";

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode utf8", per_encode_utf8_string(&s, str, 10) == PER_OK);
    uint8_t out[16] = {0};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode utf8", per_decode_utf8_string(&s, out, 10) == PER_OK);
    TEST("utf8 content", strcmp((const char *)out, (const char *)str) == 0);
}

static void test_string_bit_fixed(void) {
    printf("=== string: bit string fixed ===\n");
    uint8_t buf[64];
    per_stream_t s;
    /* 13 bits: 1010101010101 */
    uint8_t data[2] = { 0xAA, 0xA8 };

    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode bit fixed 13", per_encode_bit_string_fixed(&s, data, 13) == PER_OK);
    uint8_t out[2] = {0};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bit fixed 13", per_decode_bit_string_fixed(&s, out, 13) == PER_OK);
    TEST("bit fixed content", out[0] == 0xAA && out[1] == 0xA8);
}

static void test_string_bit_variable(void) {
    printf("=== string: bit string variable ===\n");
    uint8_t buf[64];
    per_stream_t s;
    uint8_t data[2] = { 0xAA, 0xA8 };

    /* constrained SIZE(0..16) */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode bit var", per_encode_bit_string(&s, data, 13, 16) == PER_OK);
    uint8_t out[2] = {0};
    int out_nbits;
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bit var", per_decode_bit_string(&s, out, &out_nbits, 16) == PER_OK);
    TEST("bit var nbits", out_nbits == 13);
    TEST("bit var content", out[0] == 0xAA && out[1] == 0xA8);

    /* unconstrained */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode bit unconstrained", per_encode_bit_string_unconstrained(&s, data, 13) == PER_OK);
    memset(out, 0, sizeof(out));
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bit unconstrained", per_decode_bit_string_unconstrained(&s, out, &out_nbits) == PER_OK);
    TEST("bit unconstrained nbits", out_nbits == 13);
    TEST("bit unconstrained content", out[0] == 0xAA && out[1] == 0xA8);
}

/* ==================== choice ==================== */

static void test_choice(void) {
    printf("=== choice ===\n");
    uint8_t buf[16];
    per_stream_t s;
    uint32_t val;

    /* small index (<=63) */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode choice 5", per_encode_choice(&s, 5) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode choice 5", per_decode_choice(&s, &val) == PER_OK && val == 5);

    /* large index (>63) */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode choice 100", per_encode_choice(&s, 100) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode choice 100", per_decode_choice(&s, &val) == PER_OK && val == 100);

    /* extensible: root */
    bool is_ext;
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode choice extensible root 3", per_encode_choice_extensible(&s, false, 3) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode choice extensible root 3", per_decode_choice_extensible(&s, &is_ext, &val) == PER_OK);
    TEST("not extension", !is_ext && val == 3);

    /* extensible: extension */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode choice extensible ext 200", per_encode_choice_extensible(&s, true, 200) == PER_OK);
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode choice extensible ext 200", per_decode_choice_extensible(&s, &is_ext, &val) == PER_OK);
    TEST("is extension", is_ext && val == 200);
}

/* ==================== sequence ==================== */

static void test_sequence_bitmap(void) {
    printf("=== sequence: optional bitmap ===\n");
    uint8_t buf[16];
    per_stream_t s;

    /* 0 fields */
    per_stream_init_write(&s, buf, sizeof(buf));
    TEST("encode bitmap 0 fields", per_encode_optional_bitmap(&s, NULL, 0) == PER_OK);
    bool flags00[1] = {false};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bitmap 0 fields", per_decode_optional_bitmap(&s, flags00, 0) == PER_OK);

    /* 2 fields: [present, absent] */
    per_stream_init_write(&s, buf, sizeof(buf));
    bool flags2enc[2] = {true, false};
    TEST("encode bitmap 2 fields", per_encode_optional_bitmap(&s, flags2enc, 2) == PER_OK);
    TEST("tell after 2 bits", per_stream_tell(&s) == 2);
    bool flags2dec[2] = {false, false};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bitmap 2 fields", per_decode_optional_bitmap(&s, flags2dec, 2) == PER_OK);
    TEST("bitmap[0]=true", flags2dec[0] == true);
    TEST("bitmap[1]=false", flags2dec[1] == false);

    /* 7 fields: all present */
    per_stream_init_write(&s, buf, sizeof(buf));
    bool flags7enc[7] = {true, true, true, true, true, true, true};
    TEST("encode bitmap 7 all-present", per_encode_optional_bitmap(&s, flags7enc, 7) == PER_OK);
    bool flags7dec[7] = {false};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bitmap 7 all-present", per_decode_optional_bitmap(&s, flags7dec, 7) == PER_OK);
    bool all_ok = true;
    for (int i = 0; i < 7; i++) if (!flags7dec[i]) { all_ok = false; break; }
    TEST("bitmap 7 all-present value", all_ok);

    /* 8 fields: all present → should take 1 byte */
    per_stream_init_write(&s, buf, sizeof(buf));
    bool flags8enc[8] = {true, true, true, true, true, true, true, true};
    TEST("encode bitmap 8 all-present", per_encode_optional_bitmap(&s, flags8enc, 8) == PER_OK);
    bool flags8dec[8] = {false};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bitmap 8 all-present", per_decode_optional_bitmap(&s, flags8dec, 8) == PER_OK);
    all_ok = true;
    for (int i = 0; i < 8; i++) if (!flags8dec[i]) { all_ok = false; break; }
    TEST("bitmap 8 all-present value", all_ok);

    /* 64 fields: all present */
    per_stream_init_write(&s, buf, sizeof(buf));
    bool flags64enc[64];
    for (int i = 0; i < 64; i++) flags64enc[i] = true;
    TEST("encode bitmap 64 all-present", per_encode_optional_bitmap(&s, flags64enc, 64) == PER_OK);
    bool flags64dec[64] = {false};
    per_stream_init_read(&s, buf, sizeof(buf));
    TEST("decode bitmap 64 all-present", per_decode_optional_bitmap(&s, flags64dec, 64) == PER_OK);
    all_ok = true;
    for (int i = 0; i < 64; i++) if (!flags64dec[i]) { all_ok = false; break; }
    TEST("bitmap 64 all-present value", all_ok);
}

/* ==================== main ==================== */

int main(void) {
    /* stream */
    test_stream_bit_io();
    test_stream_align();
    test_stream_dynamic();
    test_stream_byte_io();

    /* integer */
    test_integer_constrained_small();
    test_integer_constrained_range1();
    test_integer_constrained_medium();
    test_integer_constrained_large();
    test_integer_out_of_range();
    test_integer_length();
    test_integer_small_non_negative();
    test_integer_unconstrained();
    test_integer_semi_constrained();
    test_integer_unsigned_to_bytes();

    /* string */
    test_string_octet();
    test_string_visible();
    test_string_utf8();
    test_string_bit_fixed();
    test_string_bit_variable();

    /* choice */
    test_choice();

    /* sequence */
    test_sequence_bitmap();

    printf("\n%d test(s) failed.\n", failed);
    return failed > 0 ? 1 : 0;
}
