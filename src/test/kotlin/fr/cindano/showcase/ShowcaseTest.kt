package fr.cindano.showcase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ShowcaseTest {

    @Test
    fun `parseAge returns values or reasons, never throws`() {
        assertEquals(Result.Ok(42), parseAge(" 42 "))
        assertIs<Result.Failure>(parseAge("abc"))
        assertIs<Result.Failure>(parseAge("200"))
        assertEquals(Result.Ok(84), parseAge("42").map { it * 2 })
    }

    @Test
    fun `cents add up and print as euros`() {
        assertEquals("25,50 €", (12.euros * 2 + Cents(150)).toString())
    }

    @Test
    fun `negative cents keep their sign`() {
        assertEquals("-0,50 €", Cents(-50).toString())
        assertEquals("-12,05 €", Cents(-1205).toString())
    }

    @Test
    fun `order DSL computes the total`() {
        val o = order("Ana") {
            line("Billet", 12.euros, quantity = 2)
            line("Casque", 5.euros)
        }
        assertEquals(29.euros, o.total)
    }

    @Test
    fun `mapConcurrently keeps order and respects the concurrency limit`() = runTest {
        val inFlight = AtomicInteger()
        var peak = 0
        val result = (1..6).toList().mapConcurrently(concurrency = 2) { n ->
            peak = max(peak, inFlight.incrementAndGet())
            delay(100)
            inFlight.decrementAndGet()
            n * 10
        }
        assertEquals(listOf(10, 20, 30, 40, 50, 60), result)
        assertEquals(2, peak)
        assertEquals(300, currentTime) // 6 tasks, 2 at a time, 100 ms each
    }

    @Test
    fun `flow is cold - nothing runs until collected and each collection restarts it`() = runTest {
        var started = 0
        val source = flow { started++; emit(1); emit(2) }
        val doubled = source.doubledAndBuffered()
        assertEquals(0, started)
        assertEquals(listOf(2, 4), doubled.toList())
        assertEquals(listOf(2, 4), doubled.toList())
        assertEquals(2, started)
    }

    @Test
    fun `readings emits squares over virtual time`() = runTest {
        assertEquals(listOf(0, 1, 4, 9), readings(4, intervalMs = 10).toList())
        assertEquals(40, currentTime)
    }
}
