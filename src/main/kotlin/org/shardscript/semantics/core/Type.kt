package org.shardscript.semantics.core

import org.shardscript.semantics.infer.SubstitutionChain

sealed interface Type

sealed interface TerminusType: Type, RawTerminus

data object ErrorType : Type

class FunctionTypeSymbol(
    val formalParamTypes: List<Type>,
    val returnType: Type
) : Type

sealed class TypeParameter : Type

class StandardTypeParameter(
    val qualifiedName: String,
    val identifier: Identifier
) : TypeParameter(), Type

class ImmutableFinTypeParameter(
    val qualifiedName: String,
    val identifier: Identifier
) : TypeParameter(), CostExpression, Type {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class MutableFinTypeParameter(
    val qualifiedName: String,
    val identifier: Identifier
) : TypeParameter(), CostExpression, Type {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class FinTypeSymbol(val magnitude: Long) : CostExpression, Type {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

data object ConstantFinTypeSymbol : CostExpression, Type {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class SumCostExpression(val children: List<CostExpression>) : CostExpression {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class ProductCostExpression(val children: List<CostExpression>) : CostExpression {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class MaxCostExpression(val children: List<CostExpression>) : CostExpression {
    override fun <R> accept(visitor: CostExpressionVisitor<R>): R {
        return visitor.visit(this)
    }
}

class TypeInstantiation(
    val substitutionChain: SubstitutionChain<TerminusType>
) : Type

class PlatformObjectSymbol(
    val identifier: Identifier,
    private val symbolTable: SymbolTable = SymbolTable(NullSymbolTable)
) : Type, Scope by symbolTable

class ObjectSymbol(
    val qualifiedName: String,
    val identifier: Identifier,
    val featureSupport: FeatureSupport
) : Type

class GroundRecordTypeSymbol(
    definitionScopeForTypeChecking: Scope,
    val qualifiedName: String,
    val identifier: Identifier,
    private val symbolTable: SymbolTable = SymbolTable(definitionScopeForTypeChecking)
) : Type, Scope by symbolTable {
    lateinit var fields: List<FieldSymbol>
}

class ParameterizedRecordTypeSymbol(
    definitionScopeForTypeChecking: Scope,
    val qualifiedName: String,
    val identifier: Identifier,
    val featureSupport: FeatureSupport,
    private val symbolTable: SymbolTable = SymbolTable(definitionScopeForTypeChecking)
) : TerminusType, Scope by symbolTable {
    override lateinit var typeParams: List<TypeParameter>
    lateinit var fields: List<FieldSymbol>
}

class BasicTypeSymbol(
    val identifier: Identifier,
    private val symbolTable: SymbolTable = SymbolTable(NullSymbolTable)
) : Type, Scope by symbolTable

class ParameterizedBasicTypeSymbol(
    val identifier: Identifier,
    val instantiation: SingleTypeInstantiation<TerminusType, TypeInstantiation>,
    val featureSupport: FeatureSupport,
    private val symbolTable: SymbolTable = SymbolTable(NullSymbolTable)
) : TerminusType, Scope by symbolTable {
    override lateinit var typeParams: List<TypeParameter>
    lateinit var modeSelector: (List<Type>) -> BasicTypeMode
    lateinit var fields: List<PlatformFieldSymbol>
}
