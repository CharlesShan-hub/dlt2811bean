cmake_minimum_required(VERSION 3.14)
project(ccms VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS_SRC
    src/per/cms_stream.c
    src/per/cms_integer.c
    src/per/cms_string.c
    src/per/cms_choice.c
    src/per/cms_sequence.c

    # scalar types (leaf, inline value)
    src/data/scalar/cms_boolean.c
    src/data/scalar/cms_int8.c
    src/data/scalar/cms_int8u.c
    src/data/scalar/cms_int16.c
    src/data/scalar/cms_int16u.c
    src/data/scalar/cms_int24u.c
    src/data/scalar/cms_int32.c
    src/data/scalar/cms_int32u.c
    src/data/scalar/cms_int64.c
    src/data/scalar/cms_int64u.c
    src/data/scalar/cms_float32.c
    src/data/scalar/cms_float64.c

    # time types
    src/data/time/cms_time_quality.c
    src/data/time/cms_utc_time.c
    src/data/time/cms_binary_time.c

    # string types
    src/data/string/cms_octet_string.c
    src/data/string/cms_visible_string.c
    src/data/string/cms_utf8_string.c
    src/data/string/cms_bit_string.c

    # common types
    src/data/common/cms_quality.c
    src/data/common/cms_object_name.c
    src/data/common/cms_object_reference.c
    src/data/common/cms_sub_reference.c
    src/data/common/cms_dbpos.c
    src/data/common/cms_tcmd.c
    src/data/common/cms_time_stamp.c
    src/data/common/cms_entry_id.c
    src/data/common/cms_entry_time.c
    src/data/common/cms_file_entry.c
    src/data/common/cms_service_error.c
    src/data/common/cms_phy_com_addr.c

    # enum types
    src/data/enum/cms_enumerated.c

    # fc types
    src/data/fc/cms_functional_constraint.c

    # control types
    src/data/control/cms_or_cat.c
    src/data/control/cms_originator.c
    src/data/control/cms_check.c
    src/data/control/cms_add_cause.c

    # block types
    src/data/block/cms_trigger_conditions.c
    src/data/block/cms_reason_code.c
    src/data/block/cms_rcb_opt_flds.c
    src/data/block/cms_lcb_opt_flds.c
    src/data/block/cms_msvcb_opt_flds.c
    src/data/block/cms_smp_mod.c
    src/data/block/cms_sgcb.c
    src/data/block/cms_brcb.c
    src/data/block/cms_urcb.c
    src/data/block/cms_lcb.c
    src/data/block/cms_go_cb.c
    src/data/block/cms_msvcb.c

    # choice types
    src/data/choice/cms_data.c
    src/data/choice/cms_data_definition.c
    src/data/choice/cms_data_definition_array.c

    # svc - other
    src/svc/other/cms_req_id.c
    src/svc/other/cms_association_id.c
    src/svc/other/cms_reference_choice.c

    # svc - connection
    src/svc/connection/cms_abort_reason.c
    src/svc/connection/cms_authentication_parameter.c
    src/svc/connection/cms_associate.c
    src/svc/connection/cms_release.c
    src/svc/connection/cms_abort.c

    # svc - directory
    src/svc/directory/cms_acsi_class.c
    src/svc/directory/cms_object_class.c
    src/svc/directory/cms_data_value_entry.c
    src/svc/directory/cms_data_definition_entry.c
    src/svc/directory/cms_cb_value_choice.c
    src/svc/directory/cms_cb_value_entry.c
    src/svc/directory/cms_get_server_directory.c
    src/svc/directory/cms_get_logical_device_directory.c
    src/svc/directory/cms_get_logical_node_directory.c
    src/svc/directory/cms_get_all_data_values.c
    src/svc/directory/cms_get_all_data_definition.c
    src/svc/directory/cms_get_all_cb_values.c

    # svc - data
    src/svc/data/cms_data_ref_entry.c
    src/svc/data/cms_sub_ref_entry.c
    src/svc/data/cms_data_ref_value_entry.c
    src/svc/data/cms_data_def_result_entry.c
    src/svc/data/cms_get_data_directory.c
    src/svc/data/cms_get_data_definition.c
    src/svc/data/cms_get_data_values.c
    src/svc/data/cms_set_data_values.c

    # svc - dataset
    src/svc/dataset/cms_data_ref_fc_entry.c
    src/svc/dataset/cms_get_data_set_directory.c
    src/svc/dataset/cms_create_data_set.c
    src/svc/dataset/cms_get_data_set_values.c
    src/svc/dataset/cms_set_data_set_values.c
    src/svc/dataset/cms_delete_data_set.c

    # svc - sg
    src/svc/sg/cms_sgcb_value_choice.c
    src/svc/sg/cms_sg_ref_fc_entry.c
    src/svc/sg/cms_sg_ref_value_entry.c
    src/svc/sg/cms_select_active_sg.c
    src/svc/sg/cms_select_edit_sg.c
    src/svc/sg/cms_set_edit_sg_value.c
    src/svc/sg/cms_confirm_edit_sg_values.c
    src/svc/sg/cms_get_edit_sg_value.c
    src/svc/sg/cms_get_sgcb_values.c

    # svc - report
    src/svc/report/cms_rcb_value_choice.c
    src/svc/report/cms_report_data_entry.c
    src/svc/report/cms_report_entry.c
    src/svc/report/cms_set_brcb_entry.c
    src/svc/report/cms_set_urcb_entry.c
    src/svc/report/cms_set_brcb_result.c
    src/svc/report/cms_set_urcb_result.c
    src/svc/report/cms_report.c
    src/svc/report/cms_get_brcb_values.c
    src/svc/report/cms_get_urcb_values.c
    src/svc/report/cms_set_brcb_values.c
    src/svc/report/cms_set_urcb_values.c

    # svc - log
    src/svc/log/cms_log_data_entry.c
    src/svc/log/cms_log_entry.c
    src/svc/log/cms_lcb_value_choice.c
    src/svc/log/cms_log_status_value.c
    src/svc/log/cms_log_status_value_choice.c
    src/svc/log/cms_set_lcb_entry.c
    src/svc/log/cms_set_lcb_result.c
    src/svc/log/cms_get_lcb_values.c
    src/svc/log/cms_set_lcb_values.c
    src/svc/log/cms_query_log_after.c
    src/svc/log/cms_query_log_by_time.c
    src/svc/log/cms_get_log_status_values.c

    # svc - goose
    src/svc/goose/cms_go_ref_fc_entry.c
    src/svc/goose/cms_gocb_value_choice.c
    src/svc/goose/cms_set_go_cb_entry.c
    src/svc/goose/cms_set_go_cb_result.c
    src/svc/goose/cms_send_goose_message.c
    src/svc/goose/cms_get_go_reference.c
    src/svc/goose/cms_get_goose_element_number.c
    src/svc/goose/cms_get_go_cb_values.c
    src/svc/goose/cms_set_go_cb_values.c

    # svc - msv
    src/svc/msv/cms_msvcb_value_choice.c
    src/svc/msv/cms_set_msvcb_entry.c
    src/svc/msv/cms_set_msvcb_result.c
    src/svc/msv/cms_send_msv_message.c
    src/svc/msv/cms_get_msvcb_values.c
    src/svc/msv/cms_set_msvcb_values.c

    # svc - control
    src/svc/control/cms_select.c
    src/svc/control/cms_select_with_value.c
    src/svc/control/cms_operate.c
    src/svc/control/cms_cancel.c
    src/svc/control/cms_command_termination.c
    src/svc/control/cms_time_activated_operate.c
    src/svc/control/cms_time_activated_operate_termination.c

    # svc - file
    src/svc/file/cms_visible_string255.c
    src/svc/file/cms_get_file.c
    src/svc/file/cms_set_file.c
    src/svc/file/cms_delete_file.c
    src/svc/file/cms_get_file_attribute_values.c
    src/svc/file/cms_get_file_directory.c

    # svc - rpc
    src/svc/rpc/cms_rpc_method_entry.c
    src/svc/rpc/cms_rpc_method_def.c
    src/svc/rpc/cms_rpc_method_def_choice.c
    src/svc/rpc/cms_rpc_call_req_choice.c
    src/svc/rpc/cms_get_rpc_interface_directory.c
    src/svc/rpc/cms_get_rpc_method_directory.c
    src/svc/rpc/cms_get_rpc_interface_definition.c
    src/svc/rpc/cms_get_rpc_method_definition.c
    src/svc/rpc/cms_rpc_call.c

    # svc - test
    src/svc/test/cms_test.c

    # svc - negotiate
    src/svc/negotiate/cms_negotiate.c
)

add_library(ccms SHARED ${CMS_SRC})
target_include_directories(ccms PUBLIC include)

if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_C_COMPILER_ID MATCHES "Clang")
    target_compile_options(ccms PRIVATE -fvisibility=hidden)
endif()
set_target_properties(ccms PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
)

# Test
add_executable(test_scalar tests/test_scalar.c)
target_link_libraries(test_scalar ccms)
target_include_directories(test_scalar PRIVATE include)
set_target_properties(test_scalar PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
)

add_executable(test_per tests/test_per.c
    src/per/cms_stream.c
    src/per/cms_integer.c
    src/per/cms_string.c
    src/per/cms_choice.c
    src/per/cms_sequence.c
)
target_include_directories(test_per PRIVATE include)
set_target_properties(test_per PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
)
