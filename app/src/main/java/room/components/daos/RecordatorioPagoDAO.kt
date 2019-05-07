package room.components.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
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
}