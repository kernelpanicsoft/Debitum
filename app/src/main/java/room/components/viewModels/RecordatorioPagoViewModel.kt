package room.components.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import model.RecordatorioPago
import room.components.repositories.RecordatorioPagoRepository

class RecordatorioPagoViewModel (application : Application) : AndroidViewModel(application) {
    val repository : RecordatorioPagoRepository = RecordatorioPagoRepository(application)

    fun insert(recordatorioPago: RecordatorioPago){
        repository.insert(recordatorioPago)
    }

    fun update(recordatorioPago: RecordatorioPago){
        repository.update(recordatorioPago)
    }

    fun delete(recordatorioPago: RecordatorioPago){
        repository.delete(recordatorioPago)
    }

    fun deleteAllRecordatoriosPago(){
        repository.deleteAllRecordatoriosPago()
    }

    fun getRecordatorioPago(id: Int){
        repository.getAllRecordatoriosPago()
    }
}