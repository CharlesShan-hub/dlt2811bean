#ifndef CMS_SET_GO_CB_ENTRY_H
#define CMS_SET_GO_CB_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetGoCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     goEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *     goID        [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * Used by SetGoCBValues request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t *reference;
    cms_boolean_t          *go_ena_present;
    cms_boolean_t          *go_ena;
    cms_boolean_t          *go_id_present;
    cms_uint8_array_t      *go_id;
    cms_boolean_t          *dat_set_present;
    cms_object_reference_t *dat_set;
} cms_set_go_cb_entry_t;

int cms_set_go_cb_entry_encode_stream(per_stream_t *s, const cms_set_go_cb_entry_t *v);
int cms_set_go_cb_entry_decode_stream(per_stream_t *s, cms_set_go_cb_entry_t *v);

#ifdef __cplusplus
}
#endif

#endif
