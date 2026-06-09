#ifndef CMS2_REFERENCE_CHOICE_H
#define CMS2_REFERENCE_CHOICE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * CHOICE { ldName [0] ObjectName, lnReference [1] ObjectReference }
 *
 * All-pointer layout (sizeof = 24):
 *   [0] choice:     int32_t*     — 0=ldName, 1=lnReference
 *   [8] ld_name:    void*        — cms2_uint8_array_t*
 *   [16] ln_ref:    void*        — cms2_uint8_array_t*
 */
typedef struct {
    void *choice;        /* int32_t* */
    void *ld_name;       /* cms2_uint8_array_t* (ObjectName) */
    void *ln_reference;  /* cms2_uint8_array_t* (ObjectReference) */
} cms2_reference_choice_t;

#define CMS2_REF_CHOICE_LD_NAME      0
#define CMS2_REF_CHOICE_LN_REFERENCE 1

/*
 * Encode the CHOICE { ldName, lnReference } into a PER stream.
 * In all-pointer mode, this is called by the parent PDU's encoder.
 */
int cms2_reference_choice_encode(per_stream_t *s, const cms2_reference_choice_t *c);
int cms2_reference_choice_decode(per_stream_t *s, cms2_reference_choice_t *c);

#ifdef __cplusplus
}
#endif

#endif
