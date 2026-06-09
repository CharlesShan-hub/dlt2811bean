#ifndef CMS2_GET_ALL_CB_VALUES_H
#define CMS2_GET_ALL_CB_VALUES_H

#include "cms_core.h"
#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_choice.h"
#include "svc/directory/cms2_reference_choice.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetAllCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT CHOICE {           ← 封装成独立 struct
 *         ldName         [0] IMPLICIT ObjectName,
 *         lnReference    [1] IMPLICIT ObjectReference
 *     },
 *     acsiClass       [1] IMPLICIT ACSIClass,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * All-pointer layout:
 *   req_id             → uint16_t*
 *   reference          → cms2_reference_choice_t*     ← 只存一个指针
 *   acsi_class         → int32_t*
 *   ref_after_present  → int32_t*
 *   ref_after          → cms2_uint8_array_t*
 *
 * sizeof = 5 * 8 = 40
 * ============================================================
 */
typedef struct {
    void *req_id;             /* uint16_t* */
    void *reference;          /* cms2_reference_choice_t* — 内部自管 choice + ld_name + ln_ref */
    void *acsi_class;         /* int32_t* (ACSIClass 0..10) */
    void *ref_after_present;  /* int32_t* (0/1) */
    void *ref_after;          /* cms2_uint8_array_t* (ObjectReference, max 129) */
} cms2_get_all_cb_values_request_t;

CMS2_EXPORT int cms2_get_all_cb_values_request_encode(
    const cms2_get_all_cb_values_request_t *pdu,
    uint8_t *out_buf, int *out_len);

CMS2_EXPORT int cms2_get_all_cb_values_request_decode(
    cms2_get_all_cb_values_request_t *pdu,
    const uint8_t *in_buf, int in_len);

CMS2_EXPORT void cms2_get_all_cb_values_request_init(
    cms2_get_all_cb_values_request_t *pdu);

#ifdef __cplusplus
}
#endif

#endif
