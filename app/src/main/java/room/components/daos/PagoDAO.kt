package room.components.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import model.Pago

@Dao
interface PagoDAO {
    @Insert
    fun insert(pago: Pago)

    @Update
    fun update(pago: Pago)

    @Delete
    fun delete(pago: Pago)

    @Query("DELETE FROM Pago")
    fun deleteAllPagos()

    @Query("SELECT * FROM Pago")
    fun getAllPagos(): LiveData<Pago>

    @Query("SELECT * FROM Pago WHERE Pago.id = :id")
    fun getPago(id: Int?): LiveData<Pago>
}