package org.shardscript.semantics.infer

import org.shardscript.semantics.core.*
import org.shardscript.semantics.prelude.Lang
import kotlin.math.abs

object RangeInstantiation : SingleTypeInstantiation {
    override fun apply(
        ctx: SourceContext,
        errors: LanguageErrors,
        args: List<Ast>,
        rawTerminus: RawTerminus,
        identifier: Identifier,
        explicitTypeArgs: List<Type>
    ): SymbolInstantiation {
        if (explicitTypeArgs.isNotEmpty()) {
            // TODO: Fix this use of the as keyword
            errors.add(ctx, CannotExplicitlyInstantiate(rawTerminus as Symbol))
        }
        val allValid = args.all {
            val valid = it is IntLiteralAst
            if (!valid) {
                errors.add(it.ctx, InvalidRangeArg)
            }
            valid
        }
        if (allValid) {
            val intArgs = args.map { it as IntLiteralAst }
            if (intArgs.size == 2) {
                val first = intArgs[0].canonicalForm
                val second = intArgs[1].canonicalForm
                val min = first.coerceAtMost(second)
                val max = first.coerceAtLeast(second)
                val fin = FinTypeSymbol(abs(max - min).toLong())
                val substitution = Substitution(rawTerminus.typeParams, listOf(fin))
                return substitution.apply(rawTerminus)
            } else {
                errors.add(ctx, IncorrectNumberOfArgs(2, args.size))
            }
        }
        throw LanguageException(errors.toSet())
    }
}

object RandomInstantiation : SingleTypeInstantiation {
    override fun apply(
        ctx: SourceContext,
        errors: LanguageErrors,
        args: List<Ast>,
        rawTerminus: RawTerminus,
        identifier: Identifier,
        explicitTypeArgs: List<Type>
    ): SymbolInstantiation {
        val parameterizedStaticPluginSymbol = rawTerminus as ParameterizedStaticPluginSymbol
        val res = if (explicitTypeArgs.isNotEmpty()) {
            if (explicitTypeArgs.size != 1) {
                errors.add(ctx, IncorrectNumberOfTypeArgs(1, explicitTypeArgs.size))
                throw LanguageException(errors.toSet())
            } else {
                validateSubstitution(ctx, errors, rawTerminus.typeParams.first(), explicitTypeArgs.first())
                val substitution = Substitution(rawTerminus.typeParams, explicitTypeArgs)
                substitution.apply(rawTerminus)
            }
        } else {
            val inOrderParameters = rawTerminus.typeParams
            val parameterSet = inOrderParameters.toSet()
            if (parameterizedStaticPluginSymbol.formalParams.size == args.size) {
                val constraints: MutableList<Constraint<TypeParameter, Type>> = ArrayList()
                parameterizedStaticPluginSymbol.formalParams.zip(args).forEach {
                    constraints.addAll(
                        constrainSymbol(
                            ctx,
                            parameterSet,
                            it.first.ofTypeSymbol,
                            it.second.readType(),
                            errors
                        )
                    )
                }
                val substitution = createSubstitution(ctx, constraints, parameterSet, inOrderParameters, errors)
                substitution.apply(rawTerminus)
            } else {
                errors.add(
                    ctx,
                    IncorrectNumberOfArgs(parameterizedStaticPluginSymbol.formalParams.size, args.size)
                )
                throw LanguageException(errors.toSet())
            }
        }
        return when (getQualifiedName((res.substitutionChain).replayArgs().first() as Symbol)) {
            Lang.intId.name -> res
            else -> {
                errors.add(ctx, RandomRequiresIntLong)
                throw LanguageException(errors.toSet())
            }
        }
    }
}