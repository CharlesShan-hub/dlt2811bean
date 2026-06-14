#include "svc/test/cms_test.h"

int cms_test_request_encode(const cms_test_request_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);
    if(!pdu->req_id)return CMS_ERR;int err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_test_request_decode(cms_test_request_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);
    if(!pdu->req_id)return CMS_ERR;return cms_req_id_decode_stream(&s,pdu->req_id);
}
int cms_test_response_encode(const cms_test_response_t*pdu,uint8_t*out_buf,int*out_len){return cms_test_request_encode(pdu,out_buf,out_len);}
int cms_test_response_decode(cms_test_response_t*pdu,const uint8_t*in_buf,int in_len){return cms_test_request_decode(pdu,in_buf,in_len);}
int cms_test_error_encode(const cms_test_error_t*pdu,uint8_t*out_buf,int*out_len){return cms_test_request_encode(pdu,out_buf,out_len);}
int cms_test_error_decode(cms_test_error_t*pdu,const uint8_t*in_buf,int in_len){return cms_test_request_decode(pdu,in_buf,in_len);}
