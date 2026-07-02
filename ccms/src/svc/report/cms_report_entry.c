#include "svc/report/cms_report_entry.h"
#include "svc/report/cms_report_data_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

int cms_report_entry_encode_stream(per_stream_t *s, const cms_report_entry_t *v) {
    if (!v || !v->entry_data) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: timeOfEntry, entryID) */
    bool opt_present[2] = {
        (v->time_of_entry_present && v->time_of_entry_present->value) && v->time_of_entry,
        (v->entry_id_present && v->entry_id_present->value) && v->entry_id
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 2);
    if (err) return err;

    /* 1. timeOfEntry — EntryTime OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_entry_time_encode_stream(s, v->time_of_entry);
        if (err) return err;
    }

    /* 2. entryID — EntryID OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_entry_id_encode_stream(s, v->entry_id);
        if (err) return err;
    }

    /* 3. entryData — SEQUENCE OF ReportDataEntry */
    {
        uint32_t cnt = (uint32_t)v->entry_data->count;
        per_error_t perr = per_encode_length(s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_report_data_entry_t *e = (cms_report_data_entry_t*)v->entry_data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_report_data_entry_encode_stream(s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_report_entry_decode_stream(per_stream_t *s, cms_report_entry_t *v) {
    if (!v || !v->entry_data) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (2 fields: timeOfEntry, entryID) */
    bool opt_present[2];
    err = (int)per_decode_optional_bitmap(s, opt_present, 2);
    if (err) return err;
    if (v->time_of_entry_present) v->time_of_entry_present->value = opt_present[0];
    if (v->entry_id_present) v->entry_id_present->value = opt_present[1];

    /* 1. timeOfEntry OPTIONAL */
    if (opt_present[0] && v->time_of_entry) {
        err = cms_entry_time_decode_stream(s, v->time_of_entry);
        if (err) return err;
    }

    /* 2. entryID OPTIONAL */
    if (opt_present[1] && v->entry_id) {
        err = cms_entry_id_decode_stream(s, v->entry_id);
        if (err) return err;
    }

    /* 3. entryData */
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(s, &cnt);
        if (perr) return CMS_ERR;
        if (v->entry_data->count < (int32_t)cnt) {
            v->entry_data->count = (int32_t)cnt;
            return CMS_RETRY;
        }
        v->entry_data->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_report_data_entry_t *e = (cms_report_data_entry_t*)v->entry_data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_report_data_entry_decode_stream(s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}
