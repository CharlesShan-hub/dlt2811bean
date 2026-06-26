#include <stdio.h>
#include <stdlib.h>
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
    uint8_t *buf = NULL; size_t len;

    int v = 1;
    TEST("encode true", cms_boolean_encode(&v, &buf, &len) == CMS_OK && len > 0);
    int v2 = 0;
    TEST("decode", cms_boolean_decode(&v2, buf, (int)len) == CMS_OK);
    TEST("value", v2 == 1);
    free(buf);
}

static void test_int8() {
    printf("=== int8 ===\n");
    uint8_t *buf = NULL; size_t len;

    int8_t v = -42;
    TEST("encode -42", cms_int8_encode(&v, &buf, &len) == CMS_OK && len > 0);
    int8_t v2 = 0;
    TEST("decode", cms_int8_decode(&v2, buf, (int)len) == CMS_OK);
    TEST("value", v2 == -42);
    free(buf);
}

static void test_int8u() {
    printf("=== int8u ===\n");
    uint8_t *buf = NULL; size_t len;

    uint8_t v = 200;
    TEST("encode 200", cms_int8u_encode(&v, &buf, &len) == CMS_OK && len > 0);
    uint8_t v2 = 0;
    TEST("decode", cms_int8u_decode(&v2, buf, (int)len) == CMS_OK);
    TEST("value", v2 == 200);
    free(buf);
}

static void test_int32u() {
    printf("=== int32u ===\n");
    uint8_t *buf = NULL; size_t len;

    uint32_t v = 123456;
    TEST("encode 123456", cms_int32u_encode(&v, &buf, &len) == CMS_OK && len > 0);
    uint32_t v2 = 0;
    TEST("decode", cms_int32u_decode(&v2, buf, (int)len) == CMS_OK);
    TEST("value", v2 == 123456);
    free(buf);
}

static void test_float32() {
    printf("=== float32 ===\n");
    uint8_t *buf = NULL; size_t len;

    uint8_t v[4] = {0x40, 0x20, 0x00, 0x00}; /* 2.5f */
    TEST("encode", cms_float32_encode(v, &buf, &len) == CMS_OK && len >= 4);
    uint8_t v2[4] = {0};
    TEST("decode", cms_float32_decode(v2, buf, (int)len) == CMS_OK);
    TEST("value", memcmp(v, v2, 4) == 0);
    free(buf);
}

static void test_float64() {
    printf("=== float64 ===\n");
    uint8_t *buf = NULL; size_t len;

    uint8_t v[8] = {0x40, 0x24, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; /* 10.0 */
    TEST("encode", cms_float64_encode(v, &buf, &len) == CMS_OK && len >= 8);
    uint8_t v2[8] = {0};
    TEST("decode", cms_float64_decode(v2, buf, (int)len) == CMS_OK);
    TEST("value", memcmp(v, v2, 8) == 0);
    free(buf);
}

static void test_time_quality() {
    printf("=== time_quality ===\n");
    uint8_t *buf = NULL; size_t len;

    cms_boolean_t leap_v = { .value = 1 };
    cms_boolean_t clock_fail_v = { .value = 0 };
    cms_boolean_t clock_ns_v = { .value = 1 };
    cms_int32_t   prec_v = { .value = 15 };

    cms_time_quality_t q = {
        .leap_seconds_known      = &leap_v,
        .clock_failure           = &clock_fail_v,
        .clock_not_synchronized  = &clock_ns_v,
        .precision               = &prec_v,
    };
    TEST("encode", cms_time_quality_encode(&q, &buf, &len) == CMS_OK);

    cms_boolean_t leap_out = {0};
    cms_boolean_t clock_fail_out = {0};
    cms_boolean_t clock_ns_out = {0};
    cms_int32_t   prec_out = {0};

    cms_time_quality_t q2 = {
        .leap_seconds_known      = &leap_out,
        .clock_failure           = &clock_fail_out,
        .clock_not_synchronized  = &clock_ns_out,
        .precision               = &prec_out,
    };
    TEST("decode", cms_time_quality_decode(&q2, buf, (int)len) == CMS_OK);
    TEST("leap_seconds_known", leap_out.value == 1);
    TEST("clock_failure", clock_fail_out.value == 0);
    TEST("clock_not_synchronized", clock_ns_out.value == 1);
    TEST("precision", prec_out.value == 15);
    free(buf);
}

static void test_utc_time() {
    printf("=== utc_time ===\n");
    uint8_t *buf = NULL; size_t len;

    cms_boolean_t leap_v     = { .value = 1 };
    cms_boolean_t clock_f_v  = { .value = 0 };
    cms_boolean_t clock_ns_v = { .value = 0 };
    cms_int32_t   prec_v     = { .value = 10 };
    cms_int32u_t  secs_v     = { .value = 1700000000 };
    cms_int24u_t  frac_v     = { .value = 500000 };

    cms_time_quality_t tq = {
        .leap_seconds_known      = &leap_v,
        .clock_failure           = &clock_f_v,
        .clock_not_synchronized  = &clock_ns_v,
        .precision               = &prec_v,
    };
    cms_utc_time_t t = {
        .seconds_since_epoch  = &secs_v,
        .fraction_of_second   = &frac_v,
        .time_quality         = &tq,
    };
    TEST("encode", cms_utc_time_encode(&t, &buf, &len) == CMS_OK);

    cms_boolean_t leap_out     = {0};
    cms_boolean_t clock_f_out  = {0};
    cms_boolean_t clock_ns_out = {0};
    cms_int32_t   prec_out     = {0};
    cms_int32u_t  secs_out     = {0};
    cms_int24u_t  frac_out     = {0};

    cms_time_quality_t tq_out = {
        .leap_seconds_known      = &leap_out,
        .clock_failure           = &clock_f_out,
        .clock_not_synchronized  = &clock_ns_out,
        .precision               = &prec_out,
    };
    cms_utc_time_t t2 = {
        .seconds_since_epoch = &secs_out,
        .fraction_of_second  = &frac_out,
        .time_quality        = &tq_out,
    };
    TEST("decode", cms_utc_time_decode(&t2, buf, (int)len) == CMS_OK);
    TEST("seconds", secs_out.value == 1700000000);
    TEST("fraction", frac_out.value == 500000);
    TEST("leap", leap_out.value == 1);
    free(buf);
}

static void test_binary_time() {
    printf("=== binary_time ===\n");
    uint8_t *buf = NULL; size_t len;

    cms_int32u_t ms_v   = { .value = 12345678 };
    cms_int16u_t days_v = { .value = 15000 };

    cms_binary_time_t t = {
        .msOfDay         = &ms_v,
        .daysSince1984   = &days_v,
    };
    TEST("encode", cms_binary_time_encode(&t, &buf, &len) == CMS_OK);
    printf("  binary_time encoded %zu bytes\n", len);

    cms_int32u_t ms_out   = {0};
    cms_int16u_t days_out = {0};
    cms_binary_time_t t2 = {
        .msOfDay         = &ms_out,
        .daysSince1984   = &days_out,
    };
    TEST("decode", cms_binary_time_decode(&t2, buf, (int)len) == CMS_OK);
    TEST("msOfDay", ms_out.value == 12345678);
    TEST("daysSince1984", days_out.value == 15000);
    free(buf);
}

static void test_quality() {
    printf("=== quality ===\n");
    uint8_t *buf = NULL; size_t len;

    cms_int32_t   validity_val  = { .value = CMS_QUALITY_GOOD };
    cms_boolean_t overflow_v    = { .value = 0 };
    cms_boolean_t outOfRange_v  = { .value = 1 };
    cms_boolean_t badRef_v      = { .value = 0 };
    cms_boolean_t oscillatory_v = { .value = 0 };
    cms_boolean_t failure_v     = { .value = 1 };
    cms_boolean_t oldData_v     = { .value = 0 };
    cms_boolean_t incons_v      = { .value = 0 };
    cms_boolean_t inaccur_v     = { .value = 1 };
    cms_boolean_t subst_v       = { .value = 0 };
    cms_boolean_t test_v        = { .value = 0 };
    cms_boolean_t opBlocked_v   = { .value = 0 };

    cms_quality_t q = {
        .validity            = &validity_val,
        .overflow            = &overflow_v,
        .outOfRange          = &outOfRange_v,
        .badReference        = &badRef_v,
        .oscillatory         = &oscillatory_v,
        .failure             = &failure_v,
        .oldData             = &oldData_v,
        .inconsistent        = &incons_v,
        .inaccurate          = &inaccur_v,
        .substituted         = &subst_v,
        .test                = &test_v,
        .operatorBlocked     = &opBlocked_v,
    };
    TEST("encode", cms_quality_encode(&q, &buf, &len) == CMS_OK);

    cms_quality_t q2 = {0};
    cms_int32_t   validity_out  = {0};
    cms_boolean_t overflow_out  = {0};
    cms_boolean_t outOfRange_out = {0};
    cms_boolean_t badRef_out    = {0};
    cms_boolean_t oscillatory_out = {0};
    cms_boolean_t failure_out   = {0};
    cms_boolean_t oldData_out   = {0};
    cms_boolean_t incons_out    = {0};
    cms_boolean_t inaccur_out   = {0};
    cms_boolean_t subst_out     = {0};
    cms_boolean_t test_out      = {0};
    cms_boolean_t opBlocked_out = {0};
    q2.validity = &validity_out;
    q2.overflow = &overflow_out;
    q2.outOfRange = &outOfRange_out;
    q2.badReference = &badRef_out;
    q2.oscillatory = &oscillatory_out;
    q2.failure = &failure_out;
    q2.oldData = &oldData_out;
    q2.inconsistent = &incons_out;
    q2.inaccurate = &inaccur_out;
    q2.substituted = &subst_out;
    q2.test = &test_out;
    q2.operatorBlocked = &opBlocked_out;

    TEST("decode", cms_quality_decode(&q2, buf, (int)len) == CMS_OK);
    TEST("validity", validity_out.value == CMS_QUALITY_GOOD);
    TEST("outOfRange", outOfRange_out.value == 1);
    TEST("failure", failure_out.value == 1);
    TEST("inaccurate", inaccur_out.value == 1);
    free(buf);
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