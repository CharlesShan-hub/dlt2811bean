#include "data/common/cms_file_entry.h"
#include "data/basic/cms_string.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_file_entry_encode_stream(per_stream_t *s, const cms_file_entry_t *v){
    cms_visible_string_fixed_t fileName = {v->fileName.value, 129};
    int rc = cms_visible_string_fixed_encode_stream(s, &fileName);
    if (rc) return rc;
    rc = cms_int32u_encode_stream(s, &v->fileSize);
    if (rc) return rc;
    rc = cms_utc_time_encode_stream(s, &v->lastModified);
    if (rc) return rc;
    return cms_int32u_encode_stream(s, &v->checkSum);
}

int cms_file_entry_decode_stream(per_stream_t *s, cms_file_entry_t *v){
    cms_visible_string_fixed_t fileName = {v->fileName.value, 129};
    int rc = cms_visible_string_fixed_decode_stream(s, &fileName);
    if (rc) return rc;
    rc = cms_int32u_decode_stream(s, &v->fileSize);
    if (rc) return rc;
    rc = cms_utc_time_decode_stream(s, &v->lastModified);
    if (rc) return rc;
    return cms_int32u_decode_stream(s, &v->checkSum);
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_file_entry_encode(const cms_file_entry_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_file_entry_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_file_entry_decode(cms_file_entry_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_file_entry_decode_stream(&r, v);
}
