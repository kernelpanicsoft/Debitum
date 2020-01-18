package model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = arrayOf(Index("deuda_ID")),
    foreignKeys = arrayOf(
    ForeignKey(entity =  Deuda::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("deuda_ID"),
    onDelete = ForeignKey.CASCADE)
))
class Pago (@PrimaryKey(autoGenerate = true) var id: Int,
            var monto: Float = 0f,
            var fecha: String? = null,
            var nota: String? = null,
            var deuda_ID: Int? = null)