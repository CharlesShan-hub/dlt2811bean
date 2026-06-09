#ifndef CMS_BLOCK_SMP_MOD_H
#define CMS_BLOCK_SMP_MOD_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * SmpMod ::= INTEGER (0..2)  —  7.6.7
 * PER: constrained integer, encoded as Int8 (-128..127)
 */

#define CMS_SMP_MOD_SAMPLES_PER_NOMINAL_PERIOD  0
#define CMS_SMP_MOD_SAMPLES_PER_SECOND          1
#define CMS_SMP_MOD_SECONDS_PER_SAMPLE          2

typedef cms_enumerated_t cms_smp_mod_t;

int cms_smp_mod_encode_stream(per_stream_t *s, const void *ptr);
int cms_smp_mod_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_smp_mod_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_smp_mod_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
