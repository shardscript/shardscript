package moirai.semantics.prelude

import moirai.semantics.core.*
import moirai.semantics.infer.RandomInstantiationValidation
import moirai.semantics.infer.RangeInstantiationValidation
import moirai.semantics.infer.Substitution

internal object StaticPlugins {
    val rangePlugin = createRangePlugin()
    val randomPlugin = createRandomPlugin()

    private fun createRangePlugin(): ParameterizedStaticPluginSymbol {
        val rangePlugin = ParameterizedStaticPluginSymbol(
            Lang.prelude,
            Lang.rangeId,
            RangeInstantiationValidation
        )

        val rangeTypeParam = FinTypeParameter("${Lang.rangeId.name}.${Lang.rangeTypeId.name}", Lang.rangeTypeId)
        rangePlugin.defineType(Lang.rangeTypeId, rangeTypeParam)
        rangePlugin.typeParams = listOf(rangeTypeParam)

        val beginFormalParamId = Identifier(NotInSource, "begin")
        val beginFormalParam = FunctionFormalParameterSymbol(rangePlugin, beginFormalParamId, Lang.intType)
        rangePlugin.define(beginFormalParamId, beginFormalParam)

        val endFormalParamId = Identifier(NotInSource, "end")
        val endFormalParam = FunctionFormalParameterSymbol(rangePlugin, endFormalParamId, Lang.intType)
        rangePlugin.define(endFormalParamId, endFormalParam)
        rangePlugin.formalParams = listOf(beginFormalParam, endFormalParam)

        val outputSubstitution = Substitution(Lang.listType.typeParams, listOf(Lang.intType, rangeTypeParam))
        val outputType = outputSubstitution.apply(Lang.listType)
        rangePlugin.returnType = outputType

        rangePlugin.costExpression = rangeTypeParam
        return rangePlugin
    }

    private fun createRandomPlugin(): ParameterizedStaticPluginSymbol {
        val randomPlugin = ParameterizedStaticPluginSymbol(
            Lang.prelude,
            Lang.randomId,
            RandomInstantiationValidation
        )

        val randomTypeParam = StandardTypeParameter("${Lang.randomId.name}.${Lang.randomTypeId.name}", Lang.randomTypeId)
        randomPlugin.defineType(Lang.randomTypeId, randomTypeParam)
        randomPlugin.typeParams = listOf(randomTypeParam)

        val beginFormalParamId = Identifier(NotInSource, "offset")
        val beginFormalParam = FunctionFormalParameterSymbol(randomPlugin, beginFormalParamId, randomTypeParam)
        randomPlugin.define(beginFormalParamId, beginFormalParam)

        val endFormalParamId = Identifier(NotInSource, "limit")
        val endFormalParam = FunctionFormalParameterSymbol(randomPlugin, endFormalParamId, randomTypeParam)
        randomPlugin.define(endFormalParamId, endFormalParam)
        randomPlugin.formalParams = listOf(beginFormalParam, endFormalParam)
        randomPlugin.returnType = randomTypeParam

        randomPlugin.costExpression = ConstantFin(DefaultFin)
        return randomPlugin
    }
}