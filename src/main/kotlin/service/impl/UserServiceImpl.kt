package service.impl

import domain.CryptoExchange
import domain.User
import domain.Wallet
import repository.impl.CryptoExchangeRepository
import repository.impl.UserRepository
import service.UserService
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.Instant
import java.util.*

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cryptoExchangeRepository: CryptoExchangeRepository
) : UserService {

    override fun cryptoBalance(vararg wallets: Wallet) {
        for (wallet in wallets) {
            for ((key, value) in wallet.cryptoCurrencies) {
                print("${key.code} : ${formatBigDecimal(value)}")
            }
        }
    }

    private fun formatBigDecimal(amount: BigDecimal) = DecimalFormat("#,##0.00").format(amount)

    override fun getAllTransaction(cryptoExchange: CryptoExchange, from: Instant, to: Instant) {
        cryptoExchange.transactionHistory.forEachIndexed { index, transaction ->
            print("${index + 1}) $transaction")
        }
    }

    override fun createWallet(wallet: Wallet, userId: UUID) {
        val existingUser = userRepository.getItem(userId)
        existingUser?.wallets?.add(wallet)
    }

    override fun createUser(user: User) = userRepository.save(user)

    override fun getUserById(id: UUID): User? = userRepository.getItem(id)

    override fun getAllUser(): List<User> = userRepository.getAllItem()
}