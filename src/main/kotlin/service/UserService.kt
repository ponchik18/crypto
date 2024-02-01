package service

import domain.CryptoExchange
import domain.User
import domain.Wallet
import java.time.Instant
import java.util.*

interface UserService {
    fun cryptoBalance(vararg wallets: Wallet)
    fun getAllTransaction(cryptoExchange: CryptoExchange, from: Instant, to: Instant = Instant.now())
    fun createUser(user: User)
    fun createWallet(wallet: Wallet, userId: UUID)
    fun getUserById(id: UUID): User?
    fun getAllUser(): List<User>
}