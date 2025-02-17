package moirai.semantics.prelude

import moirai.semantics.core.*
import moirai.semantics.infer.*

internal object Lang {
    val prelude = SymbolTable(NullSymbolTable)

    val unitId = Identifier(NotInSource, "Unit")
    val booleanId = Identifier(NotInSource, "Boolean")
    val intId = Identifier(NotInSource, "Int")
    val charId = Identifier(NotInSource, "Char")
    val stringId = Identifier(NotInSource, "String")
    val stringTypeId = Identifier(NotInSource, "O")
    val stringInputTypeId = Identifier(NotInSource, "P")

    val itId = Identifier(NotInSource, "it")

    val decimalId = Identifier(NotInSource, "Decimal")
    private val decimalTypeId = Identifier(NotInSource, "O")
    val decimalInputTypeId = Identifier(NotInSource, "P")

    val listId = Identifier(NotInSource, "List")
    private val listElementTypeId = Identifier(NotInSource, "E")
    private val listFinTypeId = Identifier(NotInSource, "O")
    val listInputFinTypeId = Identifier(NotInSource, "P")

    val mutableListId = Identifier(NotInSource, "MutableList")
    private val mutableListElementTypeId = Identifier(NotInSource, "E")
    private val mutableListFinTypeId = Identifier(NotInSource, "O")
    val mutableListInputFinTypeId = Identifier(NotInSource, "P")

    val pairId = Identifier(NotInSource, "Pair")
    private val pairFirstTypeId = Identifier(NotInSource, "A")
    private val pairSecondTypeId = Identifier(NotInSource, "B")
    val pairFirstId = Identifier(NotInSource, "first")
    val pairSecondId = Identifier(NotInSource, "second")

    val dictionaryId = Identifier(NotInSource, "Dictionary")
    private val dictionaryKeyTypeId = Identifier(NotInSource, "K")
    private val dictionaryValueTypeId = Identifier(NotInSource, "V")
    private val dictionaryFinTypeId = Identifier(NotInSource, "O")
    val dictionaryInputFinTypeId = Identifier(NotInSource, "P")

    val mutableDictionaryId = Identifier(NotInSource, "MutableDictionary")
    private val mutableDictionaryKeyTypeId = Identifier(NotInSource, "K")
    private val mutableDictionaryValueTypeId = Identifier(NotInSource, "V")
    private val mutableDictionaryFinTypeId = Identifier(NotInSource, "O")
    val mutableDictionaryInputFinTypeId = Identifier(NotInSource, "P")

    val setId = Identifier(NotInSource, "Set")
    private val setElementTypeId = Identifier(NotInSource, "E")
    private val setFinTypeId = Identifier(NotInSource, "O")
    val setInputFinTypeId = Identifier(NotInSource, "P")

    val mutableSetId = Identifier(NotInSource, "MutableSet")
    private val mutableSetElementTypeId = Identifier(NotInSource, "E")
    private val mutableSetFinTypeId = Identifier(NotInSource, "O")
    val mutableSetInputFinTypeId = Identifier(NotInSource, "P")

    private val optionId = Identifier(NotInSource, "Option")
    private val optionTypeParamId = Identifier(NotInSource, "A")
    private val someId = Identifier(NotInSource, "Some")
    private val someValueId = Identifier(NotInSource, "value")
    private val noneId = Identifier(NotInSource, "None")

    private val eitherId = Identifier(NotInSource, "Either")
    private val eitherLeftTypeParamId = Identifier(NotInSource, "L")
    private val eitherRightTypeParamId = Identifier(NotInSource, "R")
    private val leftId = Identifier(NotInSource, "Left")
    private val leftValueId = Identifier(NotInSource, "value")
    private val rightId = Identifier(NotInSource, "Right")
    private val rightValueId = Identifier(NotInSource, "value")

    val rangeId = Identifier(NotInSource, "range")
    val rangeTypeId = Identifier(NotInSource, "O")
    val randomId = Identifier(NotInSource, "random")
    val randomTypeId = Identifier(NotInSource, "A")

    const val INT_FIN: Long = (Int.MIN_VALUE.toString().length).toLong()
    val unitFin: Long = unitId.name.length.toLong()
    const val BOOL_FIN: Long = false.toString().length.toLong()
    const val CHAR_FIN: Long = 1L

    // Unit
    val unitObject = PlatformObjectType(
        unitId,
        unitFeatureSupport
    )

    // Boolean
    val booleanType = BasicType(
        booleanId
    )

    // Integer
    val intType = BasicType(
        intId
    )

    // Char
    val charType = BasicType(
        charId
    )

    // Decimal
    val decimalType = ParameterizedBasicType(
        decimalId,
        DecimalInstantiationValidation(),
        userTypeFeatureSupport
    )
    val decimalTypeParam = FinTypeParameter("${decimalId.name}.${decimalTypeId.name}", decimalTypeId)
    val ascribeFinTypeParameterQualifiedName = "${decimalId.name}.${DecimalMethods.AscribeFin.idStr}.${decimalInputTypeId.name}"

    // List
    val listType = ParameterizedBasicType(
        listId,
        ListInstantiationValidation(),
        immutableOrderedFeatureSupport
    )
    val listElementTypeParam = StandardTypeParameter("${listId.name}.${listElementTypeId.name}", listElementTypeId)
    val listFinTypeParam = FinTypeParameter("${listId.name}.${listFinTypeId.name}", listFinTypeId)

    // MutableList
    val mutableListType = ParameterizedBasicType(
        mutableListId,
        MutableListInstantiationValidation(),
        immutableOrderedFeatureSupport
    )
    val mutableListElementTypeParam = StandardTypeParameter("${mutableListId.name}.${mutableListElementTypeId.name}", mutableListElementTypeId)
    val mutableListFinTypeParam = FinTypeParameter("${mutableListId.name}.${mutableListFinTypeId.name}", mutableListFinTypeId)

    // String
    val stringType = ParameterizedBasicType(
        stringId,
        StringInstantiationValidation(),
        userTypeFeatureSupport
    )

    val stringTypeParam = FinTypeParameter("${stringId.name}.${stringTypeId.name}", stringTypeId)

    // Pair
    private val pairType = ParameterizedRecordType(
        prelude,
        pairId.name,
        pairId,
        userTypeFeatureSupport
    )
    private val pairFirstType = StandardTypeParameter("${pairId.name}.${pairFirstTypeId.name}", pairFirstTypeId)
    private val pairSecondType = StandardTypeParameter("${pairId.name}.${pairSecondTypeId.name}", pairSecondTypeId)

    val dictionaryType = ParameterizedBasicType(
        dictionaryId,
        DictionaryInstantiationValidation(pairType),
        immutableUnorderedFeatureSupport
    )

    val dictionaryKeyTypeParam = StandardTypeParameter("${dictionaryId.name}.${dictionaryKeyTypeId.name}", dictionaryKeyTypeId)
    val dictionaryKeyParameterHashCodeCost = ParameterHashCodeCost(dictionaryKeyTypeParam)

    val dictionaryValueTypeParam = StandardTypeParameter("${dictionaryId.name}.${dictionaryValueTypeId.name}", dictionaryValueTypeId)
    val dictionaryFinTypeParam = FinTypeParameter("${dictionaryId.name}.${dictionaryFinTypeId.name}", dictionaryFinTypeId)

    val mutableDictionaryType = ParameterizedBasicType(
        mutableDictionaryId,
        MutableDictionaryInstantiationValidation(pairType),
        immutableUnorderedFeatureSupport
    )

    val mutableDictionaryKeyTypeParam = StandardTypeParameter("${mutableDictionaryId.name}.${mutableDictionaryKeyTypeId.name}", mutableDictionaryKeyTypeId)
    val mutableDictionaryKeyParameterHashCodeCost = ParameterHashCodeCost(mutableDictionaryKeyTypeParam)

    val mutableDictionaryValueTypeParam = StandardTypeParameter("${mutableDictionaryId.name}.${mutableDictionaryValueTypeId.name}", mutableDictionaryValueTypeId)
    val mutableDictionaryFinTypeParam = FinTypeParameter("${mutableDictionaryId.name}.${mutableDictionaryFinTypeId.name}", mutableDictionaryFinTypeId)

    val setType = ParameterizedBasicType(
        setId,
        SetInstantiationValidation(),
        immutableUnorderedFeatureSupport
    )

    val setElementTypeParam = StandardTypeParameter("${setId.name}.${setElementTypeId.name}", setElementTypeId)
    val setParameterHashCodeCost = ParameterHashCodeCost(setElementTypeParam)

    val setFinTypeParam = FinTypeParameter("${setId.name}.${setFinTypeId.name}", setFinTypeId)

    val mutableSetType = ParameterizedBasicType(
        mutableSetId,
        MutableSetInstantiationValidation(),
        immutableUnorderedFeatureSupport
    )

    val mutableSetElementTypeParam = StandardTypeParameter("${mutableSetId.name}.${mutableSetElementTypeId.name}", mutableSetElementTypeId)
    val mutableSetParameterHashCodeCost = ParameterHashCodeCost(mutableSetElementTypeParam)

    val mutableSetFinTypeParam = FinTypeParameter("${mutableSetId.name}.${mutableSetFinTypeId.name}", mutableSetFinTypeId)

    init {
        IntegerMathOpMembers.members().forEach { (name, plugin) ->
            intType.define(Identifier(NotInSource, name), plugin)
        }
        IntegerOrderOpMembers.members().forEach { (name, plugin) ->
            intType.define(Identifier(NotInSource, name), plugin)
        }
        IntegerEqualityOpMembers.members().forEach { (name, plugin) ->
            intType.define(Identifier(NotInSource, name), plugin)
        }

        BooleanEqualityOpMembers.members().forEach { (name, plugin) ->
            booleanType.define(Identifier(NotInSource, name), plugin)
        }
        ValueLogicalOpMembers.members().forEach { (name, plugin) ->
            booleanType.define(Identifier(NotInSource, name), plugin)
        }

        CharEqualityOpMembers.members().forEach { (name, plugin) ->
            charType.define(Identifier(NotInSource, name), plugin)
        }

        decimalType.defineType(decimalTypeId, decimalTypeParam)
        decimalType.typeParams = listOf(decimalTypeParam)
        decimalType.fields = listOf()

        DecimalMathOpMembers.members().forEach { (name, plugin) ->
            decimalType.define(Identifier(NotInSource, name), plugin)
        }
        DecimalOrderOpMembers.members().forEach { (name, plugin) ->
            decimalType.define(Identifier(NotInSource, name), plugin)
        }
        DecimalEqualityOpMembers.members().forEach { (name, plugin) ->
            decimalType.define(Identifier(NotInSource, name), plugin)
        }
        decimalType.define(Identifier(NotInSource, DecimalMethods.AscribeFin.idStr), DecimalMethodMembers.ascribe)

        // List
        listType.defineType(listElementTypeId, listElementTypeParam)
        listType.defineType(listFinTypeId, listFinTypeParam)
        listType.typeParams = listOf(listElementTypeParam, listFinTypeParam)
        
        ListTypes.listCollectionType()
        
        // MutableList
        mutableListType.defineType(mutableListElementTypeId, mutableListElementTypeParam)
        mutableListType.defineType(mutableListFinTypeId, mutableListFinTypeParam)
        mutableListType.typeParams = listOf(mutableListElementTypeParam, mutableListFinTypeParam)
        
        ListTypes.mutableListCollectionType()

        // Dictionary
        dictionaryType.defineType(dictionaryKeyTypeId, dictionaryKeyTypeParam)
        dictionaryType.defineType(dictionaryValueTypeId, dictionaryValueTypeParam)
        dictionaryType.defineType(dictionaryFinTypeId, dictionaryFinTypeParam)
        dictionaryType.typeParams =
            listOf(dictionaryKeyTypeParam, dictionaryValueTypeParam, dictionaryFinTypeParam)

        DictionaryTypes.dictionaryCollectionType()

        // MutableDictionary
        mutableDictionaryType.defineType(mutableDictionaryKeyTypeId, mutableDictionaryKeyTypeParam)
        mutableDictionaryType.defineType(mutableDictionaryValueTypeId, mutableDictionaryValueTypeParam)
        mutableDictionaryType.defineType(mutableDictionaryFinTypeId, mutableDictionaryFinTypeParam)
        mutableDictionaryType.typeParams =
            listOf(
                mutableDictionaryKeyTypeParam,
                mutableDictionaryValueTypeParam,
                mutableDictionaryFinTypeParam
            )

        DictionaryTypes.mutableDictionaryCollectionType()

        // Set
        setType.defineType(setElementTypeId, setElementTypeParam)
        setType.defineType(setFinTypeId, setFinTypeParam)
        setType.typeParams = listOf(setElementTypeParam, setFinTypeParam)
        
        SetTypes.setCollectionType()

        // MutableSet
        mutableSetType.defineType(mutableSetElementTypeId, mutableSetElementTypeParam)
        mutableSetType.defineType(mutableSetFinTypeId, mutableSetFinTypeParam)
        mutableSetType.typeParams = listOf(mutableSetElementTypeParam, mutableSetFinTypeParam)
        
        SetTypes.mutableSetCollectionType()

        StringTypes.stringType()

        pairType.typeParams = listOf(pairFirstType, pairSecondType)
        val pairFirstField = FieldSymbol(pairType, pairFirstId, pairFirstType, mutable = false)
        val pairSecondField = FieldSymbol(pairType, pairSecondId, pairSecondType, mutable = false)
        pairType.fields = listOf(pairFirstField, pairSecondField)
        pairType.define(pairFirstId, pairFirstField)
        pairType.define(pairSecondId, pairSecondField)

        // Option
        val optionType = PlatformSumType(optionId, userTypeFeatureSupport)
        val optionTypeParam = StandardTypeParameter("${optionId.name}.${optionTypeParamId.name}", optionTypeParamId)

        val someType = PlatformSumRecordType(prelude, optionType, someId, noFeatureSupport)
        val noneType = PlatformSumObjectType(optionType, noneId, noFeatureSupport)

        optionType.typeParams = listOf(optionTypeParam)
        optionType.memberTypes = listOf(someType, noneType)

        someType.typeParams = listOf(optionTypeParam)
        val valueField = FieldSymbol(someType, someValueId, optionTypeParam, mutable = false)
        someType.fields = listOf(valueField)
        someType.define(someValueId, valueField)
        
        // Either
        val eitherType = PlatformSumType(eitherId, userTypeFeatureSupport)
        val eitherLeftTypeParam = StandardTypeParameter("${eitherId.name}.${eitherLeftTypeParamId.name}", eitherLeftTypeParamId)
        val eitherRightTypeParam = StandardTypeParameter("${eitherId.name}.${eitherRightTypeParamId.name}", eitherRightTypeParamId)

        val leftType = PlatformSumRecordType(prelude, eitherType, leftId, noFeatureSupport)
        val rightType = PlatformSumRecordType(prelude, eitherType, rightId, noFeatureSupport)

        eitherType.typeParams = listOf(eitherLeftTypeParam, eitherRightTypeParam)
        eitherType.memberTypes = listOf(leftType, rightType)

        leftType.typeParams = listOf(eitherLeftTypeParam)
        val leftValueField = FieldSymbol(leftType, leftValueId, eitherLeftTypeParam, mutable = false)
        leftType.fields = listOf(leftValueField)
        leftType.define(leftValueId, leftValueField)

        rightType.typeParams = listOf(eitherRightTypeParam)
        val rightValueField = FieldSymbol(rightType, rightValueId, eitherRightTypeParam, mutable = false)
        rightType.fields = listOf(rightValueField)
        rightType.define(rightValueId, rightValueField)

        // Compose output
        prelude.defineType(unitId, unitObject)
        prelude.defineType(booleanId, booleanType)
        prelude.defineType(intId, intType)
        prelude.defineType(decimalId, decimalType)
        prelude.defineType(listId, listType)
        prelude.defineType(mutableListId, mutableListType)
        prelude.defineType(pairId, pairType)
        prelude.defineType(dictionaryId, dictionaryType)
        prelude.defineType(mutableDictionaryId, mutableDictionaryType)
        prelude.defineType(setId, setType)
        prelude.defineType(mutableSetId, mutableSetType)
        prelude.defineType(charId, charType)
        prelude.defineType(stringId, stringType)
        prelude.defineType(optionId, optionType)
        prelude.defineType(someId, someType)
        prelude.defineType(noneId, noneType)
        prelude.defineType(eitherId, eitherType)
        prelude.defineType(leftId, leftType)
        prelude.defineType(rightId, rightType)
        prelude.define(rangeId, StaticPlugins.rangePlugin)
        prelude.define(randomId, StaticPlugins.randomPlugin)
    }
}
