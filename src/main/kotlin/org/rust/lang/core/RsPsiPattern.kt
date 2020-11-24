/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core

import com.intellij.patterns.*
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns.or
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.RsElementTypes.*
import org.rust.lang.core.psi.ext.*

/**
 * Rust PSI tree patterns.
 */
object RsPsiPattern {
    private val STATEMENT_BOUNDARIES = TokenSet.create(SEMICOLON, LBRACE, RBRACE)

    /**
     * Source of attributes: [https://doc.rust-lang.org/1.41.1/reference/attributes.html#built-in-attributes-index]
     */
    val STD_ATTRIBUTES: Set<String> = setOf(
        "cfg",
        "cfg_attr",

        "test",
        "ignore",
        "should_panic",

        "derive",

        "macro_export",
        "macro_use",
        "proc_macro",
        "proc_macro_derive",
        "proc_macro_attribute",

        "allow",
        "warn",
        "deny",
        "forbid",

        "deprecated",
        "must_use",

        "link",
        "link_name",
        "no_link",
        "repr",
        "crate_type",
        "no_main",
        "export_name",
        "link_section",
        "no_mangle",
        "used",
        "crate_name",

        "inline",
        "cold",
        "no_builtins",
        "target_feature",

        "doc",

        "no_std",
        "no_implicit_prelude",

        "path",

        "recursion_limit",
        "type_length_limit",

        "panic_handler",
        "global_allocator",
        "windows_subsystem",

        "non_exhaustive",

        // unstable attr
        "start"
    )

    private val LINT_ATTRIBUTES: Set<String> = setOf(
        "allow",
        "warn",
        "deny",
        "forbid"
    )

    const val META_ITEM_IDENTIFIER_DEPTH = 4

    val onStatementBeginning: PsiElementPattern.Capture<PsiElement> = psiElement().with(OnStatementBeginning())

    fun onStatementBeginning(vararg startWords: String): PsiElementPattern.Capture<PsiElement> =
        psiElement().with(OnStatementBeginning(*startWords))

    val onStruct: PsiElementPattern.Capture<PsiElement> = onItem<RsStructItem>()

    val onEnum: PsiElementPattern.Capture<PsiElement> = onItem<RsEnumItem>()

    val onEnumVariant: PsiElementPattern.Capture<PsiElement> = onItem<RsEnumVariant>()

    val onFn: PsiElementPattern.Capture<PsiElement> = onItem<RsFunction>()

    val onMod: PsiElementPattern.Capture<PsiElement> = onItem<RsModItem>() or onItem<RsModDeclItem>()

    val onStatic: PsiElementPattern.Capture<PsiElement> = onItem(psiElement<RsConstant>()
        .with("onStaticCondition") { e -> e.kind == RsConstantKind.STATIC })

    val onStaticMut: PsiElementPattern.Capture<PsiElement> = onItem(psiElement<RsConstant>()
        .with("onStaticMutCondition") { e -> e.kind == RsConstantKind.MUT_STATIC })

    val onMacro: PsiElementPattern.Capture<PsiElement> = onItem<RsMacro>()

    val onTupleStruct: PsiElementPattern.Capture<PsiElement> = onItem(psiElement<RsStructItem>()
        .withChild(psiElement<RsTupleFields>()))

    val onCrate: PsiElementPattern.Capture<PsiElement> = onItem<RsFile>()
        .with("onCrateCondition") { e ->
            val file = e.containingFile.originalFile as RsFile
            file.isCrateRoot
        }

    val onExternBlock: PsiElementPattern.Capture<PsiElement> = onItem<RsForeignModItem>()

    val onExternBlockDecl: PsiElementPattern.Capture<PsiElement> =
        onItem<RsFunction>() or //FIXME: check if this is indeed a foreign function
            onItem<RsConstant>() or
            onItem<RsForeignModItem>()

    val onAnyItem: PsiElementPattern.Capture<PsiElement> = onItem<RsDocAndAttributeOwner>()

    val onExternCrate: PsiElementPattern.Capture<PsiElement> = onItem<RsExternCrateItem>()

    val onTrait: PsiElementPattern.Capture<PsiElement> = onItem<RsTraitItem>()

    val onDropFn: PsiElementPattern.Capture<PsiElement>
        get() {
            val dropTraitRef = psiElement<RsTraitRef>().withText("Drop")
            val implBlock = psiElement<RsImplItem>().withChild(dropTraitRef)
            return psiElement().withSuperParent(6, implBlock)
        }

    val onTestFn: PsiElementPattern.Capture<PsiElement> = onItem(psiElement<RsFunction>()
        .withChild(psiElement<RsOuterAttr>().withText("#[test]")))

    val onStructLike: PsiElementPattern.Capture<PsiElement> = onStruct or onEnum or onEnumVariant

    val inAnyLoop: PsiElementPattern.Capture<PsiElement> =
        psiElement().inside(
            true,
            psiElement<RsBlock>().withParent(
                or(
                    psiElement<RsForExpr>(),
                    psiElement<RsLoopExpr>(),
                    psiElement<RsWhileExpr>()
                )
            ),
            psiElement<RsLambdaExpr>()
        )

    val derivedTraitMetaItem: PsiElementPattern.Capture<RsMetaItem> =
        psiElement<RsMetaItem>().withSuperParent(
            2,
            psiElement()
                .withSuperParent<RsStructOrEnumItemElement>(2)
                .with("deriveCondition") { e -> e is RsMetaItem && e.name == "derive" }
        )

    /**
     * Supposed to capture outer attributes names, like `attribute` in `#[attribute(par1, par2)]`.
     */
    val nonStdOuterAttributeMetaItem: PsiElementPattern.Capture<RsMetaItem> =
        psiElement<RsMetaItem>()
            .withSuperParent(2, RsOuterAttributeOwner::class.java)
            .with("nonStdAttributeCondition") { e -> e.name !in STD_ATTRIBUTES }

    val lintAttributeMetaItem: PsiElementPattern.Capture<RsMetaItem> =
        psiElement<RsMetaItem>()
            .withParent(RsAttr::class.java)
            .with("lintAttributeCondition") { e -> e.name in LINT_ATTRIBUTES }

    val includeMacroLiteral: PsiElementPattern.Capture<RsLitExpr> = psiElement<RsLitExpr>()
        .withParent(psiElement<RsIncludeMacroArgument>())

    val pathAttrLiteral: PsiElementPattern.Capture<RsLitExpr> = psiElement<RsLitExpr>()
        .withParent(psiElement<RsMetaItem>()
            .withSuperParent(2, or(psiElement<RsModDeclItem>(), psiElement<RsModItem>()))
            .with("pathAttrCondition") { metaItem -> metaItem.name == "path" }
        )

    val whitespace: PsiElementPattern.Capture<PsiElement> = psiElement().whitespace()

    val error: PsiElementPattern.Capture<PsiErrorElement> = psiElement<PsiErrorElement>()

    val simplePathPattern: ElementPattern<PsiElement>
        get() {
            val simplePath = psiElement<RsPath>()
                .with(object : PatternCondition<RsPath>("SimplePath") {
                    override fun accepts(path: RsPath, context: ProcessingContext?): Boolean =
                        path.kind == PathKind.IDENTIFIER &&
                            path.path == null &&
                            path.typeQual == null &&
                            !path.hasColonColon &&
                            path.ancestorStrict<RsUseSpeck>() == null
                })
            return psiElement().withParent(simplePath)
        }

    /** `#[cfg()]` */
    private val onCfgAttributeMeta: PsiElementPattern.Capture<RsMetaItem> = metaItem("cfg")
        .withParent(RsAttr::class.java)

    /** `#[cfg_attr()]` */
    private val onCfgAttrAttributeMeta: PsiElementPattern.Capture<RsMetaItem> = metaItem("cfg_attr")
        .withParent(RsAttr::class.java)

    /** `#[doc(cfg())]` */
    private val onDocCfgAttributeMeta: PsiElementPattern.Capture<RsMetaItem> = metaItem("cfg")
        .withSuperParent(2, metaItem("doc"))
        .withSuperParent(3, RsAttr::class.java)

    /**
     * ```
     * #[cfg_attr(condition, attr)]
     *           //^
     * ```
     */
    private val onCfgAttrCondition: PsiElementPattern.Capture<RsMetaItem> = psiElement<RsMetaItem>()
        .withSuperParent(2, onCfgAttrAttributeMeta)
        .with("firstItem") { it, _ -> (it.parent as? RsMetaItemArgs)?.metaItemList?.firstOrNull() == it }

    val onCfgOrAttrFeature: PsiElementPattern.Capture<RsLitExpr> = psiElement<RsLitExpr>()
        .withParent(metaItem("feature"))
        .inside(onCfgAttributeMeta or onCfgAttrCondition or onDocCfgAttributeMeta)

    private inline fun <reified I : RsDocAndAttributeOwner> onItem(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withSuperParent<I>(META_ITEM_IDENTIFIER_DEPTH)
    }

    private fun onItem(pattern: ElementPattern<out RsDocAndAttributeOwner>): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withSuperParent(META_ITEM_IDENTIFIER_DEPTH, pattern)
    }

    private fun metaItem(key: String): PsiElementPattern.Capture<RsMetaItem> =
        psiElement<RsMetaItem>().withChild(
            psiElement<RsPath>().withText(key)
        )

    private class OnStatementBeginning(vararg startWords: String) : PatternCondition<PsiElement>("on statement beginning") {
        val myStartWords = startWords
        override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
            val prev = t.prevVisibleOrNewLine
            return if (myStartWords.isEmpty())
                prev == null || prev is PsiWhiteSpace || prev.node.elementType in STATEMENT_BOUNDARIES
            else
                prev != null && prev.node.text in myStartWords
        }
    }
}

inline fun <reified I : PsiElement> psiElement(): PsiElementPattern.Capture<I> {
    return psiElement(I::class.java)
}

inline fun <reified I : PsiElement> psiElement(contextName: String): PsiElementPattern.Capture<I> {
    return psiElement(I::class.java).with("putIntoContext") { e, context ->
        context?.put(contextName, e)
        true
    }
}

inline fun <reified I : PsiElement> PsiElementPattern.Capture<PsiElement>.withSuperParent(level: Int): PsiElementPattern.Capture<PsiElement> {
    return this.withSuperParent(level, I::class.java)
}

inline infix fun <reified I : PsiElement> ElementPattern<out I>.or(pattern: ElementPattern<out I>): PsiElementPattern.Capture<PsiElement> {
    return psiElement().andOr(this, pattern)
}

private val PsiElement.prevVisibleOrNewLine: PsiElement?
    get() = leftLeaves
        .filterNot { it is PsiComment || it is PsiErrorElement }
        .filter { it !is PsiWhiteSpace || it.textContains('\n') }
        .firstOrNull()

/**
 * Similar with [TreeElementPattern.afterSiblingSkipping]
 * but it uses [PsiElement.getPrevSibling] to get previous sibling elements
 * instead of [PsiElement.getChildren].
 */
fun <T : PsiElement, Self : PsiElementPattern<T, Self>> PsiElementPattern<T, Self>.withPrevSiblingSkipping(
    skip: ElementPattern<out T>,
    pattern: ElementPattern<out T>
): Self = with("withPrevSiblingSkipping") { e ->
    val sibling = e.leftSiblings.dropWhile { skip.accepts(it) }
        .firstOrNull() ?: return@with false
    pattern.accepts(sibling)
}

fun <T, Self : ObjectPattern<T, Self>> ObjectPattern<T, Self>.with(name: String, cond: (T) -> Boolean): Self =
    with(object : PatternCondition<T>(name) {
        override fun accepts(t: T, context: ProcessingContext?): Boolean = cond(t)
    })

fun <T, Self : ObjectPattern<T, Self>> ObjectPattern<T, Self>.with(name: String, cond: (T, ProcessingContext?) -> Boolean): Self =
    with(object : PatternCondition<T>(name) {
        override fun accepts(t: T, context: ProcessingContext?): Boolean = cond(t, context)
    })
