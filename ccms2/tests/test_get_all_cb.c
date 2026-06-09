#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "svc/directory/cms2_get_all_cb_values.h"
#include "data/common/cms2_acsi_class.h"

static int failed = 0;

#define TEST(name, expr) do { \
    if (!(expr)) { \
        printf("  FAIL: %s\n", name); \
        failed++; \
    } else { \
        printf("  PASS: %s\n", name); \
    } \
} while(0)

static void test_get_all_cb_values_request() {
    printf("=== GetAllCBValues-Request (reference 子结构) ===\n");

    cms2_get_all_cb_values_request_t pdu;
    cms2_get_all_cb_values_request_init(&pdu);

    /* Set values */
    *(uint16_t*)pdu.req_id = 42;

    /* reference — 通过指针访问子结构 */
    cms2_reference_choice_t *ref = (cms2_reference_choice_t*)pdu.reference;
    *(int32_t*)ref->choice = 0;  /* ldName */

    cms2_uint8_array_t *ld_name = (cms2_uint8_array_t*)ref->ld_name;
    const char *ld = "MyLD";
    memcpy(ld_name->value, ld, strlen(ld) + 1);
    ld_name->len = (int32_t)strlen(ld);

    *(int32_t*)pdu.acsi_class = CMS2_ACSI_CLASS_BRCB;
    *(int32_t*)pdu.ref_after_present = 0;

    /* Encode */
    uint8_t buf[256];
    int out_len = sizeof(buf);
    int rc = cms2_get_all_cb_values_request_encode(&pdu, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);
    printf("  encoded %d bytes\n", out_len);

    /* Decode */
    cms2_get_all_cb_values_request_t pdu2;
    cms2_get_all_cb_values_request_init(&pdu2);

    rc = cms2_get_all_cb_values_request_decode(&pdu2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);

    uint16_t req_id2 = *(uint16_t*)pdu2.req_id;
    int32_t acsi2 = *(int32_t*)pdu2.acsi_class;
    int32_t ref_after_present2 = *(int32_t*)pdu2.ref_after_present;

    /* 通过 reference 指针读子结构 */
    cms2_reference_choice_t *ref2 = (cms2_reference_choice_t*)pdu2.reference;
    int32_t ref_choice2 = ref2->choice ? *(int32_t*)ref2->choice : -1;
    cms2_uint8_array_t *ld2 = (cms2_uint8_array_t*)ref2->ld_name;

    TEST("req_id matches", req_id2 == 42);
    TEST("ref_choice == ldName", ref_choice2 == 0);
    TEST("ld_name matches", ld2->value && strcmp((const char*)ld2->value, "MyLD") == 0);
    TEST("acsi_class == BRCB", acsi2 == CMS2_ACSI_CLASS_BRCB);
    TEST("ref_after_present == 0", ref_after_present2 == 0);

    printf("  req_id=%u ref_choice=%d acsi=%d ld_name=%s\n",
           req_id2, ref_choice2, acsi2,
           ld2->value ? (const char*)ld2->value : "(null)");
}

static void test_get_all_cb_request_with_ln_ref() {
    printf("\n=== GetAllCBValues-Request (lnReference + with refAfter) ===\n");

    cms2_get_all_cb_values_request_t pdu;
    cms2_get_all_cb_values_request_init(&pdu);

    *(uint16_t*)pdu.req_id = 99;

    cms2_reference_choice_t *ref = (cms2_reference_choice_t*)pdu.reference;
    *(int32_t*)ref->choice = 1;  /* lnReference */

    cms2_uint8_array_t *ln_ref = (cms2_uint8_array_t*)ref->ln_reference;
    const char *ln = "MyLD/MyLN.MyData";
    memcpy(ln_ref->value, ln, strlen(ln) + 1);
    ln_ref->len = (int32_t)strlen(ln);

    *(int32_t*)pdu.acsi_class = CMS2_ACSI_CLASS_URCB;
    *(int32_t*)pdu.ref_after_present = 1;

    cms2_uint8_array_t *after = (cms2_uint8_array_t*)pdu.ref_after;
    const char *after_str = "NextRef";
    memcpy(after->value, after_str, strlen(after_str) + 1);
    after->len = (int32_t)strlen(after_str);

    /* Encode */
    uint8_t buf[256];
    int out_len = sizeof(buf);
    int rc = cms2_get_all_cb_values_request_encode(&pdu, buf, &out_len);
    TEST("encode ok", rc == CMS2_OK && out_len > 0);
    printf("  encoded %d bytes\n", out_len);

    /* Decode */
    cms2_get_all_cb_values_request_t pdu2;
    cms2_get_all_cb_values_request_init(&pdu2);
    rc = cms2_get_all_cb_values_request_decode(&pdu2, buf, out_len);
    TEST("decode ok", rc == CMS2_OK);

    uint16_t req_id2 = *(uint16_t*)pdu2.req_id;
    int32_t acsi2 = *(int32_t*)pdu2.acsi_class;
    int32_t ref_after_present2 = *(int32_t*)pdu2.ref_after_present;

    cms2_reference_choice_t *ref2 = (cms2_reference_choice_t*)pdu2.reference;
    int32_t ref_choice2 = ref2->choice ? *(int32_t*)ref2->choice : -1;
    cms2_uint8_array_t *ln2 = (cms2_uint8_array_t*)ref2->ln_reference;
    cms2_uint8_array_t *after2 = (cms2_uint8_array_t*)pdu2.ref_after;

    TEST("req_id matches", req_id2 == 99);
    TEST("ref_choice == lnReference", ref_choice2 == 1);
    TEST("ln_ref matches", ln2->value && strcmp((const char*)ln2->value, ln) == 0);
    TEST("acsi_class == URCB", acsi2 == CMS2_ACSI_CLASS_URCB);
    TEST("ref_after_present == 1", ref_after_present2 == 1);
    TEST("ref_after matches", after2->value && strcmp((const char*)after2->value, after_str) == 0);

    printf("  req_id=%u ref_choice=%d acsi=%d ln_ref=%s after=%s\n",
           req_id2, ref_choice2, acsi2,
           ln2->value ? (const char*)ln2->value : "(null)",
           after2->value ? (const char*)after2->value : "(null)");
}

int main() {
    test_get_all_cb_values_request();
    test_get_all_cb_request_with_ln_ref();

    printf("\n=== %s ===\n", failed ? "SOME FAILED" : "ALL PASS");
    return failed;
}
