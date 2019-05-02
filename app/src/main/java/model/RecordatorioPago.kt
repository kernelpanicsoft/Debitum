package model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Deuda::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("deudaID"),
        onDelete = ForeignKey.CASCADE)
))
class RecordatorioPago (@PrimaryKey(autoGenerate = true) var id: Int,
                        var fecha: String? = null,
                        var nota: String? = null,
                        var deudaID: Int? = null
                        )