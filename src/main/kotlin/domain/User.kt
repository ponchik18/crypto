package domain

import enums.UserStatus
import repository.Identifiable
import java.util.*

class User(
    val email: String,
    val fullName: String,
    val wallets: MutableSet<Wallet>,
    var status: UserStatus,
    override val id: UUID = UUID.randomUUID()
) : Identifiable {
    override fun toString(): String {
        return "User(email='$email', fullName='$fullName', wallets=$wallets, status=$status, id=$id)"
    }

}