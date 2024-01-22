package org.shardscript.semantics.prelude

import org.shardscript.semantics.core.*
import org.shardscript.semantics.infer.*

object SetTypes {
    val setContains = createSetContainsFunction()
    val mutableSetContains = createMutableSetContainsFunction()
    val mutableSetAdd = createMutableSetAddFunction()
    val mutableSetRemove = createMutableSetRemoveFunction()
    val mutableSetToSet = createMutableSetToImmutableSetPlugin()

    private fun createSetContainsFunction(): ParameterizedMemberPluginSymbol {
        val containsId = Identifier(NotInSource, CollectionMethods.Contains.idStr)
        val containsMemberFunction = ParameterizedMemberPluginSymbol(
            Lang.setType,
            containsId,
            SingleParentArgInstantiation
        )
        containsMemberFunction.typeParams = listOf(Lang.setElementTypeParam)
        containsMemberFunction.costExpression = ConstantFinTypeSymbol
        val containsFormalParamId = Identifier(NotInSource, "element")
        val containsFormalParam =
            FunctionFormalParameterSymbol(containsMemberFunction, containsFormalParamId, Lang.setElementTypeParam)
        containsMemberFunction.define(containsFormalParamId, containsFormalParam)

        containsMemberFunction.formalParams = listOf(containsFormalParam)
        containsMemberFunction.returnType = Lang.booleanType
        Lang.setType.define(containsId, containsMemberFunction)
        return containsMemberFunction
    }

    private fun createMutableSetContainsFunction(): ParameterizedMemberPluginSymbol {
        val containsId = Identifier(NotInSource, CollectionMethods.Contains.idStr)
        val containsMemberFunction = ParameterizedMemberPluginSymbol(
            Lang.mutableSetType,
            containsId,
            SingleParentArgInstantiation
        )
        containsMemberFunction.typeParams = listOf(Lang.mutableSetElementTypeParam)
        containsMemberFunction.costExpression = ConstantFinTypeSymbol
        val containsFormalParamId = Identifier(NotInSource, "element")
        val containsFormalParam =
            FunctionFormalParameterSymbol(containsMemberFunction, containsFormalParamId, Lang.mutableSetElementTypeParam)
        containsMemberFunction.define(containsFormalParamId, containsFormalParam)

        containsMemberFunction.formalParams = listOf(containsFormalParam)
        containsMemberFunction.returnType = Lang.booleanType
        Lang.mutableSetType.define(containsId, containsMemberFunction)
        return containsMemberFunction
    }

    private fun createMutableSetAddFunction(): ParameterizedMemberPluginSymbol {
        val addId = Identifier(NotInSource, CollectionMethods.InsertElement.idStr)
        val addMemberFunction = ParameterizedMemberPluginSymbol(
            Lang.mutableSetType,
            addId,
            SingleParentArgInstantiation
        )
        addMemberFunction.typeParams = listOf(Lang.mutableSetElementTypeParam)
        addMemberFunction.costExpression = ConstantFinTypeSymbol
        val addFormalParamId = Identifier(NotInSource, "element")
        val addFormalParam = FunctionFormalParameterSymbol(addMemberFunction, addFormalParamId, Lang.mutableSetElementTypeParam)
        addMemberFunction.define(addFormalParamId, addFormalParam)

        addMemberFunction.formalParams = listOf(addFormalParam)
        addMemberFunction.returnType = Lang.unitObject
        Lang.mutableSetType.define(addId, addMemberFunction)
        return addMemberFunction
    }

    private fun createMutableSetRemoveFunction(): ParameterizedMemberPluginSymbol {
        val removeId = Identifier(NotInSource, CollectionMethods.Remove.idStr)
        val removeMemberFunction = ParameterizedMemberPluginSymbol(
            Lang.mutableSetType,
            removeId,
            SingleParentArgInstantiation
        )
        removeMemberFunction.typeParams = listOf(Lang.mutableSetElementTypeParam)
        removeMemberFunction.costExpression = ConstantFinTypeSymbol
        val removeFormalParamId = Identifier(NotInSource, "element")
        val removeFormalParam =
            FunctionFormalParameterSymbol(removeMemberFunction, removeFormalParamId, Lang.mutableSetElementTypeParam)
        removeMemberFunction.define(removeFormalParamId, removeFormalParam)

        removeMemberFunction.formalParams = listOf(removeFormalParam)
        removeMemberFunction.returnType = Lang.unitObject
        Lang.mutableSetType.define(removeId, removeMemberFunction)
        return removeMemberFunction
    }

    fun createMutableSetToImmutableSetPlugin(): ParameterizedMemberPluginSymbol {
        val plugin = ParameterizedMemberPluginSymbol(
            Lang.mutableSetType,
            Identifier(NotInSource, CollectionMethods.ToImmutableSet.idStr),
            DoubleParentArgInstantiation
        )
        plugin.typeParams = listOf(Lang.mutableSetElementTypeParam, Lang.mutableSetFinTypeParam)
        plugin.formalParams = listOf()
        val outputSubstitution =
            Substitution(Lang.setType.typeParams, listOf(Lang.mutableSetElementTypeParam, Lang.mutableSetFinTypeParam))
        val outputType = outputSubstitution.apply(Lang.setType)
        plugin.returnType = outputType

        plugin.costExpression =
            ProductCostExpression(
                listOf(
                    CommonCostExpressions.twoPass,
                    Lang.mutableSetFinTypeParam
                )
            )
        Lang.mutableSetType.define(plugin.identifier, plugin)
        return plugin
    }

    fun setCollectionType() {
        Lang.setType.define(Lang.setElementTypeId, Lang.setElementTypeParam)
        Lang.setType.define(Lang.setFinTypeId, Lang.setFinTypeParam)
        Lang.setType.typeParams = listOf(Lang.setElementTypeParam, Lang.setFinTypeParam)
        Lang.setType.modeSelector = { _ ->
            ImmutableBasicTypeMode
        }

        val sizeId = Identifier(NotInSource, CollectionFields.Size.idStr)
        val sizeFieldSymbol = PlatformFieldSymbol(
            Lang.setType,
            sizeId,
            Lang.intType
        ) { value ->
            (value as SetValue).fieldSize()
        }

        Lang.setType.define(sizeId, sizeFieldSymbol)
        Lang.setType.fields = listOf(sizeFieldSymbol)
    }

    fun mutableSetCollectionType() {
        Lang.mutableSetType.define(Lang.mutableSetElementTypeId, Lang.mutableSetElementTypeParam)
        Lang.mutableSetType.define(Lang.mutableSetFinTypeId, Lang.mutableSetFinTypeParam)
        Lang.mutableSetType.typeParams = listOf(Lang.mutableSetElementTypeParam, Lang.mutableSetFinTypeParam)
        Lang.mutableSetType.modeSelector = { args ->
            when (val fin = args[1]) {
                is FinTypeSymbol -> {
                    MutableBasicTypeMode(fin.magnitude)
                }

                else -> {
                    ImmutableBasicTypeMode
                }
            }
        }

        val sizeId = Identifier(NotInSource, CollectionFields.Size.idStr)
        val sizeFieldSymbol = PlatformFieldSymbol(
            Lang.mutableSetType,
            sizeId,
            Lang.intType
        ) { value ->
            (value as SetValue).fieldSize()
        }

        Lang.mutableSetType.define(sizeId, sizeFieldSymbol)
        Lang.mutableSetType.fields = listOf(sizeFieldSymbol)
    }
}