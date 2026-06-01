#ifndef CMSPER_BIT_STRING_H
#define CMSPER_BIT_STRING_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

per_error_t per_encode_bit_string_fixed(per_stream_t *s, const uint8_t *data, int fixed_nbits);
per_error_t per_decode_bit_string_fixed(per_stream_t *s, uint8_t *out, int fixed_nbits);

per_error_t per_encode_bit_string(per_stream_t *s, const uint8_t *data, int nbits, int ub);
per_error_t per_decode_bit_string(per_stream_t *s, uint8_t *out, int *out_nbits, int ub);

#ifdef __cplusplus
}
#endif

#endif
