package fr.cindano.showcase

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/** Runs [transform] on every item concurrently, with at most [concurrency] calls in flight, keeping input order. */
suspend fun <T, R> List<T>.mapConcurrently(concurrency: Int, transform: suspend (T) -> R): List<R> = coroutineScope {
    val gate = Semaphore(concurrency)
    map { item -> async { gate.withPermit { transform(item) } } }.map { it.await() }
}

/** A cold Flow that emits [n] readings, then stays lazy until collected. */
fun readings(n: Int, intervalMs: Long = 10): Flow<Int> = flow {
    repeat(n) { i ->
        delay(intervalMs)
        emit(i * i)
    }
}

fun Flow<Int>.doubledAndBuffered(): Flow<Int> = map { it * 2 }.buffer()
