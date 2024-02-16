package constants

import domain.Currency

class CryptoAppConstant {
    companion object {
        object Currency {
            val USD = Currency("United States Dollar", "USD", "$")
            val BTC = Currency("Bitcoin", "BTC", "₿")
            val ETH = Currency("Ethereum", "ETH", "Ξ")
        }
        val FILE_PATH: String = "src/main/resources/log.txt"
    }
}