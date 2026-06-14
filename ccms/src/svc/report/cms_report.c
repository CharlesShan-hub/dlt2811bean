#include "svc/report/cms_report.h"
#include "svc/other/cms_req_id.h"
#include "svc/report/cms_report_entry.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

#define RPT_ID_MAX 129

int cms_report_encode(const cms_report_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;

    /* rptID — VisibleString129 */
    if (!pdu->rpt_id) return CMS_ERR; err = cms_visible_string_encode_stream(&s, pdu->rpt_id, RPT_ID_MAX); if (err) return err;

    /* optFlds — RCBOptFlds */
    if (!pdu->opt_flds) return CMS_ERR; err = cms_rcb_opt_flds_encode_stream(&s, pdu->opt_flds); if (err) return err;

    /* sqNum — INT16U OPTIONAL */
    { int p = (pdu->sq_num_present && pdu->sq_num_present->value) && pdu->sq_num; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int16u_encode_stream(&s,pdu->sq_num);if(err)return err;} }

    /* subSeqNum — INT16U OPTIONAL */
    { int p = (pdu->sub_seq_num_present && pdu->sub_seq_num_present->value) && pdu->sub_seq_num; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int16u_encode_stream(&s,pdu->sub_seq_num);if(err)return err;} }

    /* moreSegmentsFollow — BOOLEAN OPTIONAL */
    { int p = (pdu->more_segments_follow_present && pdu->more_segments_follow_present->value); cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(&s,pdu->more_segments_follow);if(err)return err;} }

    /* dataSet — ObjectReference OPTIONAL */
    { int p = (pdu->data_set_present && pdu->data_set_present->value) && pdu->data_set; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(&s,pdu->data_set);if(err)return err;} }

    /* bufOvfl — BOOLEAN OPTIONAL */
    { int p = (pdu->buf_ovfl_present && pdu->buf_ovfl_present->value); cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(&s,pdu->buf_ovfl);if(err)return err;} }

    /* confRev — INT32U OPTIONAL */
    { int p = (pdu->conf_rev_present && pdu->conf_rev_present->value) && pdu->conf_rev; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int32u_encode_stream(&s,pdu->conf_rev);if(err)return err;} }

    /* entry — ReportEntry */
    if (!pdu->entry) return CMS_ERR; err = cms_report_entry_encode_stream(&s, pdu->entry); if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}

int cms_report_decode(cms_report_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->rpt_id) return CMS_ERR; err = cms_visible_string_decode_stream(&s, pdu->rpt_id, RPT_ID_MAX); if (err) return err;
    if (!pdu->opt_flds) return CMS_ERR; err = cms_rcb_opt_flds_decode_stream(&s, pdu->opt_flds); if (err) return err;

    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->sq_num_present)pdu->sq_num_present->value=b.value; if(b.value&&pdu->sq_num){err=cms_int16u_decode_stream(&s,pdu->sq_num);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->sub_seq_num_present)pdu->sub_seq_num_present->value=b.value; if(b.value&&pdu->sub_seq_num){err=cms_int16u_decode_stream(&s,pdu->sub_seq_num);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->more_segments_follow_present)pdu->more_segments_follow_present->value=b.value; if(b.value&&pdu->more_segments_follow){err=cms_boolean_decode_stream(&s,pdu->more_segments_follow);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->data_set_present)pdu->data_set_present->value=b.value; if(b.value&&pdu->data_set){err=cms_object_reference_decode_stream(&s,pdu->data_set);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->buf_ovfl_present)pdu->buf_ovfl_present->value=b.value; if(b.value&&pdu->buf_ovfl){err=cms_boolean_decode_stream(&s,pdu->buf_ovfl);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->conf_rev_present)pdu->conf_rev_present->value=b.value; if(b.value&&pdu->conf_rev){err=cms_int32u_decode_stream(&s,pdu->conf_rev);if(err)return err;} }

    if (!pdu->entry) return CMS_ERR; err = cms_report_entry_decode_stream(&s, pdu->entry); if (err) return err;

    return CMS_OK;
}
