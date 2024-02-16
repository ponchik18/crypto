package config

import java.io.FileWriter

class Config {
    private lateinit var _fileWriter: FileWriter

    val fileWriter: FileWriter
        get() {
            check(::_fileWriter.isInitialized) { "FileWriter should be initialized!" }
            return _fileWriter
        }

    fun initializeFileWriter(value: FileWriter) {
        _fileWriter = value
    }
}

fun Config.configInitializer(initializer: Config.() -> Unit): Config {
    this.initializer()
    return this
}