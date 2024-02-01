package domain

import enums.UserStatus
import repository.UniqueItem
import java.util.*

class User(
    val email: String,
    val fullName: String,
    val wallets: MutableSet<Wallet>,
    var status: UserStatus,
    override val id: UUID = UUID.randomUUID()
) : UniqueItem {
    override fun getUniqueId(): UUID = id
    override fun toString(): String {
        return "User(email='$email', fullName='$fullName', wallets=$wallets, status=$status, id=$id)"
    }

}