package domain

import domain.transaction.Transaction
import repository.Identifiable
import java.math.BigDecimal
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CryptoExchange(
    val name: String,
    override val id: UUID = UUID.randomUUID()
) : Identifiable {
    val exchangeRates: MutableMap<Pair<Currency, Currency>, BigDecimal> = mutableMapOf()
    val transactionHistory: MutableList<Transaction> by TransactionHistoryDelegate()
    override fun toString(): String {
        return "CryptoExchange(name='$name', id=$id, exchangeRates=$exchangeRates, transactionHistory=$transactionHistory)"
    }
}

class TransactionHistoryDelegate : ReadWriteProperty<CryptoExchange, MutableList<Transaction>> {
    private var transactions: MutableList<Transaction> = mutableListOf()

    override fun getValue(thisRef: CryptoExchange, property: KProperty<*>): MutableList<Transaction> {
        return transactions
    }

    override fun setValue(thisRef: CryptoExchange, property: KProperty<*>, value: MutableList<Transaction>) {
        transactions = value
    }
}