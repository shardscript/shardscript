package org.shardscript.semantics.core

sealed class RefAstSymbolSlot

data object RefSlotError: RefAstSymbolSlot()
data class RefSlotObject(val payload: ObjectSymbol): RefAstSymbolSlot()
data class RefSlotPlatformObject(val payload: PlatformObjectSymbol): RefAstSymbolSlot()
data class RefSlotSTP(val payload: StandardTypeParameter): RefAstSymbolSlot()
data class RefSlotLVS(val payload: LocalVariableSymbol): RefAstSymbolSlot()
data class RefSlotFormal(val payload: FunctionFormalParameterSymbol): RefAstSymbolSlot()
data class RefSlotField(val payload: FieldSymbol): RefAstSymbolSlot()

sealed class AssignAstSymbolSlot

data object AssignSlotError: AssignAstSymbolSlot()
data class AssignSlotLVS(val payload: LocalVariableSymbol): AssignAstSymbolSlot()

sealed class DotAssignAstSymbolSlot

data object DotAssignSlotError: DotAssignAstSymbolSlot()
data class DotAssignSlotField(val payload: FieldSymbol): DotAssignAstSymbolSlot()

sealed class DotAstSymbolSlot

data object DotSlotError: DotAstSymbolSlot()
data class DotSlotField(val payload: FieldSymbol): DotAstSymbolSlot()

data class DotSlotPlatformField(val payload: PlatformFieldSymbol): DotAstSymbolSlot()

sealed class DotApplyAstSymbolSlot

data object DotApplySlotError: DotApplyAstSymbolSlot()
data class DotApplySlotGF(val payload: GroundFunctionSymbol): DotApplyAstSymbolSlot()
data class DotApplySlotGMP(val payload: GroundMemberPluginSymbol): DotApplyAstSymbolSlot()
data class DotApplySlotSI(val payload: SymbolInstantiation): DotApplyAstSymbolSlot()

sealed class GroundApplyAstSymbolSlot

data object GroundApplySlotError: GroundApplyAstSymbolSlot()
data class GroundApplySlotGF(val payload: GroundFunctionSymbol): GroundApplyAstSymbolSlot()
data class GroundApplySlotSI(val payload: SymbolInstantiation): GroundApplyAstSymbolSlot()
data class GroundApplySlotTI(val payload: TypeInstantiation): GroundApplyAstSymbolSlot()
data class GroundApplySlotFormal(val payload: FunctionFormalParameterSymbol): GroundApplyAstSymbolSlot()
data class GroundApplySlotGRT(val payload: GroundRecordTypeSymbol): GroundApplyAstSymbolSlot()