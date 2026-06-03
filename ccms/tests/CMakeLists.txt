# Test executable
add_executable(test_cmsper
    test_main.c
    test_stream.c
    test_boolean.c
    test_integer.c
    test_enumerated.c
    test_string.c
    test_bit_string.c
    test_choice.c
)

target_link_libraries(test_cmsper PRIVATE ccms_core ccms)
target_include_directories(test_cmsper PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../include)

add_custom_command(TARGET test_cmsper POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different
        "$<TARGET_FILE:ccms>"
        "$<TARGET_FILE_DIR:test_cmsper>"
)

add_test(NAME cmsper COMMAND test_cmsper)
