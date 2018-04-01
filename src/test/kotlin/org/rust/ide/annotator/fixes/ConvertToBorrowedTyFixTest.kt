/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator.fixes

class ConvertToBorrowedTyFixTest : ConvertToTyUsingTraitFixTestBase(
    false, "Borrow", "borrow", "use std::borrow::Borrow;")
