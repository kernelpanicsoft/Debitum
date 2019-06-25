package model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Cuenta (@PrimaryKey(autoGenerate = true) var id: Int,
              var deuda_total: Float = 0f)