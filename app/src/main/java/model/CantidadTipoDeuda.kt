package model

import androidx.room.ColumnInfo

data class CantidadTipoDeuda (
    @ColumnInfo(name = "COUNT(Deuda.id)") val tipoDeuda: Float?
  //  @ColumnInfo(name = "columna2") val cantidadPorTipo: Int?
)