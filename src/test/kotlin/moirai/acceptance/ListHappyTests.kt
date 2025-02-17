package moirai.acceptance

import org.junit.jupiter.api.Test
import moirai.eval.DictionaryValue
import moirai.eval.ListValue

class ListHappyTests {
    @Test
    fun basicListTest() {
        val input = """
        val x = List(1, 2, 3)
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun sizeListTest() {
        val input = """
        val x = List(1, 2, 3)
        x.size
        ^^^^^
        3
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun basicExplicitListTest() {
        val input = """
        val x = List<Int, 3>(1, 2, 3)
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun basicForEachTest() {
        val input = """
        val x = List(1, 2, 3)
        for(y in x) {
            x
        }
        
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun basicForEachExplicitTest() {
        val input = """
        val x = List(1, 2, 3)
        for(y: Int in x) {
            y
        }
        
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun basicForShortFormTest() {
        val input = """
        val x = List(1, 2, 3)
        for(x) {
            it
        }
        
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun basicMutableListTest() {
        val input = """
        val x = MutableList<Int, 3>(1, 2, 3)
        x[0]
        ^^^^^
        1
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun sizeMutableListTest() {
        val input = """
        val x = MutableList<Int, 3>(1, 2, 3)
        x.size
        ^^^^^
        3
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun addMutableListTest() {
        val input = """
        val x = MutableList<Int, 4>(1, 2, 3)
        x.add(4)
        x[3]
        ^^^^^
        4
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun removeAtMutableListTest() {
        val input = """
        val x = MutableList<Int, 3>(1, 2, 3)
        x.removeAt(0)
        x[0]
        ^^^^^
        2
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun setMutableListTest() {
        val input = """
        val x = MutableList<Int, 3>(1, 2, 3)
        x[0] = 7
        x[0]
        ^^^^^
        7
    """.trimIndent()
        splitTest(input)
    }

    @Test
    fun equalsListTest() {
        splitTest(
            """
            val x = List<Int, 3>(1, 2, 3)
            val y = List<Int, 3>(1, 2, 3)
            
            x == y
            ^^^^^
            true
        """.trimIndent()
        )
    }

    @Test
    fun equalsMutableListTest() {
        splitTest(
            """
            val x = MutableList<Int, 3>(1, 2, 3)
            val y = MutableList<Int, 3>(1, 2, 3)
            
            x == y
            ^^^^^
            true
        """.trimIndent()
        )
    }

    @Test
    fun equalsNegativeListTest() {
        splitTest(
            """
            val x = List<Int, 3>(1, 2, 7)
            val y = List<Int, 3>(1, 2, 3)
            
            x == y
            ^^^^^
            false
        """.trimIndent()
        )
    }

    @Test
    fun equalsMutableNegativeListTest() {
        splitTest(
            """
            val x = MutableList<Int, 3>(1, 2, 7)
            val y = MutableList<Int, 3>(1, 2, 3)
            
            x == y
            ^^^^^
            false
        """.trimIndent()
        )
    }

    @Test
    fun notEqualsListTest() {
        splitTest(
            """
            val x = List<Int, 3>(1, 2, 7)
            val y = List<Int, 3>(1, 2, 3)
            
            x != y
            ^^^^^
            true
        """.trimIndent()
        )
    }

    @Test
    fun notEqualsMutableListTest() {
        splitTest(
            """
            val x = MutableList<Int, 3>(1, 2, 7)
            val y = MutableList<Int, 3>(1, 2, 3)
            
            x != y
            ^^^^^
            true
        """.trimIndent()
        )
    }

    @Test
    fun notEqualsNegativeListTest() {
        splitTest(
            """
            val x = List<Int, 3>(1, 2, 3)
            val y = List<Int, 3>(1, 2, 3)
            
            x != y
            ^^^^^
            false
        """.trimIndent()
        )
    }

    @Test
    fun notEqualsNegativeMutableListTest() {
        splitTest(
            """
            val x = MutableList<Int, 3>(1, 2, 3)
            val y = MutableList<Int, 3>(1, 2, 3)
            
            x != y
            ^^^^^
            false
        """.trimIndent()
        )
    }

    @Test
    fun finTypeParamListTest() {
        splitTest(
            """
            def size<E, O: Fin>(x: List<E, O>): Int {
                mutable res = 0
                for(e in x) {
                    res = res + 1
                }
                res
            }
            
            val x = List(1, 2, 3)
            size(x)
            ^^^^^
            3
        """.trimIndent()
        )
    }

    @Test
    fun listUpcastTest() {
        typeTest(
            """
                List(1, 2, 3, 4, 5)
            """.trimIndent()
        ) { it is ListValue }
    }

    @Test
    fun listUpcastNegativeTest() {
        typeTest(
            """
                List(1, 2, 3, 4, 5)
            """.trimIndent()
        ) { it is ListValue }
    }

    @Test
    fun listNestedUpcastTest() {
        typeTest(
            """
                val x = List(1, 2, 3, 4, 5)
                List(x, x, x)
            """.trimIndent()
        ) { it is ListValue }
    }

    @Test
    fun listNestedUpcastNegativeTest() {
        typeTest(
            """
                val x = List(1, 2, 3, 4, 5)
                List(x, x, x)
            """.trimIndent()
        ) { it is ListValue }
    }

    @Test
    fun mutableListToImmutableTest() {
        typeTest(
            """
                val x = MutableList<Int, 10>(1, 2, 3, 4, 5)
                x.toList()
            """.trimIndent()
        ) { it is ListValue }
    }

    @Test
    fun immutableListToMutableTest() {
        splitTest(
            """
                val x = List(1, 2, 3, 4, 5)
                val y = MutableList<Int, 10>()
                for(i in range(0, 10)) {
                    if(i < x.size) {
                        y[i] = x[i]
                    }
                }
                y[1] = 9
                y[1]
                ^^^^^
                9
            """.trimIndent()
        )
    }

    @Test
    fun failedDemoIfTest() {
        splitTest("""
            def f(list: List<Int, 10>): Int {
                mutable max = 0
                for(x in list) {
                    if(x > max) {
                        max = x
                    }
                }
                max
            }
            
            val l = List(1, 2, 3, 4, 5)
            f(l)
            ^^^^^
            5
        """.trimIndent())
    }

    @Test
    fun failedDemoMaxTest() {
        splitTest("""
            def f(list: List<Int, 100>) {
                mutable max = 0
                for(y in list) {
                    if(y > max) {
                        max = y
                    }
                }
                max
            }
            
            val l = List(1, 2, 3, 4, 5)
            f(l)
            ^^^^^
            Unit
        """.trimIndent())
    }
}