package fr.cindano.showcase

/** Errors as values: callers must handle every case, the compiler checks it. */
sealed interface Result<out T> {
    data class Ok<T>(val value: T) : Result<T>
    data class Failure(val reason: String) : Result<Nothing>
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Ok -> Result.Ok(transform(value))
    is Result.Failure -> this
}

fun parseAge(raw: String): Result<Int> {
    val age = raw.trim().toIntOrNull() ?: return Result.Failure("'$raw' n'est pas un nombre")
    return if (age in 0..130) Result.Ok(age) else Result.Failure("âge hors limites : $age")
}
