#include "svc/other/cms_association_id.h"
#include "data/basic/cms_string.h"

int cms_association_id_encode_stream(per_stream_t *s, const cms_association_id_t *id)
{
    cms_octet_string_var_t var;
    var.value   = id->value;
    var.len     = id->len;
    var.max_len = CMS_ASSOCIATION_ID_MAX;
    return cms_octet_string_var_encode_stream(s, &var);
}

int cms_association_id_decode_stream(per_stream_t *s, cms_association_id_t *id)
{
    cms_octet_string_var_t var;
    var.value   = id->value;
    var.len     = id->len;
    var.max_len = CMS_ASSOCIATION_ID_MAX;
    int rc = cms_octet_string_var_decode_stream(s, &var);
    id->len = var.len;
    return rc;
}

CMS_EXPORT int cms_association_id_encode(const cms_association_id_t *id, uint8_t *out_buf, int *out_len)
{
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len);
    int rc = cms_association_id_encode_stream(&w, id);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_association_id_decode(cms_association_id_t *id, const uint8_t *in_buf, int in_len)
{
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len);
    return cms_association_id_decode_stream(&r, id);
}
