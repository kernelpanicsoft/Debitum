package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
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
    fun getAllPagos(): LiveData<List<Pago>>

    @Query("SELECT * FROM Pago WHERE Pago.id = :id")
    fun getPago(id: Int?): LiveData<Pago>

    @Query("SELECT * FROM Pago WHERE Pago.deuda_ID = :deudaID")
    fun getAllPagosDeuda(deudaID: Int): LiveData<List<Pago>>
}