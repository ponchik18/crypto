package repository

import java.util.*

interface Repository<Item> where Item : Identifiable {
    fun findById(id: UUID): Item?
    fun findAll(): List<Item>
    fun save(item: Item)
    fun delete(id: UUID)
}