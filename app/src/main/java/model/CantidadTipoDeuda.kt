package model

import androidx.room.ColumnInfo

data class CantidadTipoDeuda (
    @ColumnInfo(name = "tipo") val tipo: Int?,
    @ColumnInfo(name = "cantidad") val cantidadTipo: Int?,
    @ColumnInfo(name = "montoDeudaTipo") val deudaPorTipo: Float? = 0f,
    @ColumnInfo(name = "pagadoPorTipo") val pagadoPorTipo: Float? = 0f
)