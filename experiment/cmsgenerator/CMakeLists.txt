cmake_minimum_required(VERSION 3.14)
project(cmsgenerator VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# --- Generator tool ---
file(GLOB SRC src/asn1_lexer.c src/asn1_parser.c src/c_gen.c src/main.c)
add_executable(cmsgen ${SRC})
target_include_directories(cmsgen PRIVATE include)

# --- Datatypes library (only if generated files exist) ---
file(GLOB GEN_SRC generated/gen_*.c)
if(GEN_SRC)
    # DLL 只包含 gen_cms.c（已包含所有公共类型定义），避免与 gen_dlt2811b_datatypes.c 重复符号
    list(FILTER GEN_SRC INCLUDE REGEX "gen_cms\\.c$")
    add_library(cmsper_datatypes SHARED ${GEN_SRC} src/cms_apdu.c src/cms_ffi.c src/cms_ffi_datatypes.c)
    target_include_directories(cmsper_datatypes
        PUBLIC
            $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/generated>
            $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
        PRIVATE ${CMAKE_SOURCE_DIR}/../cmsper/include
    )
    target_link_libraries(cmsper_datatypes
        PUBLIC  ${CMAKE_SOURCE_DIR}/../cmsper/build/libcmsper.a
    )
    if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_C_COMPILER_ID MATCHES "Clang")
        target_compile_options(cmsper_datatypes PRIVATE -fvisibility=hidden)
    endif()
    set_target_properties(cmsper_datatypes PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    )
endif()
