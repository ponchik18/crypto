package service

import domain.CryptoExchange
import domain.User
import domain.Wallet
import java.math.BigDecimal
import domain.Currency

interface TradingService {
    fun exchangeCryptoCurrency(
        firstUser: User,
        secondUser: User,
        firstUserCryptoCurrency: Pair<Currency, BigDecimal>,
        secondUserCryptoCurrency: Pair<Currency, BigDecimal>,
        cryptoExchange: CryptoExchange
    )

    fun swapCrypto(wallet: Wallet, passphrase: String, cryptoExchange: CryptoExchange, toCurrency: Currency, user: User)
    fun getAllCryptoExchange(): List<CryptoExchange>
}