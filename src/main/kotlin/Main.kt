import domain.CryptoExchange
import domain.Currency
import domain.User
import domain.Wallet
import enums.UserStatus
import repository.impl.CryptoExchangeRepository
import repository.impl.TransactionRepository
import repository.impl.UserRepository
import service.TradingService
import service.UserService
import service.impl.TradingServiceImpl
import service.impl.UserServiceImpl
import java.math.BigDecimal


val usd = Currency("United States Dollar", "USD", "$")
val btc = Currency("Bitcoin", "BTC", "₿")
val eth = Currency("Ethereum", "ETH", "Ξ")

val userRepository = UserRepository()
val transactionRepository = TransactionRepository()
val cryptoExchangeRepository = CryptoExchangeRepository()

var userService: UserService = UserServiceImpl(userRepository, cryptoExchangeRepository)
var tradingService: TradingService = TradingServiceImpl(cryptoExchangeRepository, transactionRepository)

fun main(args: Array<String>) {
    initData()
    var userList = userService.getAllUser()
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
    tradingService.exchangeCryptoCurrency(
        userList[0],
        userList[1],
        Pair(btc, BigDecimal.ONE),
        Pair(eth, BigDecimal.TEN),
        cryptoExchangeList[0]
    )
    println("Result: ")
    userList = userService.getAllUser()
    for (user in userList) {
        println(user)
    }
    println(cryptoExchangeList[0].transactionHistory)

    println("===========================5)Exchange crypto error ===========================")
    userList[0].status = UserStatus.NEW
    try {
        tradingService.exchangeCryptoCurrency(
            userList[0],
            userList[1],
            Pair(btc, BigDecimal.ONE),
            Pair(eth, BigDecimal.TEN),
            cryptoExchangeList[0]
        )
    } catch (e: Exception) {
        println(e.message)
    }

    println("===========================6)Swap crypto ===========================")
    try {
    tradingService.swapCrypto(
        userList[0].wallets.first(),
        "123456789",
        cryptoExchangeList[0],
        usd,
        userList[0]
    )
    } catch (e: Exception) {
        println(e.message)
    } finally {
        println("Result: ")
        println(cryptoExchangeList[0].transactionHistory)
    }

}

fun initData() {
    val wallet1 = Wallet("wallet1", false, "123456789")
    wallet1.topUpWallet(btc, BigDecimal.valueOf(4))
    val wallet2 = Wallet("wallet2", false, "123456789")
    wallet2.topUpWallet(btc, BigDecimal.valueOf(3))
    wallet2.topUpWallet(eth, BigDecimal.valueOf(123))

    val user1 = User("test1@gmail.com", "Test1 Test1", mutableSetOf(wallet1), UserStatus.APPROVED)
    val user2 = User("test2@gmail.com", "Test2 Test2", mutableSetOf(wallet2), UserStatus.APPROVED)
    userRepository.save(user1)
    userRepository.save(user2)

    val cryptoExchange = CryptoExchange("Binance")
    cryptoExchange.exchangeRates[Pair(btc, usd)] = BigDecimal.valueOf(43125)
    cryptoExchangeRepository.save(cryptoExchange)
}