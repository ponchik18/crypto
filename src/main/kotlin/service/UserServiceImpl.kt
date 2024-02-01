package service

import domain.CryptoExchange
import domain.User
import domain.Wallet
import repository.impl.CryptoExchangeRepository
import repository.impl.UserRepository
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.Instant
import java.util.*

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cryptoExchangeRepository: CryptoExchangeRepository
) {

    fun cryptoBalance(vararg wallets: Wallet) {
        for (wallet in wallets) {
            for ((key, value) in wallet.cryptoCurrencies) {
                print("${key.code} : ${formatBigDecimal(value)}")
            }
        }
    }

    private fun formatBigDecimal(amount: BigDecimal) = DecimalFormat("#,##0.00").format(amount)

    fun getAllTransaction(cryptoExchange: CryptoExchange, from: Instant, to: Instant) {
        cryptoExchange.transactionHistory.forEachIndexed { index, transaction ->
            print("${index + 1}) $transaction")
        }
    }

    fun createWallet(wallet: Wallet, userId: UUID) {
        val existingUser = userRepository.findById(userId)
        existingUser?.wallets?.add(wallet)
    }

    fun createUser(user: User) = userRepository.save(user)

    fun getUserById(id: UUID): User? = userRepository.findById(id)

    fun getAllUser(): List<User> = userRepository.findAll()
}