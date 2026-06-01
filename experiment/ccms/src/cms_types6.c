#include "cms_types6.h"
#include "per_stream.h"
#include "per_integer.h"
#include "per_bit_string.h"
#include <string.h>

CMS_EXPORT int cms_lcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 1);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_lcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 1);
    return CMS_OK;
}

/* 7.6.6 MsvcbOptFlds */
CMS_EXPORT int cms_msvcb_opt_flds_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 5);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_msvcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 5);
    return CMS_OK;
}

CMS_EXPORT int cms_rcb_opt_flds_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 10);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_rcb_opt_flds_decode(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 10);
    return CMS_OK;
}

/* 7.6.3 ReasonCode */
CMS_EXPORT int cms_reason_code_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 7);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_reason_code_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 7);
    return CMS_OK;
}

/* 7.6.2 TriggerConditions */
CMS_EXPORT int cms_trigger_conditions_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 6);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_trigger_conditions_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 6);
    return CMS_OK;
}

CMS_EXPORT int cms_smp_mod_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 2);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_smp_mod_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 2);
    *value = (int)tmp;
    return CMS_OK;
}
