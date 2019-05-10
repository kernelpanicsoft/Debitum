package room.components.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import model.Deuda
import room.components.repositories.DeudaRepository

class DeudaViewModel (application: Application) : AndroidViewModel(application) {
    val repository : DeudaRepository = DeudaRepository(application)

    fun insert(deuda: Deuda){
        repository.insert(deuda)
    }

    fun update(deuda: Deuda){
        repository.update(deuda)
    }

    fun delete(deuda: Deuda){
        repository.delete(deuda)
    }

    fun deleteAllDeudas(){
        repository.deleteAllDeudas()
    }

    fun getDeuda(id: Int): LiveData<Deuda>{
        return repository.getDeuda(id)
    }

    fun getAllDeudas(): LiveData<List<Deuda>>{
        return repository.getAllDeudas()
    }
}