#ifndef DATA_CONTROL_CMS_CHECK_H
#define DATA_CONTROL_CMS_CHECK_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Check ::= BIT STRING { syncheck(0), interlock-check(1) } (SIZE(2))
 * ============================================================
 */
typedef struct {
    cms_boolean_t syncheck;
    cms_boolean_t interlock_check;
} cms_check_t;

CMS_EXPORT int cms_check_encode(const cms_check_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_check_decode(cms_check_t *v, const uint8_t *in_buf, int in_len);
int cms_check_encode_stream(per_stream_t *s, const cms_check_t *v);
int cms_check_decode_stream(per_stream_t *s, cms_check_t *v);

#ifdef __cplusplus
}
#endif

#endif
