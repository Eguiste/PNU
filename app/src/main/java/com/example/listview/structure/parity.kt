package com.example.listview.structure
import org.joda.time.LocalDate
import org.joda.time.Weeks

fun parity(): Int {
    val now = LocalDate.now()
    val year = now.year
    val month = now.monthOfYear
    val september: LocalDate
    september = if (month in 1..8) {
        LocalDate(year - 1, 9, 1)
    } else {
        LocalDate(year, 9, 1)
    }
    val weeks = Weeks.weeksBetween(september, now).weeks
    return weeks % 2
}