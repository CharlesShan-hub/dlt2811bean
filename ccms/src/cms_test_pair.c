#include "cms_test_pair.h"
#include "per/cms_stream.h"

CMS_EXPORT int cms_test_pair_encode(const cms_test_pair_t *v, uint8_t *out_buf, int *out_len){
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len);
    int rc;
    rc = cms_int32_encode_stream(&w, &v->a);
    if (rc) return rc;
    rc = cms_int32_encode_stream(&w, &v->b);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_test_pair_decode(cms_test_pair_t *v, const uint8_t *in_buf, int in_len){
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len);
    int rc;
    rc = cms_int32_decode_stream(&r, &v->a);
    if (rc) return rc;
    rc = cms_int32_decode_stream(&r, &v->b);
    if (rc) return rc;
    return CMS_OK;
}
