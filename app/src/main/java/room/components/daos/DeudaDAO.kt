package room.components.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import model.Deuda
import helpcodes.EstatusDeuda

@Dao
interface DeudaDAO {
    @Insert
    fun insert(deuda: Deuda)

    @Update
    fun update(deuda: Deuda)

    @Delete
    fun delete(deuda: Deuda)

    @Query("DELETE FROM Deuda")
    fun deleteAllDeudas()

    @Query("SELECT * FROM Deuda")
    fun getAllDeudas(): LiveData<List<Deuda>>

    @Query("SELECT * FROM Deuda WHERE Deuda.id = :id")
    fun getDeuda(id: Int): LiveData<Deuda>

    @Query("UPDATE Deuda SET pagado = pagado+:monto WHERE Deuda.id = :id")
    fun updateMontoDeuda(id: Int, monto: Float)

    @Query("SELECT * FROM DEUDA WHERE Deuda.estado = :estado")
    fun getDeudasByState(estado: Int) : LiveData<List<Deuda>>

    @Query("SELECT * FROM Deuda WHERE Deuda.estado = " + EstatusDeuda.ACTIVA + " OR Deuda.estado =" + EstatusDeuda.SEGUIR)
    fun getAllDeudasActivas() : LiveData<List<Deuda>>

    @Query("UPDATE Deuda SET estado = :estado WHERE Deuda.id = :id")
    fun updateStatusDeuda(id: Int, estado: Int)


}