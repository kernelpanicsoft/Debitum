package room.components.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import model.Pago
import room.components.repositories.PagoRepository

class PagoViewModel (application: Application) : AndroidViewModel(application) {
    val repository : PagoRepository = PagoRepository(application)

    fun insert(pago: Pago){
        repository.insert(pago)
    }

    fun update(pago: Pago){
        repository.update(pago)
    }

    fun delete(pago: Pago){
        repository.delete(pago)
    }

    fun deleteAllPagos(){
        repository.deleteAllPAgos()
    }

    fun getPago(id : Int) : LiveData<Pago>{
        return repository.getPago(id)
    }

    fun getAllPagos() : LiveData<List<Pago>>{
        return repository.getAllPagos()
    }

    fun getAllPagosDeuda(id: Int): LiveData<List<Pago>>{
        return repository.getAllPagosDeuda(id)
    }

}