package org.shardscript.semantics.prelude

import org.shardscript.semantics.core.*
import org.shardscript.semantics.infer.DualFinPluginInstantiation
import org.shardscript.semantics.infer.SingleParentArgInstantiation
import org.shardscript.semantics.infer.Substitution

object DecimalMathOpMembers {
    val add = pluginAdd()
    val sub = pluginSub()
    val mul = pluginMul()
    val div = pluginDiv()
    val mod = pluginMod()
    val negate = pluginNegate()

    fun members(): Map<String, Symbol> = mapOf(
        BinaryOperator.Add.idStr to add,
        BinaryOperator.Sub.idStr to sub,
        BinaryOperator.Mul.idStr to mul,
        BinaryOperator.Div.idStr to div,
        BinaryOperator.Mod.idStr to mod,
        UnaryOperator.Negate.idStr to negate
    )

    private fun pluginAdd(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Add.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(outputTypeArg))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginSub(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Sub.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(outputTypeArg))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginMul(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Mul.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(outputTypeArg))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginDiv(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Div.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = MaxCostExpression(
            listOf(
                Lang.decimalTypeParam,
                inputTypeArg
            )
        )
        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(outputTypeArg))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginMod(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Mod.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = MaxCostExpression(
            listOf(
                Lang.decimalTypeParam,
                inputTypeArg
            )
        )
        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(outputTypeArg))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginNegate(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, UnaryOperator.Negate.idStr),
            SingleParentArgInstantiation
        )
        res.typeParams = listOf(Lang.decimalTypeParam)
        res.formalParams = listOf()

        val outputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(Lang.decimalTypeParam))
        val outputType = outputSubstitution.apply(Lang.decimalType)
        res.returnType = outputType
        res.costExpression = Lang.decimalTypeParam
        return res
    }
}

object DecimalOrderOpMembers {
    val greaterThan = pluginGreaterThan()
    val greaterThanOrEquals = pluginGreaterThanOrEquals()
    val lessThan = pluginLessThan()
    val lessThanOrEquals = pluginLessThanOrEquals()

    fun members(): Map<String, ParameterizedMemberPluginSymbol> = mapOf(
        BinaryOperator.GreaterThan.idStr to greaterThan,
        BinaryOperator.GreaterThanEqual.idStr to greaterThanOrEquals,
        BinaryOperator.LessThan.idStr to lessThan,
        BinaryOperator.LessThanEqual.idStr to lessThanOrEquals
    )

    private fun pluginGreaterThan(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.GreaterThan.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginGreaterThanOrEquals(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.GreaterThanEqual.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginLessThan(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.LessThan.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginLessThanOrEquals(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.LessThanEqual.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }
}

object DecimalEqualityOpMembers {
    val equals = pluginEquals()
    val notEquals = pluginNotEquals()

    fun members(): Map<String, ParameterizedMemberPluginSymbol> = mapOf(
        BinaryOperator.Equal.idStr to equals,
        BinaryOperator.NotEqual.idStr to notEquals
    )

    private fun pluginEquals(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.Equal.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }

    private fun pluginNotEquals(): ParameterizedMemberPluginSymbol {
        val res = ParameterizedMemberPluginSymbol(
            Lang.decimalType,
            Identifier(NotInSource, BinaryOperator.NotEqual.idStr),
            DualFinPluginInstantiation
        )
        val inputTypeArg = ImmutableFinTypeParameter(res, Lang.decimalInputTypeId)
        res.define(inputTypeArg.identifier, inputTypeArg)
        res.typeParams = listOf(Lang.decimalTypeParam, inputTypeArg)

        val inputSubstitution = Substitution(listOf(Lang.decimalTypeParam), listOf(inputTypeArg))
        val inputType = inputSubstitution.apply(Lang.decimalType)
        val formalParamId = Identifier(NotInSource, "other")
        val formalParam = FunctionFormalParameterSymbol(res, formalParamId, inputType)
        res.define(formalParamId, formalParam)
        res.formalParams = listOf(formalParam)

        val outputTypeArg = ProductCostExpression(
            listOf(
                CommonCostExpressions.twoPass,
                MaxCostExpression(
                    listOf(
                        Lang.decimalTypeParam,
                        inputTypeArg
                    )
                )
            )
        )
        res.returnType = Lang.booleanType

        res.costExpression = outputTypeArg
        return res
    }
}