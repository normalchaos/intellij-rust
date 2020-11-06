/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.completion.lint

val RUSTC_LINTS: List<Lint> = listOf(
    Lint("future_incompatible", true),
    Lint("nonstandard_style", true),
    Lint("rust_2018_compatibility", true),
    Lint("rust_2018_idioms", true),
    Lint("rustdoc", true),
    Lint("unused", true),
    Lint("warnings", true),
    Lint("absolute_paths_not_starting_with_crate", false),
    Lint("ambiguous_associated_items", false),
    Lint("anonymous_parameters", false),
    Lint("arithmetic_overflow", false),
    Lint("array_into_iter", false),
    Lint("asm_sub_register", false),
    Lint("bare_trait_objects", false),
    Lint("bindings_with_variant_name", false),
    Lint("box_pointers", false),
    Lint("broken_intra_doc_links", false),
    Lint("cenum_impl_drop_cast", false),
    Lint("clashing_extern_declarations", false),
    Lint("coherence_leak_check", false),
    Lint("conflicting_repr_hints", false),
    Lint("confusable_idents", false),
    Lint("const_err", false),
    Lint("dead_code", false),
    Lint("deprecated", false),
    Lint("deprecated_in_future", false),
    Lint("elided_lifetimes_in_paths", false),
    Lint("ellipsis_inclusive_range_patterns", false),
    Lint("explicit_outlives_requirements", false),
    Lint("exported_private_dependencies", false),
    Lint("ill_formed_attribute_input", false),
    Lint("illegal_floating_point_literal_pattern", false),
    Lint("improper_ctypes", false),
    Lint("improper_ctypes_definitions", false),
    Lint("incomplete_features", false),
    Lint("incomplete_include", false),
    Lint("indirect_structural_match", false),
    Lint("inline_no_sanitize", false),
    Lint("invalid_codeblock_attributes", false),
    Lint("invalid_type_param_default", false),
    Lint("invalid_value", false),
    Lint("irrefutable_let_patterns", false),
    Lint("keyword_idents", false),
    Lint("late_bound_lifetime_arguments", false),
    Lint("macro_expanded_macro_exports_accessed_by_absolute_paths", false),
    Lint("macro_use_extern_crate", false),
    Lint("meta_variable_misuse", false),
    Lint("missing_copy_implementations", false),
    Lint("missing_crate_level_docs", false),
    Lint("missing_debug_implementations", false),
    Lint("missing_doc_code_examples", false),
    Lint("missing_docs", false),
    Lint("missing_fragment_specifier", false),
    Lint("mixed_script_confusables", false),
    Lint("mutable_borrow_reservation_conflict", false),
    Lint("mutable_transmutes", false),
    Lint("no_mangle_const_items", false),
    Lint("no_mangle_generic_items", false),
    Lint("non_ascii_idents", false),
    Lint("non_camel_case_types", false),
    Lint("non_shorthand_field_patterns", false),
    Lint("non_snake_case", false),
    Lint("non_upper_case_globals", false),
    Lint("order_dependent_trait_objects", false),
    Lint("overflowing_literals", false),
    Lint("overlapping_patterns", false),
    Lint("path_statements", false),
    Lint("patterns_in_fns_without_body", false),
    Lint("private_doc_tests", false),
    Lint("private_in_public", false),
    Lint("proc_macro_derive_resolution_fallback", false),
    Lint("pub_use_of_private_extern_crate", false),
    Lint("redundant_semicolons", false),
    Lint("renamed_and_removed_lints", false),
    Lint("safe_packed_borrows", false),
    Lint("single_use_lifetimes", false),
    Lint("soft_unstable", false),
    Lint("stable_features", false),
    Lint("trivial_bounds", false),
    Lint("trivial_casts", false),
    Lint("trivial_numeric_casts", false),
    Lint("type_alias_bounds", false),
    Lint("tyvar_behind_raw_pointer", false),
    Lint("unaligned_references", false),
    Lint("uncommon_codepoints", false),
    Lint("unconditional_panic", false),
    Lint("unconditional_recursion", false),
    Lint("unknown_crate_types", false),
    Lint("unknown_lints", false),
    Lint("unnameable_test_items", false),
    Lint("unreachable_code", false),
    Lint("unreachable_patterns", false),
    Lint("unreachable_pub", false),
    Lint("unsafe_code", false),
    Lint("unsafe_op_in_unsafe_fn", false),
    Lint("unstable_features", false),
    Lint("unstable_name_collisions", false),
    Lint("unused_allocation", false),
    Lint("unused_assignments", false),
    Lint("unused_attributes", false),
    Lint("unused_braces", false),
    Lint("unused_comparisons", false),
    Lint("unused_crate_dependencies", false),
    Lint("unused_doc_comments", false),
    Lint("unused_extern_crates", false),
    Lint("unused_features", false),
    Lint("unused_import_braces", false),
    Lint("unused_imports", false),
    Lint("unused_labels", false),
    Lint("unused_lifetimes", false),
    Lint("unused_macros", false),
    Lint("unused_must_use", false),
    Lint("unused_mut", false),
    Lint("unused_parens", false),
    Lint("unused_qualifications", false),
    Lint("unused_results", false),
    Lint("unused_unsafe", false),
    Lint("unused_variables", false),
    Lint("variant_size_differences", false),
    Lint("warnings", false),
    Lint("where_clauses_object_safety", false),
    Lint("while_true", false)
)