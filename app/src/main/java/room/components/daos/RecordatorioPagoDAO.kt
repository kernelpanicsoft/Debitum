package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
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