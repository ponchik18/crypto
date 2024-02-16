package service

import domain.CryptoExchange
import domain.Currency
import domain.User
import domain.Wallet
import domain.exception.NotEnoughAmountToExchangeException
import domain.exception.NotRightPassphraseException
import domain.exception.UserNotApprovedException
import domain.transaction.SwapTransaction
import domain.transaction.TradeTransaction
import enums.UserStatus
import repository.impl.CryptoExchangeRepository
import repository.impl.TransactionRepository
import utils.throwException
import java.math.BigDecimal
import java.time.Instant
import kotlin.random.Random

object TradingService {

    private val cryptoExchangeRepository = CryptoExchangeRepository
    private val transactionRepository = TransactionRepository

    fun swapCurrencyBetweenUsers(
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

        val tradeTransaction = SwapTransaction(
            date = Instant.now(),
            initiator = firstUser,
            receiver = secondUser,
            fromCurrency = firstUserCryptoCurrency.first,
            fromAmount = firstUserCryptoCurrency.second,
            toCurrency = secondUserCryptoCurrency.first,
            toAmount = secondUserCryptoCurrency.second
        )

        cryptoExchange.transactionHistory.add(tradeTransaction)
        transactionRepository.use {
            it.save(tradeTransaction)
        }
        cryptoExchangeRepository.use {
            it.save(cryptoExchange)
        }
    }

    fun exchangeCryptoCurrency(
        wallet: Wallet,
        passphrase: String,
        cryptoExchange: CryptoExchange,
        toCurrency: Currency,
        user: User
    ) {

        val randomValue = Random.nextDouble()
        check(randomValue in 0.0..0.25) { "Trade transaction not processed exception!" }

        if (wallet.passphrase != passphrase)
            throwException<NotRightPassphraseException>()

        val exchangeRates = cryptoExchange.exchangeRates
        val userCryptoCurrencies = wallet.cryptoCurrencies
        for ((key, value) in userCryptoCurrencies) {
            val rate = exchangeRates[Pair(key, toCurrency)] ?: continue
            val tradeTransaction = TradeTransaction(Instant.now(), user, key, value, toCurrency, rate * value)
            cryptoExchange.transactionHistory.add(tradeTransaction)
            transactionRepository.use {
                it.save(tradeTransaction)
            }
        }
    }

    fun getAllCryptoExchange() = cryptoExchangeRepository.use { it.findAll() }

    private fun validateUserStatus(user: User) {
        if (user.status != UserStatus.APPROVED)
            throwException<UserNotApprovedException>(user.id)
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