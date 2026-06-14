#ifndef CMS_SET_MSVCB_ENTRY_H
#define CMS_SET_MSVCB_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_smp_mod.h"
#include "data/block/cms_msvcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetMSVCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     svEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *     msvID       [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *     smpMod      [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate     [6] IMPLICIT INT16U OPTIONAL,
 *     optFlds     [7] IMPLICIT MSVCBOptFlds OPTIONAL
 * }
 *
 * Used by SetMSVCBValues request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t *reference;
    cms_boolean_t          *sv_ena_present;
    cms_boolean_t          *sv_ena;
    cms_boolean_t          *msv_id_present;
    cms_uint8_array_t      *msv_id;
    cms_boolean_t          *dat_set_present;
    cms_object_reference_t *dat_set;
    cms_boolean_t          *smp_mod_present;
    cms_smp_mod_t          *smp_mod;
    cms_boolean_t          *smp_rate_present;
    cms_int16u_t           *smp_rate;
    cms_boolean_t          *opt_flds_present;
    cms_msvcb_opt_flds_t   *opt_flds;
} cms_set_msvcb_entry_t;

int cms_set_msvcb_entry_encode_stream(per_stream_t *s, const cms_set_msvcb_entry_t *v);
int cms_set_msvcb_entry_decode_stream(per_stream_t *s, cms_set_msvcb_entry_t *v);

#ifdef __cplusplus
}
#endif

#endif
