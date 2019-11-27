package model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Deuda::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("deudaID"),
        onDelete = ForeignKey.CASCADE)
))
class RecordatorioPago (@PrimaryKey(autoGenerate = true) var id: Int,
                        var fecha: String? = null,
                        var nota: String? = null,
                        var tipo: Int? = null,
                        var monto: Float? = null,
                        var montoMensual: Float? = null,
                        var deudaID: Int? = null
                        )