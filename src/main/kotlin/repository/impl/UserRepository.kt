package repository.impl

import constants.CryptoAppConstant.Companion.FILE_PATH
import domain.User
import repository.GenericRepository
import java.io.FileWriter

object UserRepository : GenericRepository<User>({ initializeFileWriter(FileWriter(FILE_PATH, true)) })