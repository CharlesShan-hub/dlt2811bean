#include "svc/other/cms_association_id.h"
#include "data/string/cms_octet_string.h"

int cms_association_id_encode_stream(per_stream_t *s, const cms_association_id_t *id) {
    return cms_octet_string_encode_stream(s, id, CMS_ASSOCIATION_ID_MAX);
}

int cms_association_id_decode_stream(per_stream_t *s, cms_association_id_t *id) {
    return cms_octet_string_decode_stream(s, id, CMS_ASSOCIATION_ID_MAX);
}

int cms_association_id_encode(const cms_association_id_t *id, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_association_id_encode_stream(&s, id);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_association_id_decode(cms_association_id_t *id, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_association_id_decode_stream(&s, id);
}
