package model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(foreignKeys = arrayOf(
    ForeignKey(entity =  Deuda::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = ForeignKey.CASCADE)
))
class Pago (@PrimaryKey(autoGenerate = true) var id: Int,
            var monto: Float? = 0f,
            var fecha: String? = null,
            var nota: String? = null)