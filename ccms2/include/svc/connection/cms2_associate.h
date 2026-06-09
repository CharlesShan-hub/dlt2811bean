#ifndef CMS2_ASSOCIATE_H
#define CMS2_ASSOCIATE_H

#include "cms_core.h"
#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/basic/cms2_basic.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Associate-RequestPDU (all-pointer version)
 *
 * C struct (全指针模式):
 *
 * typedef struct {
 *     cms2_int16u_t     *req_id;        // ptr to { uint16_t value; }
 *     int               *sap_ref_present;// ptr to int (boolean)
 *     cms2_uint8_array_t *sap_ref;      // ptr to { uint8_t *; int32_t; }
 *     int               *auth_present;  // ptr to int (boolean)
 *     void              *auth_param;    // ptr to AuthenticationParameter (TODO)
 * } cms2_associate_request_t;
 *
 * sizeof = 5 * 8 = 40 (all pointers)
 * ============================================================
 */
typedef struct {
    void *req_id;           /* cms2_int16u_t * */
    void *sap_ref_present;  /* int * */
    void *sap_ref;          /* cms2_uint8_array_t * */
    void *auth_present;     /* int * */
    void *auth_param;       /* void * (AuthenticationParameter) */
} cms2_associate_request_t;
/* sizeof = 5 * 8 = 40 */

CMS2_EXPORT int cms2_associate_request_encode(
    const cms2_associate_request_t *pdu,
    uint8_t *out_buf, int *out_len);

CMS2_EXPORT int cms2_associate_request_decode(
    cms2_associate_request_t *pdu,
    const uint8_t *in_buf, int in_len);

/*
 * ============================================================
 * Helper: alloc and init an Associate-Request with default values
 * ============================================================
 */
CMS2_EXPORT void cms2_associate_request_init(cms2_associate_request_t *pdu);

#ifdef __cplusplus
}
#endif

#endif
