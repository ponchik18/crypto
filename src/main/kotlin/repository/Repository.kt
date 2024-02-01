package repository

import java.util.*

interface Repository<Item> where Item : UniqueItem {
    fun getItem(id: UUID): Item?
    fun getAllItem(): List<Item>
    fun save(item: Item)
    fun delete(id: UUID)
}