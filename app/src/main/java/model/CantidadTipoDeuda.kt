package model

import androidx.room.ColumnInfo

data class CantidadTipoDeuda (
    @ColumnInfo(name = "tipo") val tipo: Int?,
    @ColumnInfo(name = "cantidad") val cantidadTipo: Int?
)