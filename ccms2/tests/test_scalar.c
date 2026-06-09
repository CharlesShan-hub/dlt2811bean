#include <stdio.h>
#include <string.h>
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8.h"
#include "data/scalar/cms_int8u.h"
#include "data/scalar/cms_int16.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_float32.h"
#include "data/scalar/cms_float64.h"
#include "data/time/cms_time_quality.h"
#include "data/time/cms_utc_time.h"
#include "data/time/cms_binary_time.h"
#include "data/common/cms_quality.h"

static int failed = 0;

#define TEST(name, expr) do { \
    if (!(expr)) { printf("  FAIL: %s\n", name); failed++; } \
    else { printf("  PASS: %s\n", name); } \
} while(0)

static void test_boolean() {
    printf("=== boolean ===\n");
    uint8_t buf[64]; int len;

    int v = 1; len = sizeof(buf);
    TEST("encode true", cms_boolean_encode(&v, buf, &len) == CMS_OK && len > 0);
    int v2 = 0;
    TEST("decode", cms_boolean_decode(&v2, buf, len) == CMS_OK);
    TEST("value", v2 == 1);
}

static void test_int8() {
    printf("=== int8 ===\n");
    uint8_t buf[64]; int len;

    int8_t v = -42; len = sizeof(buf);
    TEST("encode -42", cms_int8_encode(&v, buf, &len) == CMS_OK && len > 0);
    int8_t v2 = 0;
    TEST("decode", cms_int8_decode(&v2, buf, len) == CMS_OK);
    TEST("value", v2 == -42);
}

static void test_int8u() {
    printf("=== int8u ===\n");
    uint8_t buf[64]; int len;

    uint8_t v = 200; len = sizeof(buf);
    TEST("encode 200", cms_int8u_encode(&v, buf, &len) == CMS_OK && len > 0);
    uint8_t v2 = 0;
    TEST("decode", cms_int8u_decode(&v2, buf, len) == CMS_OK);
    TEST("value", v2 == 200);
}

static void test_int32u() {
    printf("=== int32u ===\n");
    uint8_t buf[64]; int len;

    uint32_t v = 123456; len = sizeof(buf);
    TEST("encode 123456", cms_int32u_encode(&v, buf, &len) == CMS_OK && len > 0);
    uint32_t v2 = 0;
    TEST("decode", cms_int32u_decode(&v2, buf, len) == CMS_OK);
    TEST("value", v2 == 123456);
}

static void test_float32() {
    printf("=== float32 ===\n");
    uint8_t buf[64]; int len;

    uint8_t v[4] = {0x40, 0x20, 0x00, 0x00}; /* 2.5f */
    len = sizeof(buf);
    TEST("encode", cms_float32_encode(v, buf, &len) == CMS_OK && len >= 4);
    uint8_t v2[4] = {0};
    TEST("decode", cms_float32_decode(v2, buf, len) == CMS_OK);
    TEST("value", memcmp(v, v2, 4) == 0);
}

static void test_float64() {
    printf("=== float64 ===\n");
    uint8_t buf[64]; int len;

    uint8_t v[8] = {0x40, 0x24, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; /* 10.0 */
    len = sizeof(buf);
    TEST("encode", cms_float64_encode(v, buf, &len) == CMS_OK && len >= 8);
    uint8_t v2[8] = {0};
    TEST("decode", cms_float64_decode(v2, buf, len) == CMS_OK);
    TEST("value", memcmp(v, v2, 8) == 0);
}

static void test_time_quality() {
    printf("=== time_quality ===\n");
    uint8_t buf[64]; int len;

    cms_time_quality_t q = {
        .leap_seconds_known      = { .value = 1 },
        .clock_failure           = { .value = 0 },
        .clock_not_synchronized  = { .value = 1 },
        .precision               = { .value = 15 },
    };
    len = sizeof(buf);
    TEST("encode", cms_time_quality_encode(&q, buf, &len) == CMS_OK && len >= 1);

    cms_time_quality_t q2 = {0};
    TEST("decode", cms_time_quality_decode(&q2, buf, len) == CMS_OK);
    TEST("leap_seconds_known", q2.leap_seconds_known.value == 1);
    TEST("clock_failure", q2.clock_failure.value == 0);
    TEST("clock_not_synchronized", q2.clock_not_synchronized.value == 1);
    TEST("precision", q2.precision.value == 15);
}

static void test_utc_time() {
    printf("=== utc_time ===\n");
    uint8_t buf[64]; int len;

    cms_utc_time_t t = {
        .seconds_since_epoch  = { .value = 1700000000 },
        .fraction_of_second   = { .value = 500000 },
        .time_quality = {
            .leap_seconds_known     = { .value = 1 },
            .clock_failure          = { .value = 0 },
            .clock_not_synchronized = { .value = 0 },
            .precision              = { .value = 10 },
        },
    };
    len = sizeof(buf);
    TEST("encode", cms_utc_time_encode(&t, buf, &len) == CMS_OK && len == 8);

    cms_utc_time_t t2 = {0};
    TEST("decode", cms_utc_time_decode(&t2, buf, len) == CMS_OK);
    TEST("seconds", t2.seconds_since_epoch.value == 1700000000);
    TEST("fraction", t2.fraction_of_second.value == 500000);
    TEST("leap", t2.time_quality.leap_seconds_known.value == 1);
}

static void test_binary_time() {
    printf("=== binary_time ===\n");
    uint8_t buf[64]; int len;

    cms_binary_time_t t = {
        .msOfDay         = { .value = 12345678 },
        .daysSince1984   = { .value = 15000 },
    };
    len = sizeof(buf);
    TEST("encode", cms_binary_time_encode(&t, buf, &len) == CMS_OK);
    printf("  binary_time encoded %d bytes\n", len);

    cms_binary_time_t t2 = {0};
    TEST("decode", cms_binary_time_decode(&t2, buf, len) == CMS_OK);
    TEST("msOfDay", t2.msOfDay.value == 12345678);
    TEST("daysSince1984", t2.daysSince1984.value == 15000);
}

static void test_quality() {
    printf("=== quality ===\n");
    uint8_t buf[64]; int len;

    cms_quality_t q = {
        .validity            = { .value = CMS_QUALITY_GOOD },
        .overflow            = { .value = 0 },
        .outOfRange          = { .value = 1 },
        .badReference        = { .value = 0 },
        .oscillatory         = { .value = 0 },
        .failure             = { .value = 1 },
        .oldData             = { .value = 0 },
        .inconsistent        = { .value = 0 },
        .inaccurate          = { .value = 1 },
        .substituted         = { .value = 0 },
        .test                = { .value = 0 },
        .operatorBlocked     = { .value = 0 },
    };
    len = sizeof(buf);
    TEST("encode", cms_quality_encode(&q, buf, &len) == CMS_OK);

    cms_quality_t q2 = {0};
    TEST("decode", cms_quality_decode(&q2, buf, len) == CMS_OK);
    TEST("validity", q2.validity.value == CMS_QUALITY_GOOD);
    TEST("outOfRange", q2.outOfRange.value == 1);
    TEST("failure", q2.failure.value == 1);
    TEST("inaccurate", q2.inaccurate.value == 1);
}

int main() {
    test_boolean();
    test_int8();
    test_int8u();
    test_int32u();
    test_float32();
    test_float64();
    test_time_quality();
    test_utc_time();
    test_binary_time();
    test_quality();

    printf("\n=== %s ===\n", failed ? "SOME FAILED" : "ALL PASS");
    return failed;
}
