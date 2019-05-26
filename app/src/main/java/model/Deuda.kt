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
            var tipo: Int = 0,
            var monto: Float = 0f,
            var nota: String? = null,
            var fecha_adquision: String? = null,
            var pagado: Float = 0f,
            var estado: Int? = null,
            var cuenta_id: Int? = null
            )