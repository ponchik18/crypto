package repository.impl

import constants.CryptoAppConstant
import domain.CryptoExchange
import repository.GenericRepository
import java.io.FileWriter

object CryptoExchangeRepository : GenericRepository<CryptoExchange>({ initializeFileWriter(FileWriter(CryptoAppConstant.FILE_PATH, true)) })