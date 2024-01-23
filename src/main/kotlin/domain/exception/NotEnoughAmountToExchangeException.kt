package domain.exception

import domain.Currency
import java.util.UUID

class NotEnoughAmountToExchangeException(
    userId: UUID,
    currency: Currency
): RuntimeException("User with ID '$userId' haven't enough crypto currency '${currency.code}' to exchange!")