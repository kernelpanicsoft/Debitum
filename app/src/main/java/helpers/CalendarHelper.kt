package helpers

import java.time.DayOfWeek
import java.util.*

class CalendarHelper {


    var sundaysInMonth: Int = 0
    var mondaysInMonth: Int = 0
    var tuesdaysInMonth: Int = 0
    var wednesdaysInMonth: Int = 0
    var thursdaysInMonth: Int = 0
    var fridaysInMonth: Int = 0
    var saturdaysInMonth: Int = 0

    init {
        countDaysOfCurrentMonth()
    }


    private fun countDaysOfCurrentMonth(){
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,1)

        for(day : Int in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            calendar.set(Calendar.DAY_OF_MONTH,day)

            when(calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.SUNDAY -> sundaysInMonth++
                Calendar.MONDAY -> mondaysInMonth++
                Calendar.TUESDAY -> tuesdaysInMonth++
                Calendar.WEDNESDAY -> wednesdaysInMonth++
                Calendar.THURSDAY -> thursdaysInMonth++
                Calendar.FRIDAY -> fridaysInMonth++
                Calendar.SATURDAY -> saturdaysInMonth++
            }
        }

    }

    override fun toString() : String{
        return "Dias: " + sundaysInMonth + " | " + mondaysInMonth + " | " + tuesdaysInMonth + " | " + wednesdaysInMonth + " | " + thursdaysInMonth + " | " + fridaysInMonth + " | " + saturdaysInMonth
    }


}