#ifndef DATA_FC_CMS_FUNCTIONAL_CONSTRAINT_H
#define DATA_FC_CMS_FUNCTIONAL_CONSTRAINT_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * FunctionalConstraint ::= VisibleString (SIZE(2))
 * ============================================================
 */
typedef cms_uint8_array_t cms_functional_constraint_t;
#define CMS_FUNCTIONAL_CONSTRAINT_LEN 2

CMS_EXPORT int cms_functional_constraint_encode(const cms_functional_constraint_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_functional_constraint_decode(cms_functional_constraint_t *v, const uint8_t *in_buf, int in_len);
int cms_functional_constraint_encode_stream(per_stream_t *s, const cms_functional_constraint_t *v);
int cms_functional_constraint_decode_stream(per_stream_t *s, cms_functional_constraint_t *v);

#ifdef __cplusplus
}
#endif

#endif
