package domain.transaction

import domain.User
import java.math.BigDecimal
import java.time.Instant
import domain.Currency

class TradeTransaction(
    date: Instant,
    initiator: User,
    fromCurrency: Currency,
    fromAmount: BigDecimal,
    val toCurrency: Currency,
    val toAmount: BigDecimal
) : Transaction(date, initiator, fromCurrency, fromAmount)