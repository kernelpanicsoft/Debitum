package model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Cuenta::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("cuenta_id"),
    onDelete = ForeignKey.CASCADE)
))
class Deuda(@PrimaryKey(autoGenerate = true) var id: Int,
            var titulo: String? = null,
            var monto: Float? = 0f,
            var fecha_adquision: String? = null,
            var saldo: Float? = 0f,
            var nota: String? = null,
            var cuenta_id: Int? = null
            )