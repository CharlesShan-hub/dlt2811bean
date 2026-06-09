#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "svc/connection/cms2_associate.h"
#include "data/basic/cms2_basic.h"

static int failed = 0;

#define TEST(name, expr) do { \
    if (!(expr)) { \
        printf("  FAIL: %s\n", name); \
        failed++; \
    } else { \
        printf("  PASS: %s\n", name); \
    } \
} while(0)

static void test_boolean_roundtrip() {
    printf("=== Boolean roundtrip ===\n");
    cms2_boolean_t b = { 1 };
    uint8_t buf[64];
    int out_len = sizeof(buf);
    int rc = cms2_boolean_encode(&b, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);

    cms2_boolean_t b2 = { 0 };
    rc = cms2_boolean_decode(&b2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);
    TEST("value matches", b2.value == 1);
}

static void test_int8_roundtrip() {
    printf("\n=== Int8 roundtrip ===\n");
    cms2_int8_t v = { -42 };
    uint8_t buf[64];
    int out_len = sizeof(buf);
    int rc = cms2_int8_encode(&v, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);

    cms2_int8_t v2 = { 0 };
    rc = cms2_int8_decode(&v2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);
    TEST("value matches", v2.value == -42);
}

static void test_int8u_roundtrip() {
    printf("\n=== Int8U roundtrip ===\n");
    cms2_int8u_t v = { 200 };
    uint8_t buf[64];
    int out_len = sizeof(buf);
    int rc = cms2_int8u_encode(&v, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);

    cms2_int8u_t v2 = { 0 };
    rc = cms2_int8u_decode(&v2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);
    TEST("value matches", v2.value == 200);
}

static void test_associate_request() {
    printf("\n=== Associate-Request (all-pointer) ===\n");

    cms2_associate_request_t pdu;
    cms2_associate_request_init(&pdu);

    /* Set values */
    *(uint16_t*)pdu.req_id = 1001;
    *(int*)pdu.sap_ref_present = 1;

    cms2_uint8_array_t *sap = (cms2_uint8_array_t*)pdu.sap_ref;
    const char *sap_name = "MyServer";
    memcpy(sap->value, sap_name, strlen(sap_name) + 1);
    sap->len = (int32_t)strlen(sap_name);

    /* Encode */
    uint8_t buf[256];
    int out_len = sizeof(buf);
    int rc = cms2_associate_request_encode(&pdu, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);
    printf("  encoded %d bytes\n", out_len);

    /* Decode into new PDU */
    cms2_associate_request_t pdu2;
    cms2_associate_request_init(&pdu2);

    rc = cms2_associate_request_decode(&pdu2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);

    uint16_t req_id2 = *(uint16_t*)pdu2.req_id;
    int sap_present2 = *(int*)pdu2.sap_ref_present;
    cms2_uint8_array_t *sap2 = (cms2_uint8_array_t*)pdu2.sap_ref;

    TEST("req_id matches", req_id2 == 1001);
    TEST("sap_ref_present matches", sap_present2 == 1);
    TEST("sap_ref value matches", sap2->value && strcmp((const char*)sap2->value, "MyServer") == 0);

    printf("  req_id=%u  sap_present=%d  sap_ref=%s\n",
           req_id2, sap_present2, sap2->value ? (const char*)sap2->value : "(null)");
}

int main() {
    test_boolean_roundtrip();
    test_int8_roundtrip();
    test_int8u_roundtrip();
    test_associate_request();

    printf("\n=== %s ===\n", failed ? "SOME FAILED" : "ALL PASS");
    return failed;
}
