package repository

import java.util.*

open class GenericRepository<Item> : Repository<Item> where Item : UniqueItem {
    private val items: MutableList<Item> = mutableListOf()

    override fun getItem(id: UUID): Item? =
        items.find { it -> it.getUniqueId() == id }

    override fun getAllItem(): List<Item> = items

    override fun save(item: Item) {
        if (items.any { it -> it.getUniqueId() == item.getUniqueId() }) {
            items.replaceAll { if (it.getUniqueId() == item.getUniqueId()) item else it }
        } else {
            items.add(item)
        }
    }

    override fun delete(id: UUID) {
        items.removeIf { it.getUniqueId() == id }
    }
}