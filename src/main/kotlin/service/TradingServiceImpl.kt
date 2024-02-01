package service

import domain.CryptoExchange
import domain.Currency
import domain.User
import domain.Wallet
import domain.exception.NotEnoughAmountToExchangeException
import domain.exception.NotRightPassphraseException
import domain.exception.SwapTransactionNotProcessException
import domain.exception.UserNotApprovedException
import domain.transaction.SwapTransaction
import domain.transaction.TradeTransaction
import enums.UserStatus
import repository.impl.CryptoExchangeRepository
import repository.impl.TransactionRepository
import java.math.BigDecimal
import java.time.Instant
import kotlin.random.Random

class TradingServiceImpl(
    private val cryptoExchangeRepository: CryptoExchangeRepository,
    private val transactionRepository: TransactionRepository
) {

    fun exchangeCryptoCurrency(
        firstUser: User,
        secondUser: User,
        firstUserCryptoCurrency: Pair<Currency, BigDecimal>,
        secondUserCryptoCurrency: Pair<Currency, BigDecimal>,
        cryptoExchange: CryptoExchange
    ) {
        validateUserStatus(firstUser)
        validateUserStatus(secondUser)

        val firstUserWallet = findUserWalletThatContainEnoughCryptoCurrency(
            user = firstUser,
            userCryptoCurrency = firstUserCryptoCurrency
        )
        val secondUserWallet = findUserWalletThatContainEnoughCryptoCurrency(
            user = firstUser,
            userCryptoCurrency = firstUserCryptoCurrency
        )

        exchangeCurrency(firstUserWallet, firstUserCryptoCurrency, secondUserCryptoCurrency)
        exchangeCurrency(secondUserWallet, secondUserCryptoCurrency, firstUserCryptoCurrency)

        val tradeTransaction = TradeTransaction(
            date =  Instant.now(),
            initiator = firstUser,
            fromCurrency = firstUserCryptoCurrency.first,
            fromAmount = firstUserCryptoCurrency.second,
            toCurrency = secondUserCryptoCurrency.first,
            toAmount = secondUserCryptoCurrency.second
        )

        cryptoExchange.transactionHistory.add(tradeTransaction)
        transactionRepository.save(tradeTransaction)
        cryptoExchangeRepository.save(cryptoExchange)
    }

    fun swapCrypto(
        wallet: Wallet,
        passphrase: String,
        cryptoExchange: CryptoExchange,
        toCurrency: Currency,
        user: User
    ) {
        if (wallet.passphrase != passphrase)
            throw NotRightPassphraseException()

        val randomValue = Random.nextDouble()
        if (randomValue in 0.0..0.25)
            throw SwapTransactionNotProcessException()

        val exchangeRates = cryptoExchange.exchangeRates
        val userCryptoCurrencies = wallet.cryptoCurrencies
        for ((key, value) in userCryptoCurrencies) {
            val rate = exchangeRates[Pair(key, toCurrency)] ?: continue
            val swapTransaction = SwapTransaction(Instant.now(), user, key, value, toCurrency, rate * value)
            transactionRepository.save(swapTransaction)
        }
    }

    fun getAllCryptoExchange() = cryptoExchangeRepository.findAll()

    private fun validateUserStatus(user: User) {
        if (user.status != UserStatus.APPROVED)
            throw UserNotApprovedException(user.id)
    }

    private fun findUserWalletThatContainEnoughCryptoCurrency(
        user: User,
        userCryptoCurrency: Pair<Currency, BigDecimal>
    ): Wallet {
        for (wallet in user.wallets) {
            val cryptoCurrency = wallet.cryptoCurrencies[userCryptoCurrency.first] ?: continue

            if (cryptoCurrency >= userCryptoCurrency.second)
                return wallet
        }
        throw NotEnoughAmountToExchangeException(user.id, userCryptoCurrency.first)
    }

    private fun exchangeCurrency(
        wallet: Wallet,
        firstUserCryptoCurrency: Pair<Currency, BigDecimal>,
        secondUserCryptoCurrency: Pair<Currency, BigDecimal>
    ) {
        wallet.cryptoCurrencies[firstUserCryptoCurrency.first]?.subtract(firstUserCryptoCurrency.second)
        wallet.cryptoCurrencies.getOrPut(secondUserCryptoCurrency.first) { BigDecimal.ZERO }
    }
}