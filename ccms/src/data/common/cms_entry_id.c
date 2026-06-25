#include "data/common/cms_entry_id.h"
#include <string.h>

int cms_entry_id_encode_stream(per_stream_t *s, const void *ptr) {
    const uint8_t *vptr = *(const uint8_t *const*)ptr;
    if (!vptr) return CMS_ERR;
    return cms_octet_string_fixed_encode_stream(s, vptr, CMS_ENTRY_ID_LEN);
}

int cms_entry_id_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t *vptr = *(uint8_t **)ptr;
    if (!vptr) return CMS_ERR;
    int err = cms_octet_string_fixed_decode_stream(s, vptr, CMS_ENTRY_ID_LEN);
    if (err) return err;
    *(int32_t*)((uint8_t*)ptr + 8) = CMS_ENTRY_ID_LEN;
    return CMS_OK;
}

int cms_entry_id_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_entry_id_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_entry_id_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_entry_id_decode_stream(&s, ptr);
}
