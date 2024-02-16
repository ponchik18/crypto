package domain

import repository.Identifiable
import java.math.BigDecimal
import java.util.*
import kotlin.properties.Delegates

class Wallet(
    val name: String,
    var isCold: Boolean,
    var passphrase: String,
    override val id: UUID = UUID.randomUUID()
) : Identifiable {
    var cryptoCurrencies: MutableMap<Currency, BigDecimal> by Delegates.observable(cryptoCurrencies) { _, oldValue, newValue ->
        println("CryptoCurrencies changed from $oldValue to $newValue")
    }

    fun topUpWallet(currency: Currency, amount: BigDecimal) {
        val cryptoCurrencyValue = cryptoCurrencies.getOrDefault(currency, BigDecimal.ZERO)
        cryptoCurrencies[currency] = amount.add(cryptoCurrencyValue)
    }

    operator fun plus(other: Wallet): Wallet {
        val wallet = Wallet("General", false, this.passphrase)
        wallet.cryptoCurrencies.putAll(this.cryptoCurrencies)
        wallet.cryptoCurrencies.putAll(other.cryptoCurrencies)
        return wallet
    }

    operator fun component1(): String = name
    operator fun component2(): Boolean = isCold
    operator fun component3(): String = passphrase
    operator fun component4(): Map<Currency, BigDecimal> = cryptoCurrencies
    operator fun component5(): UUID = id
}