package com.ysh.jcms.core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * jcms FFI — ccms dll bridge
 */
public class NativeBridge {

    private interface Lib extends Library {
        int cms_boolean_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_boolean_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int8_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int8u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int16_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int16u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int24u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int24u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int32u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int64u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_float32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float32_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_float64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float64_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_enumerated_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_enumerated_decode(Pointer v, byte[] inBuf, int inLen);

        // string / alias types
        int cms_object_name_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_object_name_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_object_reference_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_object_reference_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_sub_reference_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_sub_reference_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_entry_id_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_entry_id_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_functional_constraint_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_functional_constraint_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_association_id_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_association_id_decode(Pointer v, byte[] inBuf, int inLen);

        // time
        int cms_time_quality_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_quality_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_utc_time_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_utc_time_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_binary_time_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_binary_time_decode(Pointer v, byte[] inBuf, int inLen);

        // common containers
        int cms_quality_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_quality_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_phy_com_addr_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_phy_com_addr_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_file_entry_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_file_entry_decode(Pointer v, byte[] inBuf, int inLen);

        // control
        int cms_originator_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_originator_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_check_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_check_decode(Pointer v, byte[] inBuf, int inLen);

        // block BIT STRING containers
        int cms_trigger_conditions_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_trigger_conditions_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_reason_code_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_reason_code_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_rcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_rcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_lcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_lcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_msvcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_msvcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);

        // block SEQUENCE containers
        int cms_sgcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_sgcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_brcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_brcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_urcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_urcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_lcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_lcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_go_cb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_go_cb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_msvcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_msvcb_decode(Pointer v, byte[] inBuf, int inLen);

        // choice types
        int cms_data_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_data_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_data_definition_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_data_definition_decode(Pointer v, byte[] inBuf, int inLen);
        
        // 8.2 conntion
        int cms_abort_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_abort_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_associate_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_associate_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_associate_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_associate_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_associate_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_associate_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_release_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_release_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_release_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_release_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_release_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_release_error_decode(Pointer v, byte[] inBuf, int inLen);
        
        // 
        int cms_cancel_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_cancel_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_cancel_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_cancel_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_cancel_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_cancel_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_command_termination_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_command_termination_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_operate_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_operate_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_operate_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_operate_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_operate_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_operate_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_with_value_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_with_value_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_with_value_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_with_value_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_with_value_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_with_value_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_time_activated_operate_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_activated_operate_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_time_activated_operate_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_activated_operate_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_time_activated_operate_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_activated_operate_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_time_activated_operate_termination_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_activated_operate_termination_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_definition_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_definition_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_definition_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_definition_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_definition_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_definition_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_create_data_set_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_create_data_set_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_create_data_set_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_create_data_set_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_create_data_set_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_create_data_set_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_data_set_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_data_set_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_data_set_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_data_set_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_data_set_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_data_set_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_data_set_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_data_set_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_set_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_set_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_set_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_set_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_data_set_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_data_set_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_cb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_cb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_cb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_cb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_cb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_cb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_definition_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_definition_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_definition_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_definition_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_definition_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_definition_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_all_data_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_all_data_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_device_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_device_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_device_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_device_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_device_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_device_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_node_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_node_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_node_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_node_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_logical_node_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_logical_node_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_server_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_server_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_server_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_server_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_server_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_server_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_file_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_file_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_file_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_file_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_delete_file_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_delete_file_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_attribute_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_attribute_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_attribute_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_attribute_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_attribute_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_attribute_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_file_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_file_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_file_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_file_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_file_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_file_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_file_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_file_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_cb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_cb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_cb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_cb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_cb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_cb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_reference_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_reference_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_reference_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_reference_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_go_reference_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_go_reference_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_goose_element_number_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_goose_element_number_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_goose_element_number_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_goose_element_number_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_goose_element_number_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_goose_element_number_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_send_goose_message_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_send_goose_message_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_go_cb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_go_cb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_go_cb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_go_cb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_go_cb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_go_cb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_lcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_lcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_lcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_lcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_lcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_lcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_log_status_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_log_status_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_log_status_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_log_status_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_log_status_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_log_status_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_after_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_after_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_after_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_after_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_after_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_after_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_by_time_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_by_time_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_by_time_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_by_time_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_query_log_by_time_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_query_log_by_time_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_lcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_lcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_lcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_lcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_lcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_lcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_msvcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_msvcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_msvcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_msvcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_msvcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_msvcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_send_msv_message_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_send_msv_message_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_msvcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_msvcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_msvcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_msvcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_msvcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_msvcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_negotiate_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_negotiate_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_negotiate_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_negotiate_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_negotiate_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_negotiate_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_report_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_report_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_brcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_brcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_brcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_brcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_brcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_brcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_urcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_urcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_urcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_urcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_urcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_urcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_brcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_brcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_brcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_brcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_brcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_brcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_urcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_urcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_urcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_urcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_urcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_urcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_rpc_call_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_rpc_call_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_rpc_call_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_rpc_call_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_rpc_call_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_rpc_call_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_definition_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_definition_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_definition_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_definition_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_interface_definition_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_interface_definition_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_directory_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_directory_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_directory_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_directory_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_directory_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_directory_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_definition_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_definition_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_definition_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_definition_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_rpc_method_definition_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_rpc_method_definition_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_confirm_edit_sg_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_confirm_edit_sg_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_confirm_edit_sg_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_confirm_edit_sg_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_confirm_edit_sg_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_confirm_edit_sg_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_edit_sg_value_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_edit_sg_value_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_edit_sg_value_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_edit_sg_value_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_edit_sg_value_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_edit_sg_value_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_sgcb_values_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_sgcb_values_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_sgcb_values_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_sgcb_values_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_get_sgcb_values_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_get_sgcb_values_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_active_sg_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_active_sg_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_active_sg_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_active_sg_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_active_sg_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_active_sg_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_edit_sg_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_edit_sg_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_edit_sg_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_edit_sg_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_select_edit_sg_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_select_edit_sg_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_edit_sg_value_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_edit_sg_value_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_edit_sg_value_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_edit_sg_value_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_set_edit_sg_value_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_set_edit_sg_value_error_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_test_request_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_test_request_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_test_response_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_test_response_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_test_error_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_test_error_decode(Pointer v, byte[] inBuf, int inLen);
    }

    private static final Lib LIB = Native.load("ccms", Lib.class);

    private static byte[] encode(Pointer structPtr, Encoder fn) {
        byte[] buf = new byte[256];
        IntByReference outLen = new IntByReference(buf.length);
        int rc = fn.encode(structPtr, buf, outLen);
        if (rc != 0) throw new RuntimeException("encode failed: " + rc);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    private static void decode(Pointer structPtr, byte[] data, Decoder fn) {
        int rc = fn.decode(structPtr, data, data.length);
        if (rc != 0) throw new RuntimeException("decode failed: " + rc);
    }

    @FunctionalInterface private interface Encoder { int encode(Pointer v, byte[] buf, IntByReference outLen); }
    @FunctionalInterface private interface Decoder { int decode(Pointer v, byte[] buf, int len); }

    public static byte[] encodeBoolean(Pointer p) { return encode(p, LIB::cms_boolean_encode); }
    public static void decodeBoolean(Pointer p, byte[] d) { decode(p, d, LIB::cms_boolean_decode); }
    public static byte[] encodeInt8(Pointer p) { return encode(p, LIB::cms_int8_encode); }
    public static void decodeInt8(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8_decode); }
    public static byte[] encodeInt8U(Pointer p) { return encode(p, LIB::cms_int8u_encode); }
    public static void decodeInt8U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8u_decode); }
    public static byte[] encodeInt16(Pointer p) { return encode(p, LIB::cms_int16_encode); }
    public static void decodeInt16(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16_decode); }
    public static byte[] encodeInt16U(Pointer p) { return encode(p, LIB::cms_int16u_encode); }
    public static void decodeInt16U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16u_decode); }
    public static byte[] encodeInt24U(Pointer p) { return encode(p, LIB::cms_int24u_encode); }
    public static void decodeInt24U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int24u_decode); }
    public static byte[] encodeInt32(Pointer p) { return encode(p, LIB::cms_int32_encode); }
    public static void decodeInt32(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32_decode); }
    public static byte[] encodeInt32U(Pointer p) { return encode(p, LIB::cms_int32u_encode); }
    public static void decodeInt32U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32u_decode); }
    public static byte[] encodeInt64(Pointer p) { return encode(p, LIB::cms_int64_encode); }
    public static void decodeInt64(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64_decode); }
    public static byte[] encodeInt64U(Pointer p) { return encode(p, LIB::cms_int64u_encode); }
    public static void decodeInt64U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64u_decode); }
    public static byte[] encodeFloat32(Pointer p) { return encode(p, LIB::cms_float32_encode); }
    public static void decodeFloat32(Pointer p, byte[] d) { decode(p, d, LIB::cms_float32_decode); }
    public static byte[] encodeFloat64(Pointer p) { return encode(p, LIB::cms_float64_encode); }
    public static void decodeFloat64(Pointer p, byte[] d) { decode(p, d, LIB::cms_float64_decode); }
    public static byte[] encodeEnumerated(Pointer p) { return encode(p, LIB::cms_enumerated_encode); }
    public static void decodeEnumerated(Pointer p, byte[] d) { decode(p, d, LIB::cms_enumerated_decode); }

    // string / alias types
    public static byte[] encodeObjectName(Pointer p) { return encode(p, LIB::cms_object_name_encode); }
    public static void decodeObjectName(Pointer p, byte[] d) { decode(p, d, LIB::cms_object_name_decode); }
    public static byte[] encodeObjectReference(Pointer p) { return encode(p, LIB::cms_object_reference_encode); }
    public static void decodeObjectReference(Pointer p, byte[] d) { decode(p, d, LIB::cms_object_reference_decode); }
    public static byte[] encodeSubReference(Pointer p) { return encode(p, LIB::cms_sub_reference_encode); }
    public static void decodeSubReference(Pointer p, byte[] d) { decode(p, d, LIB::cms_sub_reference_decode); }
    public static byte[] encodeEntryId(Pointer p) { return encode(p, LIB::cms_entry_id_encode); }
    public static void decodeEntryId(Pointer p, byte[] d) { decode(p, d, LIB::cms_entry_id_decode); }
    public static byte[] encodeFunctionalConstraint(Pointer p) { return encode(p, LIB::cms_functional_constraint_encode); }
    public static void decodeFunctionalConstraint(Pointer p, byte[] d) { decode(p, d, LIB::cms_functional_constraint_decode); }
    public static byte[] encodeAssociationId(Pointer p) { return encode(p, LIB::cms_association_id_encode); }
    public static void decodeAssociationId(Pointer p, byte[] d) { decode(p, d, LIB::cms_association_id_decode); }

    // time
    public static byte[] encodeTimeQuality(Pointer p) { return encode(p, LIB::cms_time_quality_encode); }
    public static void decodeTimeQuality(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_quality_decode); }
    public static byte[] encodeUtcTime(Pointer p) { return encode(p, LIB::cms_utc_time_encode); }
    public static void decodeUtcTime(Pointer p, byte[] d) { decode(p, d, LIB::cms_utc_time_decode); }
    public static byte[] encodeBinaryTime(Pointer p) { return encode(p, LIB::cms_binary_time_encode); }
    public static void decodeBinaryTime(Pointer p, byte[] d) { decode(p, d, LIB::cms_binary_time_decode); }

    // common containers
    public static byte[] encodeQuality(Pointer p) { return encode(p, LIB::cms_quality_encode); }
    public static void decodeQuality(Pointer p, byte[] d) { decode(p, d, LIB::cms_quality_decode); }
    public static byte[] encodePhyComAddr(Pointer p) { return encode(p, LIB::cms_phy_com_addr_encode); }
    public static void decodePhyComAddr(Pointer p, byte[] d) { decode(p, d, LIB::cms_phy_com_addr_decode); }
    public static byte[] encodeFileEntry(Pointer p) { return encode(p, LIB::cms_file_entry_encode); }
    public static void decodeFileEntry(Pointer p, byte[] d) { decode(p, d, LIB::cms_file_entry_decode); }

    // control
    public static byte[] encodeOriginator(Pointer p) { return encode(p, LIB::cms_originator_encode); }
    public static void decodeOriginator(Pointer p, byte[] d) { decode(p, d, LIB::cms_originator_decode); }
    public static byte[] encodeCheck(Pointer p) { return encode(p, LIB::cms_check_encode); }
    public static void decodeCheck(Pointer p, byte[] d) { decode(p, d, LIB::cms_check_decode); }

    // block BIT STRING containers
    public static byte[] encodeTriggerConditions(Pointer p) { return encode(p, LIB::cms_trigger_conditions_encode); }
    public static void decodeTriggerConditions(Pointer p, byte[] d) { decode(p, d, LIB::cms_trigger_conditions_decode); }
    public static byte[] encodeReasonCode(Pointer p) { return encode(p, LIB::cms_reason_code_encode); }
    public static void decodeReasonCode(Pointer p, byte[] d) { decode(p, d, LIB::cms_reason_code_decode); }
    public static byte[] encodeRcbOptFlds(Pointer p) { return encode(p, LIB::cms_rcb_opt_flds_encode); }
    public static void decodeRcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_rcb_opt_flds_decode); }
    public static byte[] encodeLcbOptFlds(Pointer p) { return encode(p, LIB::cms_lcb_opt_flds_encode); }
    public static void decodeLcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_lcb_opt_flds_decode); }
    public static byte[] encodeMsvcbOptFlds(Pointer p) { return encode(p, LIB::cms_msvcb_opt_flds_encode); }
    public static void decodeMsvcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_msvcb_opt_flds_decode); }

    // block SEQUENCE containers
    public static byte[] encodeSgcb(Pointer p) { return encode(p, LIB::cms_sgcb_encode); }
    public static void decodeSgcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_sgcb_decode); }
    public static byte[] encodeBrcb(Pointer p) { return encode(p, LIB::cms_brcb_encode); }
    public static void decodeBrcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_brcb_decode); }
    public static byte[] encodeUrcb(Pointer p) { return encode(p, LIB::cms_urcb_encode); }
    public static void decodeUrcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_urcb_decode); }
    public static byte[] encodeLcb(Pointer p) { return encode(p, LIB::cms_lcb_encode); }
    public static void decodeLcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_lcb_decode); }
    public static byte[] encodeGoCb(Pointer p) { return encode(p, LIB::cms_go_cb_encode); }
    public static void decodeGoCb(Pointer p, byte[] d) { decode(p, d, LIB::cms_go_cb_decode); }
    public static byte[] encodeMsvcb(Pointer p) { return encode(p, LIB::cms_msvcb_encode); }
    public static void decodeMsvcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_msvcb_decode); }

    // choice types
    public static byte[] encodeData(Pointer p) { return encode(p, LIB::cms_data_encode); }
    public static void decodeData(Pointer p, byte[] d) { decode(p, d, LIB::cms_data_decode); }
    public static byte[] encodeDataDefinition(Pointer p) { return encode(p, LIB::cms_data_definition_encode); }
    public static void decodeDataDefinition(Pointer p, byte[] d) { decode(p, d, LIB::cms_data_definition_decode); }
    public static byte[] encodeAbort(Pointer p) { return encode(p, LIB::cms_abort_encode); }
    public static void decodeAbort(Pointer p, byte[] d) { decode(p, d, LIB::cms_abort_decode); }
    public static byte[] encodeAssociateRequest(Pointer p) { return encode(p, LIB::cms_associate_request_encode); }
    public static void decodeAssociateRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_associate_request_decode); }
    public static byte[] encodeAssociateResponse(Pointer p) { return encode(p, LIB::cms_associate_response_encode); }
    public static void decodeAssociateResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_associate_response_decode); }
    public static byte[] encodeAssociateError(Pointer p) { return encode(p, LIB::cms_associate_error_encode); }
    public static void decodeAssociateError(Pointer p, byte[] d) { decode(p, d, LIB::cms_associate_error_decode); }
    public static byte[] encodeReleaseRequest(Pointer p) { return encode(p, LIB::cms_release_request_encode); }
    public static void decodeReleaseRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_release_request_decode); }
    public static byte[] encodeReleaseResponse(Pointer p) { return encode(p, LIB::cms_release_response_encode); }
    public static void decodeReleaseResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_release_response_decode); }
    public static byte[] encodeReleaseError(Pointer p) { return encode(p, LIB::cms_release_error_encode); }
    public static void decodeReleaseError(Pointer p, byte[] d) { decode(p, d, LIB::cms_release_error_decode); }
    public static byte[] encodeCancelRequest(Pointer p) { return encode(p, LIB::cms_cancel_request_encode); }
    public static void decodeCancelRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_cancel_request_decode); }
    public static byte[] encodeCancelResponse(Pointer p) { return encode(p, LIB::cms_cancel_response_encode); }
    public static void decodeCancelResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_cancel_response_decode); }
    public static byte[] encodeCancelError(Pointer p) { return encode(p, LIB::cms_cancel_error_encode); }
    public static void decodeCancelError(Pointer p, byte[] d) { decode(p, d, LIB::cms_cancel_error_decode); }
    public static byte[] encodeCommandTermination(Pointer p) { return encode(p, LIB::cms_command_termination_encode); }
    public static void decodeCommandTermination(Pointer p, byte[] d) { decode(p, d, LIB::cms_command_termination_decode); }
    public static byte[] encodeOperateRequest(Pointer p) { return encode(p, LIB::cms_operate_request_encode); }
    public static void decodeOperateRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_operate_request_decode); }
    public static byte[] encodeOperateResponse(Pointer p) { return encode(p, LIB::cms_operate_response_encode); }
    public static void decodeOperateResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_operate_response_decode); }
    public static byte[] encodeOperateError(Pointer p) { return encode(p, LIB::cms_operate_error_encode); }
    public static void decodeOperateError(Pointer p, byte[] d) { decode(p, d, LIB::cms_operate_error_decode); }
    public static byte[] encodeSelectRequest(Pointer p) { return encode(p, LIB::cms_select_request_encode); }
    public static void decodeSelectRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_request_decode); }
    public static byte[] encodeSelectResponse(Pointer p) { return encode(p, LIB::cms_select_response_encode); }
    public static void decodeSelectResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_response_decode); }
    public static byte[] encodeSelectError(Pointer p) { return encode(p, LIB::cms_select_error_encode); }
    public static void decodeSelectError(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_error_decode); }
    public static byte[] encodeSelectWithValueRequest(Pointer p) { return encode(p, LIB::cms_select_with_value_request_encode); }
    public static void decodeSelectWithValueRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_with_value_request_decode); }
    public static byte[] encodeSelectWithValueResponse(Pointer p) { return encode(p, LIB::cms_select_with_value_response_encode); }
    public static void decodeSelectWithValueResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_with_value_response_decode); }
    public static byte[] encodeSelectWithValueError(Pointer p) { return encode(p, LIB::cms_select_with_value_error_encode); }
    public static void decodeSelectWithValueError(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_with_value_error_decode); }
    public static byte[] encodeTimeActivatedOperateRequest(Pointer p) { return encode(p, LIB::cms_time_activated_operate_request_encode); }
    public static void decodeTimeActivatedOperateRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_activated_operate_request_decode); }
    public static byte[] encodeTimeActivatedOperateResponse(Pointer p) { return encode(p, LIB::cms_time_activated_operate_response_encode); }
    public static void decodeTimeActivatedOperateResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_activated_operate_response_decode); }
    public static byte[] encodeTimeActivatedOperateError(Pointer p) { return encode(p, LIB::cms_time_activated_operate_error_encode); }
    public static void decodeTimeActivatedOperateError(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_activated_operate_error_decode); }
    public static byte[] encodeTimeActivatedOperateTermination(Pointer p) { return encode(p, LIB::cms_time_activated_operate_termination_encode); }
    public static void decodeTimeActivatedOperateTermination(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_activated_operate_termination_decode); }
    public static byte[] encodeGetDataDefinitionRequest(Pointer p) { return encode(p, LIB::cms_get_data_definition_request_encode); }
    public static void decodeGetDataDefinitionRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_definition_request_decode); }
    public static byte[] encodeGetDataDefinitionResponse(Pointer p) { return encode(p, LIB::cms_get_data_definition_response_encode); }
    public static void decodeGetDataDefinitionResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_definition_response_decode); }
    public static byte[] encodeGetDataDefinitionError(Pointer p) { return encode(p, LIB::cms_get_data_definition_error_encode); }
    public static void decodeGetDataDefinitionError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_definition_error_decode); }
    public static byte[] encodeGetDataDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_data_directory_request_encode); }
    public static void decodeGetDataDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_directory_request_decode); }
    public static byte[] encodeGetDataDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_data_directory_response_encode); }
    public static void decodeGetDataDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_directory_response_decode); }
    public static byte[] encodeGetDataDirectoryError(Pointer p) { return encode(p, LIB::cms_get_data_directory_error_encode); }
    public static void decodeGetDataDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_directory_error_decode); }
    public static byte[] encodeGetDataValuesRequest(Pointer p) { return encode(p, LIB::cms_get_data_values_request_encode); }
    public static void decodeGetDataValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_values_request_decode); }
    public static byte[] encodeGetDataValuesResponse(Pointer p) { return encode(p, LIB::cms_get_data_values_response_encode); }
    public static void decodeGetDataValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_values_response_decode); }
    public static byte[] encodeGetDataValuesError(Pointer p) { return encode(p, LIB::cms_get_data_values_error_encode); }
    public static void decodeGetDataValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_values_error_decode); }
    public static byte[] encodeSetDataValuesRequest(Pointer p) { return encode(p, LIB::cms_set_data_values_request_encode); }
    public static void decodeSetDataValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_values_request_decode); }
    public static byte[] encodeSetDataValuesResponse(Pointer p) { return encode(p, LIB::cms_set_data_values_response_encode); }
    public static void decodeSetDataValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_values_response_decode); }
    public static byte[] encodeSetDataValuesError(Pointer p) { return encode(p, LIB::cms_set_data_values_error_encode); }
    public static void decodeSetDataValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_values_error_decode); }
    public static byte[] encodeCreateDataSetRequest(Pointer p) { return encode(p, LIB::cms_create_data_set_request_encode); }
    public static void decodeCreateDataSetRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_create_data_set_request_decode); }
    public static byte[] encodeCreateDataSetResponse(Pointer p) { return encode(p, LIB::cms_create_data_set_response_encode); }
    public static void decodeCreateDataSetResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_create_data_set_response_decode); }
    public static byte[] encodeCreateDataSetError(Pointer p) { return encode(p, LIB::cms_create_data_set_error_encode); }
    public static void decodeCreateDataSetError(Pointer p, byte[] d) { decode(p, d, LIB::cms_create_data_set_error_decode); }
    public static byte[] encodeDeleteDataSetRequest(Pointer p) { return encode(p, LIB::cms_delete_data_set_request_encode); }
    public static void decodeDeleteDataSetRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_data_set_request_decode); }
    public static byte[] encodeDeleteDataSetResponse(Pointer p) { return encode(p, LIB::cms_delete_data_set_response_encode); }
    public static void decodeDeleteDataSetResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_data_set_response_decode); }
    public static byte[] encodeDeleteDataSetError(Pointer p) { return encode(p, LIB::cms_delete_data_set_error_encode); }
    public static void decodeDeleteDataSetError(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_data_set_error_decode); }
    public static byte[] encodeGetDataSetDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_data_set_directory_request_encode); }
    public static void decodeGetDataSetDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_directory_request_decode); }
    public static byte[] encodeGetDataSetDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_data_set_directory_response_encode); }
    public static void decodeGetDataSetDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_directory_response_decode); }
    public static byte[] encodeGetDataSetDirectoryError(Pointer p) { return encode(p, LIB::cms_get_data_set_directory_error_encode); }
    public static void decodeGetDataSetDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_directory_error_decode); }
    public static byte[] encodeGetDataSetValuesRequest(Pointer p) { return encode(p, LIB::cms_get_data_set_values_request_encode); }
    public static void decodeGetDataSetValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_values_request_decode); }
    public static byte[] encodeGetDataSetValuesResponse(Pointer p) { return encode(p, LIB::cms_get_data_set_values_response_encode); }
    public static void decodeGetDataSetValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_values_response_decode); }
    public static byte[] encodeGetDataSetValuesError(Pointer p) { return encode(p, LIB::cms_get_data_set_values_error_encode); }
    public static void decodeGetDataSetValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_data_set_values_error_decode); }
    public static byte[] encodeSetDataSetValuesRequest(Pointer p) { return encode(p, LIB::cms_set_data_set_values_request_encode); }
    public static void decodeSetDataSetValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_set_values_request_decode); }
    public static byte[] encodeSetDataSetValuesResponse(Pointer p) { return encode(p, LIB::cms_set_data_set_values_response_encode); }
    public static void decodeSetDataSetValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_set_values_response_decode); }
    public static byte[] encodeSetDataSetValuesError(Pointer p) { return encode(p, LIB::cms_set_data_set_values_error_encode); }
    public static void decodeSetDataSetValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_data_set_values_error_decode); }
    public static byte[] encodeGetAllCbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_all_cb_values_request_encode); }
    public static void decodeGetAllCbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_cb_values_request_decode); }
    public static byte[] encodeGetAllCbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_all_cb_values_response_encode); }
    public static void decodeGetAllCbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_cb_values_response_decode); }
    public static byte[] encodeGetAllCbValuesError(Pointer p) { return encode(p, LIB::cms_get_all_cb_values_error_encode); }
    public static void decodeGetAllCbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_cb_values_error_decode); }
    public static byte[] encodeGetAllDataDefinitionRequest(Pointer p) { return encode(p, LIB::cms_get_all_data_definition_request_encode); }
    public static void decodeGetAllDataDefinitionRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_definition_request_decode); }
    public static byte[] encodeGetAllDataDefinitionResponse(Pointer p) { return encode(p, LIB::cms_get_all_data_definition_response_encode); }
    public static void decodeGetAllDataDefinitionResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_definition_response_decode); }
    public static byte[] encodeGetAllDataDefinitionError(Pointer p) { return encode(p, LIB::cms_get_all_data_definition_error_encode); }
    public static void decodeGetAllDataDefinitionError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_definition_error_decode); }
    public static byte[] encodeGetAllDataValuesRequest(Pointer p) { return encode(p, LIB::cms_get_all_data_values_request_encode); }
    public static void decodeGetAllDataValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_values_request_decode); }
    public static byte[] encodeGetAllDataValuesResponse(Pointer p) { return encode(p, LIB::cms_get_all_data_values_response_encode); }
    public static void decodeGetAllDataValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_values_response_decode); }
    public static byte[] encodeGetAllDataValuesError(Pointer p) { return encode(p, LIB::cms_get_all_data_values_error_encode); }
    public static void decodeGetAllDataValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_all_data_values_error_decode); }
    public static byte[] encodeGetLogicalDeviceDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_logical_device_directory_request_encode); }
    public static void decodeGetLogicalDeviceDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_device_directory_request_decode); }
    public static byte[] encodeGetLogicalDeviceDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_logical_device_directory_response_encode); }
    public static void decodeGetLogicalDeviceDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_device_directory_response_decode); }
    public static byte[] encodeGetLogicalDeviceDirectoryError(Pointer p) { return encode(p, LIB::cms_get_logical_device_directory_error_encode); }
    public static void decodeGetLogicalDeviceDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_device_directory_error_decode); }
    public static byte[] encodeGetLogicalNodeDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_logical_node_directory_request_encode); }
    public static void decodeGetLogicalNodeDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_node_directory_request_decode); }
    public static byte[] encodeGetLogicalNodeDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_logical_node_directory_response_encode); }
    public static void decodeGetLogicalNodeDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_node_directory_response_decode); }
    public static byte[] encodeGetLogicalNodeDirectoryError(Pointer p) { return encode(p, LIB::cms_get_logical_node_directory_error_encode); }
    public static void decodeGetLogicalNodeDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_logical_node_directory_error_decode); }
    public static byte[] encodeGetServerDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_server_directory_request_encode); }
    public static void decodeGetServerDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_server_directory_request_decode); }
    public static byte[] encodeGetServerDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_server_directory_response_encode); }
    public static void decodeGetServerDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_server_directory_response_decode); }
    public static byte[] encodeGetServerDirectoryError(Pointer p) { return encode(p, LIB::cms_get_server_directory_error_encode); }
    public static void decodeGetServerDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_server_directory_error_decode); }
    public static byte[] encodeDeleteFileRequest(Pointer p) { return encode(p, LIB::cms_delete_file_request_encode); }
    public static void decodeDeleteFileRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_file_request_decode); }
    public static byte[] encodeDeleteFileResponse(Pointer p) { return encode(p, LIB::cms_delete_file_response_encode); }
    public static void decodeDeleteFileResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_file_response_decode); }
    public static byte[] encodeDeleteFileError(Pointer p) { return encode(p, LIB::cms_delete_file_error_encode); }
    public static void decodeDeleteFileError(Pointer p, byte[] d) { decode(p, d, LIB::cms_delete_file_error_decode); }
    public static byte[] encodeGetFileRequest(Pointer p) { return encode(p, LIB::cms_get_file_request_encode); }
    public static void decodeGetFileRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_request_decode); }
    public static byte[] encodeGetFileResponse(Pointer p) { return encode(p, LIB::cms_get_file_response_encode); }
    public static void decodeGetFileResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_response_decode); }
    public static byte[] encodeGetFileError(Pointer p) { return encode(p, LIB::cms_get_file_error_encode); }
    public static void decodeGetFileError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_error_decode); }
    public static byte[] encodeGetFileAttributeValuesRequest(Pointer p) { return encode(p, LIB::cms_get_file_attribute_values_request_encode); }
    public static void decodeGetFileAttributeValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_attribute_values_request_decode); }
    public static byte[] encodeGetFileAttributeValuesResponse(Pointer p) { return encode(p, LIB::cms_get_file_attribute_values_response_encode); }
    public static void decodeGetFileAttributeValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_attribute_values_response_decode); }
    public static byte[] encodeGetFileAttributeValuesError(Pointer p) { return encode(p, LIB::cms_get_file_attribute_values_error_encode); }
    public static void decodeGetFileAttributeValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_attribute_values_error_decode); }
    public static byte[] encodeGetFileDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_file_directory_request_encode); }
    public static void decodeGetFileDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_directory_request_decode); }
    public static byte[] encodeGetFileDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_file_directory_response_encode); }
    public static void decodeGetFileDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_directory_response_decode); }
    public static byte[] encodeGetFileDirectoryError(Pointer p) { return encode(p, LIB::cms_get_file_directory_error_encode); }
    public static void decodeGetFileDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_file_directory_error_decode); }
    public static byte[] encodeSetFileRequest(Pointer p) { return encode(p, LIB::cms_set_file_request_encode); }
    public static void decodeSetFileRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_file_request_decode); }
    public static byte[] encodeSetFileResponse(Pointer p) { return encode(p, LIB::cms_set_file_response_encode); }
    public static void decodeSetFileResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_file_response_decode); }
    public static byte[] encodeSetFileError(Pointer p) { return encode(p, LIB::cms_set_file_error_encode); }
    public static void decodeSetFileError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_file_error_decode); }
    public static byte[] encodeGetGoCbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_go_cb_values_request_encode); }
    public static void decodeGetGoCbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_cb_values_request_decode); }
    public static byte[] encodeGetGoCbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_go_cb_values_response_encode); }
    public static void decodeGetGoCbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_cb_values_response_decode); }
    public static byte[] encodeGetGoCbValuesError(Pointer p) { return encode(p, LIB::cms_get_go_cb_values_error_encode); }
    public static void decodeGetGoCbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_cb_values_error_decode); }
    public static byte[] encodeGetGoReferenceRequest(Pointer p) { return encode(p, LIB::cms_get_go_reference_request_encode); }
    public static void decodeGetGoReferenceRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_reference_request_decode); }
    public static byte[] encodeGetGoReferenceResponse(Pointer p) { return encode(p, LIB::cms_get_go_reference_response_encode); }
    public static void decodeGetGoReferenceResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_reference_response_decode); }
    public static byte[] encodeGetGoReferenceError(Pointer p) { return encode(p, LIB::cms_get_go_reference_error_encode); }
    public static void decodeGetGoReferenceError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_go_reference_error_decode); }
    public static byte[] encodeGetGooseElementNumberRequest(Pointer p) { return encode(p, LIB::cms_get_goose_element_number_request_encode); }
    public static void decodeGetGooseElementNumberRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_goose_element_number_request_decode); }
    public static byte[] encodeGetGooseElementNumberResponse(Pointer p) { return encode(p, LIB::cms_get_goose_element_number_response_encode); }
    public static void decodeGetGooseElementNumberResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_goose_element_number_response_decode); }
    public static byte[] encodeGetGooseElementNumberError(Pointer p) { return encode(p, LIB::cms_get_goose_element_number_error_encode); }
    public static void decodeGetGooseElementNumberError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_goose_element_number_error_decode); }
    public static byte[] encodeSendGooseMessage(Pointer p) { return encode(p, LIB::cms_send_goose_message_encode); }
    public static void decodeSendGooseMessage(Pointer p, byte[] d) { decode(p, d, LIB::cms_send_goose_message_decode); }
    public static byte[] encodeSetGoCbValuesRequest(Pointer p) { return encode(p, LIB::cms_set_go_cb_values_request_encode); }
    public static void decodeSetGoCbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_go_cb_values_request_decode); }
    public static byte[] encodeSetGoCbValuesResponse(Pointer p) { return encode(p, LIB::cms_set_go_cb_values_response_encode); }
    public static void decodeSetGoCbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_go_cb_values_response_decode); }
    public static byte[] encodeSetGoCbValuesError(Pointer p) { return encode(p, LIB::cms_set_go_cb_values_error_encode); }
    public static void decodeSetGoCbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_go_cb_values_error_decode); }
    public static byte[] encodeGetLcbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_lcb_values_request_encode); }
    public static void decodeGetLcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_lcb_values_request_decode); }
    public static byte[] encodeGetLcbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_lcb_values_response_encode); }
    public static void decodeGetLcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_lcb_values_response_decode); }
    public static byte[] encodeGetLcbValuesError(Pointer p) { return encode(p, LIB::cms_get_lcb_values_error_encode); }
    public static void decodeGetLcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_lcb_values_error_decode); }
    public static byte[] encodeGetLogStatusValuesRequest(Pointer p) { return encode(p, LIB::cms_get_log_status_values_request_encode); }
    public static void decodeGetLogStatusValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_log_status_values_request_decode); }
    public static byte[] encodeGetLogStatusValuesResponse(Pointer p) { return encode(p, LIB::cms_get_log_status_values_response_encode); }
    public static void decodeGetLogStatusValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_log_status_values_response_decode); }
    public static byte[] encodeGetLogStatusValuesError(Pointer p) { return encode(p, LIB::cms_get_log_status_values_error_encode); }
    public static void decodeGetLogStatusValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_log_status_values_error_decode); }
    public static byte[] encodeQueryLogAfterRequest(Pointer p) { return encode(p, LIB::cms_query_log_after_request_encode); }
    public static void decodeQueryLogAfterRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_after_request_decode); }
    public static byte[] encodeQueryLogAfterResponse(Pointer p) { return encode(p, LIB::cms_query_log_after_response_encode); }
    public static void decodeQueryLogAfterResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_after_response_decode); }
    public static byte[] encodeQueryLogAfterError(Pointer p) { return encode(p, LIB::cms_query_log_after_error_encode); }
    public static void decodeQueryLogAfterError(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_after_error_decode); }
    public static byte[] encodeQueryLogByTimeRequest(Pointer p) { return encode(p, LIB::cms_query_log_by_time_request_encode); }
    public static void decodeQueryLogByTimeRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_by_time_request_decode); }
    public static byte[] encodeQueryLogByTimeResponse(Pointer p) { return encode(p, LIB::cms_query_log_by_time_response_encode); }
    public static void decodeQueryLogByTimeResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_by_time_response_decode); }
    public static byte[] encodeQueryLogByTimeError(Pointer p) { return encode(p, LIB::cms_query_log_by_time_error_encode); }
    public static void decodeQueryLogByTimeError(Pointer p, byte[] d) { decode(p, d, LIB::cms_query_log_by_time_error_decode); }
    public static byte[] encodeSetLcbValuesRequest(Pointer p) { return encode(p, LIB::cms_set_lcb_values_request_encode); }
    public static void decodeSetLcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_lcb_values_request_decode); }
    public static byte[] encodeSetLcbValuesResponse(Pointer p) { return encode(p, LIB::cms_set_lcb_values_response_encode); }
    public static void decodeSetLcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_lcb_values_response_decode); }
    public static byte[] encodeSetLcbValuesError(Pointer p) { return encode(p, LIB::cms_set_lcb_values_error_encode); }
    public static void decodeSetLcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_lcb_values_error_decode); }
    public static byte[] encodeGetMsvcbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_msvcb_values_request_encode); }
    public static void decodeGetMsvcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_msvcb_values_request_decode); }
    public static byte[] encodeGetMsvcbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_msvcb_values_response_encode); }
    public static void decodeGetMsvcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_msvcb_values_response_decode); }
    public static byte[] encodeGetMsvcbValuesError(Pointer p) { return encode(p, LIB::cms_get_msvcb_values_error_encode); }
    public static void decodeGetMsvcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_msvcb_values_error_decode); }
    public static byte[] encodeSendMsvMessage(Pointer p) { return encode(p, LIB::cms_send_msv_message_encode); }
    public static void decodeSendMsvMessage(Pointer p, byte[] d) { decode(p, d, LIB::cms_send_msv_message_decode); }
    public static byte[] encodeSetMsvcbValuesRequest(Pointer p) { return encode(p, LIB::cms_set_msvcb_values_request_encode); }
    public static void decodeSetMsvcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_msvcb_values_request_decode); }
    public static byte[] encodeSetMsvcbValuesResponse(Pointer p) { return encode(p, LIB::cms_set_msvcb_values_response_encode); }
    public static void decodeSetMsvcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_msvcb_values_response_decode); }
    public static byte[] encodeSetMsvcbValuesError(Pointer p) { return encode(p, LIB::cms_set_msvcb_values_error_encode); }
    public static void decodeSetMsvcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_msvcb_values_error_decode); }
    public static byte[] encodeNegotiateRequest(Pointer p) { return encode(p, LIB::cms_negotiate_request_encode); }
    public static void decodeNegotiateRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_negotiate_request_decode); }
    public static byte[] encodeNegotiateResponse(Pointer p) { return encode(p, LIB::cms_negotiate_response_encode); }
    public static void decodeNegotiateResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_negotiate_response_decode); }
    public static byte[] encodeNegotiateError(Pointer p) { return encode(p, LIB::cms_negotiate_error_encode); }
    public static void decodeNegotiateError(Pointer p, byte[] d) { decode(p, d, LIB::cms_negotiate_error_decode); }
    public static byte[] encodeReport(Pointer p) { return encode(p, LIB::cms_report_encode); }
    public static void decodeReport(Pointer p, byte[] d) { decode(p, d, LIB::cms_report_decode); }
    public static byte[] encodeGetBrcbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_brcb_values_request_encode); }
    public static void decodeGetBrcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_brcb_values_request_decode); }
    public static byte[] encodeGetBrcbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_brcb_values_response_encode); }
    public static void decodeGetBrcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_brcb_values_response_decode); }
    public static byte[] encodeGetBrcbValuesError(Pointer p) { return encode(p, LIB::cms_get_brcb_values_error_encode); }
    public static void decodeGetBrcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_brcb_values_error_decode); }
    public static byte[] encodeGetUrcbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_urcb_values_request_encode); }
    public static void decodeGetUrcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_urcb_values_request_decode); }
    public static byte[] encodeGetUrcbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_urcb_values_response_encode); }
    public static void decodeGetUrcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_urcb_values_response_decode); }
    public static byte[] encodeGetUrcbValuesError(Pointer p) { return encode(p, LIB::cms_get_urcb_values_error_encode); }
    public static void decodeGetUrcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_urcb_values_error_decode); }
    public static byte[] encodeSetBrcbValuesRequest(Pointer p) { return encode(p, LIB::cms_set_brcb_values_request_encode); }
    public static void decodeSetBrcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_brcb_values_request_decode); }
    public static byte[] encodeSetBrcbValuesResponse(Pointer p) { return encode(p, LIB::cms_set_brcb_values_response_encode); }
    public static void decodeSetBrcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_brcb_values_response_decode); }
    public static byte[] encodeSetBrcbValuesError(Pointer p) { return encode(p, LIB::cms_set_brcb_values_error_encode); }
    public static void decodeSetBrcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_brcb_values_error_decode); }
    public static byte[] encodeSetUrcbValuesRequest(Pointer p) { return encode(p, LIB::cms_set_urcb_values_request_encode); }
    public static void decodeSetUrcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_urcb_values_request_decode); }
    public static byte[] encodeSetUrcbValuesResponse(Pointer p) { return encode(p, LIB::cms_set_urcb_values_response_encode); }
    public static void decodeSetUrcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_urcb_values_response_decode); }
    public static byte[] encodeSetUrcbValuesError(Pointer p) { return encode(p, LIB::cms_set_urcb_values_error_encode); }
    public static void decodeSetUrcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_urcb_values_error_decode); }
    public static byte[] encodeRpcCallRequest(Pointer p) { return encode(p, LIB::cms_rpc_call_request_encode); }
    public static void decodeRpcCallRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_rpc_call_request_decode); }
    public static byte[] encodeRpcCallResponse(Pointer p) { return encode(p, LIB::cms_rpc_call_response_encode); }
    public static void decodeRpcCallResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_rpc_call_response_decode); }
    public static byte[] encodeRpcCallError(Pointer p) { return encode(p, LIB::cms_rpc_call_error_encode); }
    public static void decodeRpcCallError(Pointer p, byte[] d) { decode(p, d, LIB::cms_rpc_call_error_decode); }
    public static byte[] encodeGetRpcInterfaceDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_directory_request_encode); }
    public static void decodeGetRpcInterfaceDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_directory_request_decode); }
    public static byte[] encodeGetRpcInterfaceDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_directory_response_encode); }
    public static void decodeGetRpcInterfaceDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_directory_response_decode); }
    public static byte[] encodeGetRpcInterfaceDirectoryError(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_directory_error_encode); }
    public static void decodeGetRpcInterfaceDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_directory_error_decode); }
    public static byte[] encodeGetRpcInterfaceDefinitionRequest(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_definition_request_encode); }
    public static void decodeGetRpcInterfaceDefinitionRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_definition_request_decode); }
    public static byte[] encodeGetRpcInterfaceDefinitionResponse(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_definition_response_encode); }
    public static void decodeGetRpcInterfaceDefinitionResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_definition_response_decode); }
    public static byte[] encodeGetRpcInterfaceDefinitionError(Pointer p) { return encode(p, LIB::cms_get_rpc_interface_definition_error_encode); }
    public static void decodeGetRpcInterfaceDefinitionError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_interface_definition_error_decode); }
    public static byte[] encodeGetRpcMethodDirectoryRequest(Pointer p) { return encode(p, LIB::cms_get_rpc_method_directory_request_encode); }
    public static void decodeGetRpcMethodDirectoryRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_directory_request_decode); }
    public static byte[] encodeGetRpcMethodDirectoryResponse(Pointer p) { return encode(p, LIB::cms_get_rpc_method_directory_response_encode); }
    public static void decodeGetRpcMethodDirectoryResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_directory_response_decode); }
    public static byte[] encodeGetRpcMethodDirectoryError(Pointer p) { return encode(p, LIB::cms_get_rpc_method_directory_error_encode); }
    public static void decodeGetRpcMethodDirectoryError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_directory_error_decode); }
    public static byte[] encodeGetRpcMethodDefinitionRequest(Pointer p) { return encode(p, LIB::cms_get_rpc_method_definition_request_encode); }
    public static void decodeGetRpcMethodDefinitionRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_definition_request_decode); }
    public static byte[] encodeGetRpcMethodDefinitionResponse(Pointer p) { return encode(p, LIB::cms_get_rpc_method_definition_response_encode); }
    public static void decodeGetRpcMethodDefinitionResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_definition_response_decode); }
    public static byte[] encodeGetRpcMethodDefinitionError(Pointer p) { return encode(p, LIB::cms_get_rpc_method_definition_error_encode); }
    public static void decodeGetRpcMethodDefinitionError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_rpc_method_definition_error_decode); }
    public static byte[] encodeConfirmEditSgValuesRequest(Pointer p) { return encode(p, LIB::cms_confirm_edit_sg_values_request_encode); }
    public static void decodeConfirmEditSgValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_confirm_edit_sg_values_request_decode); }
    public static byte[] encodeConfirmEditSgValuesResponse(Pointer p) { return encode(p, LIB::cms_confirm_edit_sg_values_response_encode); }
    public static void decodeConfirmEditSgValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_confirm_edit_sg_values_response_decode); }
    public static byte[] encodeConfirmEditSgValuesError(Pointer p) { return encode(p, LIB::cms_confirm_edit_sg_values_error_encode); }
    public static void decodeConfirmEditSgValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_confirm_edit_sg_values_error_decode); }
    public static byte[] encodeGetEditSgValueRequest(Pointer p) { return encode(p, LIB::cms_get_edit_sg_value_request_encode); }
    public static void decodeGetEditSgValueRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_edit_sg_value_request_decode); }
    public static byte[] encodeGetEditSgValueResponse(Pointer p) { return encode(p, LIB::cms_get_edit_sg_value_response_encode); }
    public static void decodeGetEditSgValueResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_edit_sg_value_response_decode); }
    public static byte[] encodeGetEditSgValueError(Pointer p) { return encode(p, LIB::cms_get_edit_sg_value_error_encode); }
    public static void decodeGetEditSgValueError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_edit_sg_value_error_decode); }
    public static byte[] encodeGetSgcbValuesRequest(Pointer p) { return encode(p, LIB::cms_get_sgcb_values_request_encode); }
    public static void decodeGetSgcbValuesRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_sgcb_values_request_decode); }
    public static byte[] encodeGetSgcbValuesResponse(Pointer p) { return encode(p, LIB::cms_get_sgcb_values_response_encode); }
    public static void decodeGetSgcbValuesResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_sgcb_values_response_decode); }
    public static byte[] encodeGetSgcbValuesError(Pointer p) { return encode(p, LIB::cms_get_sgcb_values_error_encode); }
    public static void decodeGetSgcbValuesError(Pointer p, byte[] d) { decode(p, d, LIB::cms_get_sgcb_values_error_decode); }
    public static byte[] encodeSelectActiveSgRequest(Pointer p) { return encode(p, LIB::cms_select_active_sg_request_encode); }
    public static void decodeSelectActiveSgRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_active_sg_request_decode); }
    public static byte[] encodeSelectActiveSgResponse(Pointer p) { return encode(p, LIB::cms_select_active_sg_response_encode); }
    public static void decodeSelectActiveSgResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_active_sg_response_decode); }
    public static byte[] encodeSelectActiveSgError(Pointer p) { return encode(p, LIB::cms_select_active_sg_error_encode); }
    public static void decodeSelectActiveSgError(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_active_sg_error_decode); }
    public static byte[] encodeSelectEditSgRequest(Pointer p) { return encode(p, LIB::cms_select_edit_sg_request_encode); }
    public static void decodeSelectEditSgRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_edit_sg_request_decode); }
    public static byte[] encodeSelectEditSgResponse(Pointer p) { return encode(p, LIB::cms_select_edit_sg_response_encode); }
    public static void decodeSelectEditSgResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_edit_sg_response_decode); }
    public static byte[] encodeSelectEditSgError(Pointer p) { return encode(p, LIB::cms_select_edit_sg_error_encode); }
    public static void decodeSelectEditSgError(Pointer p, byte[] d) { decode(p, d, LIB::cms_select_edit_sg_error_decode); }
    public static byte[] encodeSetEditSgValueRequest(Pointer p) { return encode(p, LIB::cms_set_edit_sg_value_request_encode); }
    public static void decodeSetEditSgValueRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_edit_sg_value_request_decode); }
    public static byte[] encodeSetEditSgValueResponse(Pointer p) { return encode(p, LIB::cms_set_edit_sg_value_response_encode); }
    public static void decodeSetEditSgValueResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_edit_sg_value_response_decode); }
    public static byte[] encodeSetEditSgValueError(Pointer p) { return encode(p, LIB::cms_set_edit_sg_value_error_encode); }
    public static void decodeSetEditSgValueError(Pointer p, byte[] d) { decode(p, d, LIB::cms_set_edit_sg_value_error_decode); }
    public static byte[] encodeTestRequest(Pointer p) { return encode(p, LIB::cms_test_request_encode); }
    public static void decodeTestRequest(Pointer p, byte[] d) { decode(p, d, LIB::cms_test_request_decode); }
    public static byte[] encodeTestResponse(Pointer p) { return encode(p, LIB::cms_test_response_encode); }
    public static void decodeTestResponse(Pointer p, byte[] d) { decode(p, d, LIB::cms_test_response_decode); }
    public static byte[] encodeTestError(Pointer p) { return encode(p, LIB::cms_test_error_encode); }
    public static void decodeTestError(Pointer p, byte[] d) { decode(p, d, LIB::cms_test_error_decode); }
}
