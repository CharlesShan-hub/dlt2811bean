#include "svc/other/cms_req_id.h"
#include "data/scalar/cms_int16u.h"

int cms_req_id_encode_stream(per_stream_t *s, const cms_req_id_t *v) {
    return cms_int16u_encode_stream(s, v);
}

int cms_req_id_decode_stream(per_stream_t *s, cms_req_id_t *v) {
    return cms_int16u_decode_stream(s, v);
}

int cms_req_id_encode(const cms_req_id_t *v, uint8_t *out_buf, int *out_len) {
    return cms_int16u_encode(v, out_buf, out_len);
}

int cms_req_id_decode(cms_req_id_t *v, const uint8_t *in_buf, int in_len) {
    return cms_int16u_decode(v, in_buf, in_len);
}
