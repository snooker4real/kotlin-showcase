package fr.cindano.showcase

data class Line(val label: String, val unitPrice: Cents, val quantity: Int)

data class Order(val customer: String, val lines: List<Line>) {
    val total: Cents get() = lines.fold(Cents(0)) { acc, l -> acc + l.unitPrice * l.quantity }
}

@DslMarker
annotation class OrderDsl

@OrderDsl
class OrderBuilder(private val customer: String) {
    private val lines = mutableListOf<Line>()

    fun line(label: String, price: Cents, quantity: Int = 1) {
        require(quantity > 0) { "quantity must be positive" }
        lines += Line(label, price, quantity)
    }

    fun build() = Order(customer, lines.toList())
}

/** A small type-safe builder: order("Ana") { line("Billet", 12.euros, 2) } */
fun order(customer: String, block: OrderBuilder.() -> Unit): Order = OrderBuilder(customer).apply(block).build()
