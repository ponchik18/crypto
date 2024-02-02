package utils

import domain.User
import domain.numberOfWallets
import domain.transaction.SwapTransaction
import domain.transaction.TradeTransaction
import domain.transaction.Transaction
import enums.UserStatus
import java.util.*
import kotlin.system.measureTimeMillis

fun <T, U> Pair<T, U>.swap(): Pair<U, T> {
    return second to first
}

fun createRange(start: Int = 0, end: Int = 100): IntProgression {
    if (start > end)
        return start downTo end
    return start..end
}

fun getUserTransaction(transaction: Transaction): User {
    return when (transaction) {
        is TradeTransaction -> transaction.initiator
        is SwapTransaction -> transaction.receiver
    }
}

fun filterListOfUserByNumberOfWallets(userList: List<User>, filterNumberOfWallets: Int) =
    userList.filter { it.numberOfWallets > filterNumberOfWallets }

fun createMapsFromUserList(userList: List<User>): Pair<Map<UUID, User>, Map<User, UserStatus>> {
    return Pair(
        first = userList.associateBy { it.id },
        second = userList.associateWith { it.status }
    )
}

fun measureTimeUserFilter() {
    val userList = generateUserList()

    val listTime = measureTimeMillis {
        val result = userList
            .filter { it.status == UserStatus.APPROVED }
            .map { it.fullName }
            .any { it.startsWith('a', ignoreCase = true) }
        println("Regular List Result: $result")
    }

    val sequenceTime = measureTimeMillis {
        val result = userList.asSequence()
            .filter { it.status == UserStatus.APPROVED }
            .map { it.fullName }
            .any { it.startsWith('a', ignoreCase = true) }
        println("Regular Sequence Result: $result")
    }

    println("Regular List Time: $listTime ms")
    println("Sequence Time: $sequenceTime ms")
}

fun generateUserList(): List<User> {
    return (1..10000).map {
        User(
            "test",
            "${generateRandomLetter()}User",
            mutableSetOf(),
            if (it % 2 == 0) UserStatus.APPROVED else UserStatus.NEW
        )
    }
}

fun generateRandomLetter(): Char {
    val alphabet = ('A'..'Z') + ('a'..'z')
    return alphabet.random()
}

val memo = mutableMapOf<Int, Long>()

fun fibonacci(n: Int): Long {
    return memo.getOrPut(n) {
        if (n <= 1) {
            n.toLong()
        } else {
            fibonacci(n - 1) + fibonacci(n - 2)
        }
    }
}