package domain

import repository.Identifiable
import java.math.BigDecimal
import java.util.*

class Wallet(
    val name: String,
    var isCold: Boolean,
    var passphrase: String,
    val cryptoCurrencies: MutableMap<Currency, BigDecimal> = mutableMapOf(),
    override val id: UUID = UUID.randomUUID()
) : Identifiable {

    fun topUpWallet(currency: Currency, amount: BigDecimal) {
        val cryptoCurrencyValue = cryptoCurrencies.getOrDefault(currency, BigDecimal.ZERO)
        cryptoCurrencies[currency] = amount.add(cryptoCurrencyValue)
    }

    operator fun component1(): String = name
    operator fun component2(): Boolean = isCold
    operator fun component3(): String = passphrase
    operator fun component4(): Map<Currency, BigDecimal> = cryptoCurrencies
    operator fun component5(): UUID = id
}