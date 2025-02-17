package moirai.semantics.core

import moirai.semantics.infer.Substitution

internal interface Scope {
    fun define(identifier: Identifier, definition: Symbol)
    fun exists(signifier: Signifier): Boolean
    fun existsHere(signifier: Signifier): Boolean
    fun fetch(signifier: Signifier): Symbol
    fun fetchHere(signifier: Signifier): Symbol
    fun defineType(identifier: Identifier, definition: Type)
    fun typeExists(signifier: Signifier): Boolean
    fun typeExistsHere(signifier: Signifier): Boolean
    fun fetchType(signifier: Signifier): Type
    fun fetchTypeHere(signifier: Signifier): Type
}

internal object NullSymbolTable : Scope {
    override fun define(identifier: Identifier, definition: Symbol) {
        langThrow(identifier.ctx, IdentifierCouldNotBeDefined(toError(identifier)))
    }

    override fun exists(signifier: Signifier): Boolean = false

    override fun existsHere(signifier: Signifier): Boolean = false

    override fun fetch(signifier: Signifier): Symbol {
        langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
    }

    override fun fetchHere(signifier: Signifier): Symbol {
        langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
    }

    override fun defineType(identifier: Identifier, definition: Type) {
        langThrow(identifier.ctx, IdentifierCouldNotBeDefined(toError(identifier)))
    }

    override fun typeExists(signifier: Signifier): Boolean = false

    override fun typeExistsHere(signifier: Signifier): Boolean = false

    override fun fetchType(signifier: Signifier): Type {
        langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
    }

    override fun fetchTypeHere(signifier: Signifier): Type {
        langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
    }
}

internal class SymbolTable(private val parent: Scope) : Scope {
    private val symbolTable: MutableMap<String, Symbol> = HashMap()
    private val typeTable: MutableMap<String, Type> = HashMap()

    fun symbolsToMap(): Map<String, Symbol> = symbolTable.toMap()
    fun typesToMap(): Map<String, Type> = typeTable.toMap()

    override fun define(identifier: Identifier, definition: Symbol) {
        if (symbolTable.containsKey(identifier.name) || typeTable.containsKey(identifier.name)) {
            langThrow(identifier.ctx, IdentifierAlreadyExists(toError(identifier)))
        } else {
            symbolTable[identifier.name] = definition
            typeTable[identifier.name] = ErrorType
        }
    }

    override fun exists(signifier: Signifier): Boolean =
        when (signifier) {
            is Identifier -> symbolTable.containsKey(signifier.name) || parent.exists(signifier)
            is FunctionTypeLiteral -> signifier.formalParamTypes.all { exists(it) } && exists(signifier.returnType)
            is ParameterizedSignifier -> exists(signifier.tti) && signifier.args.all { exists(it) }
            is ImplicitTypeLiteral -> false
            is FinLiteral -> false
            is InvokeSignifier -> false
            is NamedCost -> false
        }

    override fun existsHere(signifier: Signifier): Boolean =
        when (signifier) {
            is Identifier -> symbolTable.containsKey(signifier.name)
            is FunctionTypeLiteral -> signifier.formalParamTypes.all { exists(it) } && exists(signifier.returnType)
            is ParameterizedSignifier -> existsHere(signifier.tti) && signifier.args.all { exists(it) }
            is ImplicitTypeLiteral -> false
            is FinLiteral -> false
            is InvokeSignifier -> false
            is NamedCost -> false
        }

    override fun fetch(signifier: Signifier): Symbol =
        when (signifier) {
            is Identifier -> {
                if (symbolTable.containsKey(signifier.name)) {
                    symbolTable[signifier.name]!!
                } else {
                    parent.fetch(signifier)
                }
            }

            is FunctionTypeLiteral -> TypePlaceholder
            is ParameterizedSignifier -> {
                when (val symbol = fetch(signifier.tti)) {
                    is ParameterizedFunctionSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    is ParameterizedMemberPluginSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    is ParameterizedStaticPluginSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    else -> TypePlaceholder
                }
            }
            is ImplicitTypeLiteral -> langThrow(signifier.ctx, TypeSystemBug)
            is FinLiteral -> TypePlaceholder
            is InvokeSignifier -> TypePlaceholder
            is NamedCost -> TypePlaceholder
        }

    override fun fetchHere(signifier: Signifier): Symbol =
        when (signifier) {
            is Identifier -> {
                if (symbolTable.containsKey(signifier.name)) {
                    symbolTable[signifier.name]!!
                } else {
                    langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
                }
            }

            is FunctionTypeLiteral -> TypePlaceholder
            is ParameterizedSignifier -> {
                when (val symbol = fetchHere(signifier.tti)) {
                    is ParameterizedFunctionSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    is ParameterizedMemberPluginSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    is ParameterizedStaticPluginSymbol -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != symbol.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(symbol.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(symbol.typeParams, typeArgs)
                            substitution.apply(symbol)
                        }
                    }
                    else -> TypePlaceholder
                }
            }
            is ImplicitTypeLiteral -> langThrow(signifier.ctx, TypeSystemBug)
            is FinLiteral -> TypePlaceholder
            is InvokeSignifier -> TypePlaceholder
            is NamedCost -> TypePlaceholder
        }

    override fun defineType(identifier: Identifier, definition: Type) {
        if (symbolTable.containsKey(identifier.name) || typeTable.containsKey(identifier.name)) {
            langThrow(identifier.ctx, IdentifierAlreadyExists(toError(identifier)))
        } else {
            symbolTable[identifier.name] = TypePlaceholder
            typeTable[identifier.name] = definition
        }
    }

    override fun typeExists(signifier: Signifier): Boolean =
        when (signifier) {
            is Identifier -> typeTable.containsKey(signifier.name) || parent.typeExists(signifier)
            is FunctionTypeLiteral -> signifier.formalParamTypes.all { typeExists(it) } && typeExists(signifier.returnType)
            is ParameterizedSignifier -> typeExists(signifier.tti) && signifier.args.all { typeExists(it) }
            is ImplicitTypeLiteral -> false
            is FinLiteral -> false
            is InvokeSignifier -> false
            is NamedCost -> false
        }

    override fun typeExistsHere(signifier: Signifier): Boolean =
        when (signifier) {
            is Identifier -> typeTable.containsKey(signifier.name)
            is FunctionTypeLiteral -> signifier.formalParamTypes.all { typeExists(it) } && typeExists(signifier.returnType)
            is ParameterizedSignifier -> typeExistsHere(signifier.tti) && signifier.args.all { typeExists(it) }
            is ImplicitTypeLiteral -> false
            is FinLiteral -> false
            is InvokeSignifier -> false
            is NamedCost -> false
        }

    override fun fetchType(signifier: Signifier): Type =
        when (signifier) {
            is Identifier -> {
                if (typeTable.containsKey(signifier.name)) {
                    typeTable[signifier.name]!!
                } else {
                    parent.fetchType(signifier)
                }
            }

            is FunctionTypeLiteral -> FunctionType(
                signifier.formalParamTypes.map { fetchType(it) },
                fetchType(signifier.returnType)
            )

            is ParameterizedSignifier -> {
                when (val type = fetchType(signifier.tti)) {
                    is ParameterizedRecordType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is PlatformSumRecordType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is PlatformSumType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is ParameterizedBasicType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is TypeInstantiation -> type
                    else -> langThrow(signifier.ctx, SymbolHasNoParameters(toError(signifier)))
                }
            }

            is ImplicitTypeLiteral -> langThrow(signifier.ctx, TypeSystemBug)
            is FinLiteral -> Fin(signifier.magnitude)
            is InvokeSignifier -> {
                val typeArgs = signifier.args.map {
                    val t = fetchType(it)
                    if (t !is CostExpression) {
                        langThrow(it.ctx, TypeMustBeCostExpression(toError(t)))
                    }
                    t
                }
                when (signifier.op) {
                    CostOperator.Sum -> SumCostExpression(typeArgs)
                    CostOperator.Mul -> ProductCostExpression(typeArgs)
                    CostOperator.Max -> MaxCostExpression(typeArgs)
                    else -> langThrow(signifier.ctx, ImpossibleState("Never reachable"))
                }
            }

            is NamedCost -> ConstantFin(NamedFin(signifier.name))
        }

    override fun fetchTypeHere(signifier: Signifier): Type =
        when (signifier) {
            is Identifier -> {
                if (typeTable.containsKey(signifier.name)) {
                    typeTable[signifier.name]!!
                } else {
                    langThrow(signifier.ctx, IdentifierNotFound(toError(signifier)))
                }
            }

            is FunctionTypeLiteral -> FunctionType(
                signifier.formalParamTypes.map { fetchType(it) },
                fetchType(signifier.returnType)
            )

            is ParameterizedSignifier -> {
                when (val type = fetchTypeHere(signifier.tti)) {
                    is ParameterizedRecordType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is PlatformSumRecordType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is PlatformSumType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is ParameterizedBasicType -> {
                        val typeArgs = signifier.args.map { fetchType(it) }
                        if (typeArgs.size != type.typeParams.size) {
                            langThrow(
                                signifier.ctx,
                                IncorrectNumberOfTypeArgs(type.typeParams.size, typeArgs.size)
                            )
                        } else {
                            val substitution = Substitution(type.typeParams, typeArgs)
                            substitution.apply(type)
                        }
                    }

                    is TypeInstantiation -> type
                    else -> langThrow(signifier.ctx, SymbolHasNoParameters(toError(signifier)))
                }
            }

            is ImplicitTypeLiteral -> langThrow(signifier.ctx, TypeSystemBug)
            is FinLiteral -> Fin(signifier.magnitude)
            is InvokeSignifier -> {
                val typeArgs = signifier.args.map {
                    val t = fetchType(it)
                    if (t !is CostExpression) {
                        langThrow(it.ctx, TypeMustBeCostExpression(toError(t)))
                    }
                    t
                }
                when (signifier.op) {
                    CostOperator.Sum -> SumCostExpression(typeArgs)
                    CostOperator.Mul -> ProductCostExpression(typeArgs)
                    CostOperator.Max -> MaxCostExpression(typeArgs)
                    else -> langThrow(signifier.ctx, ImpossibleState("Never reachable"))
                }
            }

            is NamedCost -> ConstantFin(NamedFin(signifier.name))
        }
}