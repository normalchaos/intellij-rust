/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import org.rust.lang.core.RsPsiPattern
import org.rust.lang.core.RsPsiPattern.declarationPattern
import org.rust.lang.core.RsPsiPattern.inherentImplDeclarationPattern
import org.rust.lang.core.completion.lint.RsClippyLintCompletionProvider
import org.rust.lang.core.completion.lint.RsRustcLintCompletionProvider
import org.rust.lang.core.or
import org.rust.lang.core.psi.RsElementTypes.COLON
import org.rust.lang.core.psi.ext.elementType

class RsCompletionContributor : CompletionContributor() {

    init {
        extend(CompletionType.BASIC, RsPrimitiveTypeCompletionProvider)
        extend(CompletionType.BASIC, RsBoolCompletionProvider)
        extend(CompletionType.BASIC, RsFragmentSpecifierCompletionProvider)
        extend(CompletionType.BASIC, RsCommonCompletionProvider)
        extend(CompletionType.BASIC, RsTupleFieldCompletionProvider)
        extend(CompletionType.BASIC, RsDeriveCompletionProvider)
        extend(CompletionType.BASIC, RsAttributeCompletionProvider)
        extend(CompletionType.BASIC, RsMacroCompletionProvider)
        extend(CompletionType.BASIC, RsPartialMacroArgumentCompletionProvider)
        extend(CompletionType.BASIC, RsFullMacroArgumentCompletionProvider)
        extend(CompletionType.BASIC, RsCfgAttributeCompletionProvider)
        extend(CompletionType.BASIC, RsAwaitCompletionProvider)
        extend(CompletionType.BASIC, RsStructPatRestCompletionProvider)
        extend(CompletionType.BASIC, RsClippyLintCompletionProvider)
        extend(CompletionType.BASIC, RsRustcLintCompletionProvider)
        extend(CompletionType.BASIC, RsImplTraitMemberCompletionProvider)
        extend(CompletionType.BASIC, RsVisRestrictionCompletionProvider)
        extend(CompletionType.BASIC, RsLambdaExprCompletionProvider)
        extend(CompletionType.BASIC, RsPsiPattern.fieldVisibility, RsVisibilityCompletionProvider())
        extend(CompletionType.BASIC, declarationPattern() or inherentImplDeclarationPattern(), RsVisibilityCompletionProvider())
    }

    fun extend(type: CompletionType?, provider: RsCompletionProvider) {
        extend(type, provider.elementPattern, provider)
    }

    override fun invokeAutoPopup(position: PsiElement, typeChar: Char): Boolean =
        typeChar == ':' && position.elementType == COLON
}
