#include "svc/other/cms_association_id.h"
#include "data/basic/cms_string.h"
#include "per/cms_stream.h"

int cms_association_id_encode_stream(per_stream_t *s, const cms_association_id_t *id)
{
    return cms_octet_string_encode_stream(s, id->data, id->len, CMS_ASSOCIATION_ID_MAX);
}

int cms_association_id_decode_stream(per_stream_t *s, cms_association_id_t *id)
{
    return cms_octet_string_decode_stream(s, id->data, &id->len, CMS_ASSOCIATION_ID_MAX);
}

CMS_EXPORT int cms_association_id_encode(const cms_association_id_t *id, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    int rc = (int)cms_octet_string_encode_stream(&w, id->data, id->len, CMS_ASSOCIATION_ID_MAX);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_association_id_decode(const uint8_t *in_buf, int in_len, cms_association_id_t *id)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return (int)cms_octet_string_decode_stream(&r, id->data, &id->len, CMS_ASSOCIATION_ID_MAX);
}
