package utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UtilTest {
    @Test
    fun `test convert int to string`() {
        val value: Any = 123
        val result: String? = value convert String::class.java
        assertEquals("123", result)
    }

    @Test
    fun `test convert string to int`() {
        val value: Any = "456"
        val result: Int? = value convert Int::class.java
        assertEquals(456, result)
    }

    @Test
    fun `test convert invalid type`() {
        val value: Any = "Not a number"
        val result: Int? = value convert Int::class.java
        assertEquals(null, result)
    }
}