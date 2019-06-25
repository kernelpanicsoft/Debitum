package room.components

import androidx.lifecycle.ViewModelProviders
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import model.*
import room.components.daos.CuentaDAO
import room.components.daos.DeudaDAO
import room.components.daos.PagoDAO
import room.components.daos.RecordatorioPagoDAO
import room.components.viewModels.CuentaViewModel

@Database(entities = arrayOf(Cuenta::class,
    Deuda::class,
    Pago::class,
    RecordatorioPago::class),
    version = 1)
abstract class DebitumDataBase : RoomDatabase() {
    abstract fun cuentaDao(): CuentaDAO
    abstract fun deudaDao(): DeudaDAO
    abstract fun pagoDao(): PagoDAO
    abstract fun recordatorioPagoDao(): RecordatorioPagoDAO

    companion object{
        @Volatile private var instance: DebitumDataBase? = null

        fun getInstance(context: Context) : DebitumDataBase =
                instance ?: synchronized(this){
                    instance ?: buildDatabase(context).also { instance = it}
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    DebitumDataBase::class.java, "Debitum.db")
                    .addCallback( object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                           // instance?.cuentaDao()?.insert(Cuenta(0,0f))
                            db.execSQL("INSERT INTO Cuenta VALUES (1,0.0);")
                        }
                    }
                    )
                    .build()
    }
}