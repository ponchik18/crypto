package domain

import enums.UserStatus
import repository.Identifiable
import java.math.BigDecimal
import java.util.*

data class User(
    val email: String?,
    val fullName: String,
    val wallets: MutableSet<Wallet>,
    var status: UserStatus,
    override val id: UUID = UUID.randomUUID()
) : Identifiable {
    init {
        if(wallets.isEmpty()) {
            val pair = Pair(Currency("United States Dollar", "USD", "$"), BigDecimal.TEN)
            wallets.add(Wallet("wallet", true, "12345678", mutableMapOf(pair)))
        }
    }
}

fun User.transformEmail(): User {
    val transformEmail = email?.uppercase()?.substringBefore('@') ?: ""
    return User(transformEmail, fullName, wallets, status)
}

val User.numberOfWallets: Int
    get() = wallets.size