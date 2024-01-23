package repository

import java.util.*

interface UniqueItem {
    val id: UUID
    fun getUniqueId(): UUID
}