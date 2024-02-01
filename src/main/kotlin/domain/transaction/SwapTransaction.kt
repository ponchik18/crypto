package domain.transaction

import domain.User
import java.math.BigDecimal
import java.time.Instant
import domain.Currency

class SwapTransaction(
    date: Instant,
    receiver: User,
    fromCurrency: Currency,
    fromAmount: BigDecimal,
    val toCurrency: Currency,
    val toAmount: BigDecimal
) : Transaction(date, receiver, fromCurrency, fromAmount)