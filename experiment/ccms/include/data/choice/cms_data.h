#ifndef DATA_CHOICE_CMS_DATA_H
#define DATA_CHOICE_CMS_DATA_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_data_encode(int choice, int64_t int_val, double float_val, const char *str_val, const uint8_t *bytes_val, int bytes_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_data_decode(const uint8_t *in_buf, int in_len, int *choice, int64_t *int_val, double *float_val, char *str_val, int *str_cap, uint8_t *bytes_val, int *bytes_cap);
int cms_data_encode_stream(per_stream_t *s, int choice, int64_t int_val, double float_val, const char *str_val, const uint8_t *bytes_val, int bytes_len);
int cms_data_decode_stream(per_stream_t *s, int *choice, int64_t *int_val, double *float_val, char *str_val, int *str_cap, uint8_t *bytes_val, int *bytes_cap);

#ifdef __cplusplus
}
#endif

#endif
