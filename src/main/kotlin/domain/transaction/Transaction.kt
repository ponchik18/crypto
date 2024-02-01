package domain.transaction

import domain.User
import repository.UniqueItem
import java.math.BigDecimal
import java.time.Instant
import domain.Currency
import java.util.*

open class Transaction(
    val date: Instant,
    val initiator: User,
    val fromCurrency: Currency,
    val fromAmount: BigDecimal,
    override val id: UUID = UUID.randomUUID()
) : UniqueItem {
    override fun getUniqueId(): UUID = id
    override fun toString(): String {
        return "Transaction(date=$date, initiator=$initiator, fromCurrency=$fromCurrency, fromAmount=$fromAmount, id=$id)"
    }

}