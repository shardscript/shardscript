package org.shardscript.acceptance

import org.shardscript.semantics.core.*
import org.junit.jupiter.api.Test

class ListErrorTests {
    @Test
    fun incorrectFinListTest() {
        failTest(
            """
            val x = List<2, 3>(1, 2, 3)
            x[0]
        """.trimIndent(), 3
        ) {
            it.error is InvalidStandardTypeSub || it.error is ExpectOtherError
        }
    }

    @Test
    fun incorrectStandardListTest() {
        failTest(
            """
            val x = List<Int, Int>(1, 2, 3)
            x[0]
        """.trimIndent(), 2
        ) {
            it.error is InvalidFinTypeSub
        }
    }

    @Test
    fun tooManyElementsListTest() {
        failTest(
            """
            val x = List<Int, 2>(1, 2, 3)
            x[0]
        """.trimIndent(), 1
        ) {
            it.error is TooManyElements
        }
    }

    @Test
    fun incorrectFinMutableListTest() {
        failTest(
            """
            val x = MutableList<2, 3>(1, 2, 3)
            x[0]
        """.trimIndent(), 3
        ) {
            it.error is InvalidStandardTypeSub || it.error is ExpectOtherError
        }
    }

    @Test
    fun incorrectStandardMutableListTest() {
        failTest(
            """
            val x = MutableList<Int, Int>(1, 2, 3)
            x[0]
        """.trimIndent(), 3
        ) {
            it.error is InvalidFinTypeSub
        }
    }

    @Test
    fun typeRequiresExplicitMutableListTest() {
        failTest(
            """
            val x = MutableList(1, 2, 3)
            x[0]
        """.trimIndent(), 1
        ) {
            it.error is TypeRequiresExplicit
        }
    }

    @Test
    fun tooManyElementsMutableListTest() {
        failTest(
            """
            val x = MutableList<Int, 2>(1, 2, 3)
            x[0]
        """.trimIndent(), 1
        ) {
            it.error is TooManyElements
        }
    }

    @Test
    fun runtimeFinViolationMutableListTest() {
        failTest(
            """
            val x = MutableList<Int, 3>(1, 2, 3)
            x.add(4)
            x[0]
        """.trimIndent(), 1
        ) {
            it.error is RuntimeFinViolation
        }
    }

    @Test
    fun invalidSourceTypeForEachMutableListTest() {
        failTest(
            """
            val x = MutableList<Int, 3>(1, 2, 3)
            for(y in x) {
                y
            }
        """.trimIndent(), 1
        ) {
            it.error is ForEachFeatureBan
        }
    }

    @Test
    fun finTypeParamMutableListTest() {
        failTest(
            """
            def size<E, O: Fin>(x: List<E, O>): Int {
                val z = MutableList<E, O>()
                mutable res = 0
                for(e in x) {
                    z.add(e)
                    res = res + 1
                }
                res
            }
            
            val x = List(1, 2, 3)
            size(x)
        """.trimIndent(), 2
        ) {
            it.error is InvalidFinTypeSub
        }
    }

    @Test
    fun failedDemoRecordingTest() {
        failTest(
            """
                def f(g: (Int, Int) -> Int): Int {
                    val list = List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                    for(x in list) {
                        for(y in list) {
                            g(x, y)
                        }
                    }
                    g(3, 4)
                }

                f(lambda (x: Int, y: Int) -> {
                    val list = List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                    for(a in list) {
                        for(b in list) {
                            x + y
                        }
                    }
                    x + y
                })
            """.trimIndent(), 1
        ) {
            it.error is CostOverLimit
        }
    }
}