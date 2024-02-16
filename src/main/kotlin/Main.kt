import constants.CryptoAppConstant.Companion.Currency.BTC
import constants.CryptoAppConstant.Companion.Currency.ETH
import constants.CryptoAppConstant.Companion.Currency.USD
import delegation.ApplicationDependenciesImpl
import delegation.ApplicationDependenciesImplDelegation
import domain.CryptoExchange
import domain.User
import domain.Wallet
import domain.numberOfWallets
import domain.transformEmail
import enums.UserStatus
import repository.impl.CryptoExchangeRepository
import repository.impl.TransactionRepository
import repository.impl.UserRepository
import service.TradingService
import service.UserService
import utils.createMapsFromUserList
import utils.createRange
import utils.fibonacci
import utils.filterListOfUserByNumberOfWallets
import utils.getUserTransaction
import utils.measureTimeUserFilter
import utils.swap
import java.math.BigDecimal


val userRepository = UserRepository
val cryptoExchangeRepository = CryptoExchangeRepository

var userService = UserService
var tradingService = TradingService
fun main(args: Array<String>) {
    initData()
    var userList = userService.getAllUser()
    println("===========================PART-1===========================")
    println("===========================1) All existing users ===========================")
    for (user in userList) {
        println(user)
    }

    println("===========================2) Get user by id ===========================")
    val existingUser = userService.getUserById(userList[1].id)
    println(existingUser)

    println("===========================3) Get all crypto exchange ===========================")
    val cryptoExchangeList = tradingService.getAllCryptoExchange()
    for (cryptoExchange in cryptoExchangeList) {
        println(cryptoExchange)
    }

    println("===========================4)Exchange crypto ===========================")
    try {
        tradingService.exchangeCryptoCurrency(
            userList[0].wallets.first(),
            "123456789",
            cryptoExchangeList[0],
            BTC,
            userList[0]
        )

        println("Result: ")
        userList = userService.getAllUser()
        for (user in userList) {
            println(user)
        }
        println(cryptoExchangeList[0].transactionHistory)
    } catch (e: Exception) {
        println(e.message)
    }

    println("===========================5)Swap crypto ===========================")
    userList[0].status = UserStatus.NEW
    try {
        tradingService.swapCurrencyBetweenUsers(
            userList[0],
            userList[1],
            Pair(BTC, BigDecimal.ONE),
            Pair(ETH, BigDecimal.TEN),
            cryptoExchangeList[0]
        )
    } catch (e: Exception) {
        println(e.message)
    }

    println("===========================6)Exchange crypto error ===========================")
    try {
        tradingService.exchangeCryptoCurrency(
            userList[0].wallets.first(),
            "12345678",
            cryptoExchangeList[0],
            USD,
            userList[0]
        )
    } catch (e: Exception) {
        println(e.message)
    } finally {
        println("Result: ")
        println(cryptoExchangeList[0].transactionHistory)
    }
    println("===========================PART-2 ===========================")

    println("===========================1)Sequence vs List ===========================")
    measureTimeUserFilter()

    println("===========================2)Delegation ===========================")
    val applicationDependencies = ApplicationDependenciesImpl()
    println(applicationDependencies.fetchData())
    val applicationDependenciesImplDelegation = ApplicationDependenciesImplDelegation(applicationDependencies)
    println(applicationDependenciesImplDelegation.fetchData())

    println("===========================3)Extension function swap for pair ===========================")
    val firstPair = Pair(1, "one")
    val resOfSwap = firstPair.swap()
    println("Before: $firstPair, after: $resOfSwap")

    println("===========================4)Generate range ===========================")
    val range = createRange(
        start = 9
    )
    println(range)

    println("===========================5)'When' block ===========================")
    val transactionRepository = TransactionRepository
    val transactionList = transactionRepository.findAll()
    for (transaction in transactionList)
        println(getUserTransaction(transaction))

    println("===========================6)Get user with transform email ===========================")
    println(userList[0].transformEmail())

    println("===========================7)Get user with transform email ===========================")
    println(userList)

    println("===========================8)Filter user by wallets ===========================")
    val filteredUserList = filterListOfUserByNumberOfWallets(userList, 2)
    filteredUserList.forEachIndexed { index, user ->
        println("User$index has ${user.numberOfWallets} wallets")
    }

    println("===========================9)Map from user List ===========================")
    val pairOfMap = createMapsFromUserList(filteredUserList)
    println("First map: ${pairOfMap.first}")
    println("Second map: ${pairOfMap.second}")

    println("===========================10)Fibonacci ===========================")
    println(fibonacci(6))
    println(fibonacci(9))

    println("===========================11)Destructuring declarations ===========================")
    println("1)Data class")
    val user = userList[0]
    val (_, fullName) = user
    println("User with full name: $fullName")
    println("User with wallets: ${user.component3()}")
    println("2)Class")
    val wallet = user.wallets.first()
    val (name) = wallet
    println("Wallet with name: $name")

    println("===========================PART-3 ===========================")

}

fun initData() {
    val wallet1 = Wallet("wallet1", false, "123456789")
    wallet1.topUpWallet(BTC, BigDecimal.valueOf(4))
    val wallet2 = Wallet("wallet2", false, "123456789")
    wallet2.topUpWallet(BTC, BigDecimal.valueOf(3))
    wallet2.topUpWallet(ETH, BigDecimal.valueOf(123))
    val wallet3 = Wallet("wallet3", false, "123456789")
    wallet3.topUpWallet(BTC, BigDecimal.valueOf(4))
    val wallet4 = Wallet("wallet4", false, "123456789")
    wallet4.topUpWallet(BTC, BigDecimal.valueOf(4))
    val wallet5 = Wallet("wallet5", false, "123456789")
    wallet5.topUpWallet(BTC, BigDecimal.valueOf(4))
    val wallet6 = Wallet("wallet6", false, "123456789")
    wallet6.topUpWallet(BTC, BigDecimal.valueOf(4))
    val wallet7 = wallet1 + wallet2

    val user1 = User("test1@gmail.com", "Test1 Test1", mutableSetOf(wallet1, wallet4), UserStatus.APPROVED)
    val user2 = User("test2@gmail.com", "Test2 Test2", mutableSetOf(wallet2, wallet5, wallet6), UserStatus.APPROVED)
    val user3 = User("test3@gmail.com", "Test3 Test3", mutableSetOf(wallet3, wallet7), UserStatus.APPROVED)
    userRepository.use {
        it.save(user1)
        it.save(user2)
        it.save(user3)
    }

    val cryptoExchange = CryptoExchange("Binance")
    cryptoExchange.exchangeRates[Pair(BTC, USD)] = BigDecimal.valueOf(43125)
    cryptoExchangeRepository.use {
        it.save(cryptoExchange)
    }
}