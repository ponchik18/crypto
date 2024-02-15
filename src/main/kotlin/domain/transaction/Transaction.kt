package domain.transaction

import domain.User
import repository.Identifiable
import java.math.BigDecimal
import java.time.Instant
import domain.Currency
import java.util.*

sealed class Transaction(
    val date: Instant,
    val initiator: User,
    val fromCurrency: Currency,
    val fromAmount: BigDecimal,
    override val id: UUID = UUID.randomUUID()
) : Identifiable, TransactionSealed {
    override fun toString(): String {
        return "Transaction(date=$date, initiator=$initiator, fromCurrency=$fromCurrency, fromAmount=$fromAmount, id=$id)"
    }

}