#include "svc/log/cms_log_entry.h"
#include "svc/log/cms_log_data_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "per/cms_integer.h"

int cms_log_entry_encode_stream(per_stream_t *s, const cms_log_entry_t *v) {
    if (!v || !v->time_of_entry || !v->entry_id || !v->entry_data) return CMS_ERR;
    int err;

    /* 1. timeOfEntry — EntryTime */
    err = cms_entry_time_encode_stream(s, v->time_of_entry);
    if (err) return err;

    /* 2. entryID — EntryID */
    err = cms_entry_id_encode_stream(s, v->entry_id);
    if (err) return err;

    /* 3. entryData — SEQUENCE OF LogDataEntry */
    {
        uint32_t cnt = (uint32_t)v->entry_data->count;
        per_error_t perr = per_encode_length(s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_log_data_entry_t *e = (cms_log_data_entry_t*)v->entry_data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_log_data_entry_encode_stream(s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_log_entry_decode_stream(per_stream_t *s, cms_log_entry_t *v) {
    if (!v || !v->time_of_entry || !v->entry_id || !v->entry_data) return CMS_ERR;
    int err;

    /* 1. timeOfEntry */
    err = cms_entry_time_decode_stream(s, v->time_of_entry);
    if (err) return err;

    /* 2. entryID */
    err = cms_entry_id_decode_stream(s, v->entry_id);
    if (err) return err;

    /* 3. entryData */
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(s, &cnt);
        if (perr) return CMS_ERR;
        v->entry_data->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_log_data_entry_t *e = (cms_log_data_entry_t*)v->entry_data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_log_data_entry_decode_stream(s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}
