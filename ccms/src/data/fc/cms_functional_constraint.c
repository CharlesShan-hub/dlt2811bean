#include "data/fc/cms_functional_constraint.h"
#include "cms_types.h"
#include "data/string/cms_visible_string.h"
#include <string.h>

/*
 * FC int → 2-char mapping (same ordinal as Java FunctionalConstraint enum)
 */
static const char fc_codes[][3] = {
    "ST",   /* 0 */
    "MX",   /* 1 */
    "SP",   /* 2 */
    "SV",   /* 3 */
    "CF",   /* 4 */
    "DC",   /* 5 */
    "SG",   /* 6 */
    "SE",   /* 7 */
    "SR",   /* 8 */
    "OR",   /* 9 */
    "BL",   /* 10 */
    "EX",   /* 11 */
    "XX"    /* 12 */
};

#define FC_COUNT (sizeof(fc_codes) / sizeof(fc_codes[0]))
#define FC_FIXED_LEN 2

static int fc_code_to_int(const uint8_t *code)
{
    for (unsigned i = 0; i < FC_COUNT; i++) {
        if (code[0] == fc_codes[i][0] && code[1] == fc_codes[i][1])
            return (int)i;
    }
    return CMS_ERR;
}

int cms_functional_constraint_encode_stream(per_stream_t *s, const void *ptr)
{
    const cms_functional_constraint_t *v = (const cms_functional_constraint_t*)ptr;
    cms_uint8_array_t arr;
    uint8_t buf[2];

    if (v->value < 0 || (unsigned)v->value >= FC_COUNT)
        return CMS_ERR;

    buf[0] = (uint8_t)fc_codes[v->value][0];
    buf[1] = (uint8_t)fc_codes[v->value][1];
    arr.value = buf;
    arr.len = FC_FIXED_LEN;

    return cms_visible_string_encode_stream_fixed(s, &arr, FC_FIXED_LEN);
}

int cms_functional_constraint_decode_stream(per_stream_t *s, void *ptr)
{
    cms_uint8_array_t arr;
    uint8_t buf[FC_FIXED_LEN + 1]; /* +1 for null terminator */
    int idx;

    arr.value = buf;
    arr.len = 0;

    int err = cms_visible_string_decode_stream_fixed(s, &arr, FC_FIXED_LEN);
    if (err) return err;

    idx = fc_code_to_int(arr.value);
    if (idx < 0) return CMS_ERR;

    if (ptr) ((cms_functional_constraint_t*)ptr)->value = idx;
    return CMS_OK;
}

int cms_functional_constraint_encode(const void *ptr, uint8_t **out_buf, size_t *out_len)
{
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_functional_constraint_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_functional_constraint_decode(void *ptr, const uint8_t *in_buf, int in_len)
{
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_functional_constraint_decode_stream(&s, ptr);
}
