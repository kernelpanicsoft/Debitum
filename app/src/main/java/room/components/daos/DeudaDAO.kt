package room.components.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import model.Deuda

@Dao
interface DeudaDAO {
    @Insert
    fun inser(deuda: Deuda)

    @Update
    fun update(deuda: Deuda)

    @Delete
    fun delete(deuda: Deuda)

    @Query("DELETE FROM Deuda")
    fun deleteAllDeudas()

    @Query("SELECT * FROM Deuda")
    fun getAllDeudas(): LiveData<List<Deuda>>



}