package repository

import java.util.*

open class GenericRepository<Item> : Repository<Item> where Item : Identifiable {
    private val items: MutableList<Item> = mutableListOf()

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
}