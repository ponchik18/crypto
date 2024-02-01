package domain

import domain.transaction.Transaction
import repository.UniqueItem
import java.math.BigDecimal
import java.util.*

class CryptoExchange(
    val name: String,
    override val id: UUID = UUID.randomUUID()
) : UniqueItem {
    val exchangeRates: MutableMap<Pair<Currency, Currency>, BigDecimal> = mutableMapOf()
    val transactionHistory: MutableList<Transaction> = mutableListOf()
    override fun getUniqueId(): UUID = id
    override fun toString(): String {
        return "CryptoExchange(name='$name', id=$id, exchangeRates=$exchangeRates, transactionHistory=$transactionHistory)"
    }

}