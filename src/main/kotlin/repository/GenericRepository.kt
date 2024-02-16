package repository

import config.Config
import config.configInitializer
import java.util.*

open class GenericRepository<Item>(configInitializer: Config.() -> Unit) : AutoCloseable,
    Repository<Item> where Item : Identifiable {
    private val items: MutableList<Item> = mutableListOf()
    private val config = Config().configInitializer(configInitializer)

    override fun findById(id: UUID): Item? =
        items.find { it -> it.id == id }

    override fun findAll(): List<Item> = items

    override fun save(item: Item) {
        if (items.any { it.id == item.id }) {
            items.replaceAll { if (it.id == item.id) item else it }
        } else {
            items.add(item)
        }
    }

    override fun delete(id: UUID) {
        items.removeIf { it.id == id }
    }

    override fun close() {
        val messageToWrite = this.javaClass.simpleName + " end his work! Time = " + System.currentTimeMillis() + "\n";
        config.let {
            it.fileWriter.append(messageToWrite);
            it.fileWriter.flush()
        }
    }
}