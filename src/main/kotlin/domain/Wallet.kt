package domain

import repository.UniqueItem
import java.math.BigDecimal
import java.util.*

class Wallet : UniqueItem {
    val name: String
    var isCold: Boolean
    val cryptoCurrencies: MutableMap<Currency, BigDecimal> = mutableMapOf()
    var passphrase: String
    override val id: UUID = UUID.randomUUID()

    constructor(name: String, isCold: Boolean, passphrase: String) {
        this.name = name
        this.isCold = isCold
        this.passphrase = passphrase
    }

    fun topUpWallet(currency: Currency, amount: BigDecimal) {
        val cryptoCurrencyValue = cryptoCurrencies[currency]?: BigDecimal.ZERO
        cryptoCurrencies[currency] = amount.add(cryptoCurrencyValue)
    }

    override fun getUniqueId(): UUID = id
}