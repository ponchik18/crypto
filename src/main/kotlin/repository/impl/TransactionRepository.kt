package repository.impl

import constants.CryptoAppConstant
import domain.transaction.Transaction
import repository.GenericRepository
import java.io.FileWriter

object TransactionRepository :
    GenericRepository<Transaction>({ initializeFileWriter(FileWriter(CryptoAppConstant.FILE_PATH, true)) })