package model

import androidx.room.ColumnInfo


data class JoinDeudaRecordatorio (
    @ColumnInfo(name = "id") val recordatorioID : Int?,
    @ColumnInfo(name = "titulo") val tituloDeuda : String?,
    @ColumnInfo(name = "nota") val notaRecordatorio : String?,
    @ColumnInfo(name = "monto") val montoRecordatorio : String,
    @ColumnInfo(name = "fecha") val fechaRecordatorio : String
    )
