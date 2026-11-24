package fr.cindano.showcase

import kotlin.math.abs

/** A value class: type safety on amounts with no runtime allocation. */
@JvmInline
value class Cents(val value: Long) : Comparable<Cents> {
    operator fun plus(other: Cents) = Cents(value + other.value)
    operator fun times(factor: Int) = Cents(value * factor)
    override fun compareTo(other: Cents) = value.compareTo(other.value)
    override fun toString() = "%s%d,%02d €".format(if (value < 0) "-" else "", abs(value) / 100, abs(value) % 100)
}

val Int.euros get() = Cents(this * 100L)
