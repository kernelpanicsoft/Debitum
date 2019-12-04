package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.JoinDeudaRecordatorio
import model.RecordatorioPago

@Dao
interface RecordatorioPagoDAO {
    @Insert
    fun insert(recodatorio: RecordatorioPago)

    @Update
    fun update(recodatorio: RecordatorioPago)

    @Delete
    fun delete(recodatorio: RecordatorioPago)

    @Query("DELETE FROM RecordatorioPago")
    fun deleteAllRecordatoriosPago()

    @Query("SELECT * FROM RecordatorioPago")
    fun getAllRecordatoriosPago() : LiveData<List<RecordatorioPago>>

    @Query("SELECT * FROM RecordatorioPago WHERE RecordatorioPago.id = :id")
    fun getRecordatorioPago(id: Int?) : LiveData<RecordatorioPago>

    @Query("SELECT SUM(monto) FROM RecordatorioPago")
    fun getSumaRecordatorios() : LiveData<Float>

    @Query("SELECT RecordatorioPago.id, RecordatorioPago.tipo, RecordatorioPago.nota, RecordatorioPago.fecha, RecordatorioPago.monto, Deuda.titulo from RecordatorioPago JOIN Deuda ON Deuda.id = RecordatorioPago.deudaID")
    fun getRecordatoriosPagoDeuda() : LiveData<List<JoinDeudaRecordatorio>>

    @Query("SELECT SUM(montoMensual) FROM RECORDATORIOPAGO")
    fun getSumaPagosSemanales() : LiveData<Float>

    @Query("SELECT SUM(montoMensual) + (SELECT monto FROM RECORDATORIOPAGO WHERE RecordatorioPago.tipo = 1 ) FROM RecordatorioPago")
    fun getSumaPagos() : LiveData<Float>

    @Query("SELECT id, fecha FROM RecordatorioPago WHERE  strftime('%w', 'now') = fecha")
    fun getRemindersOfTheDay() : LiveData<List<RecordatorioPago>>

}