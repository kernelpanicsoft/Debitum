package room.components.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import model.JoinDeudaRecordatorio
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

    fun getAllRecordatorios() : LiveData<List<RecordatorioPago>>{
       return repository.getAllRecordatoriosPago()
    }

    fun getRecordatorio(id: Int) : LiveData<RecordatorioPago>{
        return repository.getRecordatorioPago(id)
    }

    fun getSumaRecordatorios() : LiveData<Float>{
        return repository.getSumaRecordatorios()
    }

    fun getRecordatoriosPagoDeuda() : LiveData<List<JoinDeudaRecordatorio>>{
        return repository.getRecordatoriosPagoDeuda()
    }
}