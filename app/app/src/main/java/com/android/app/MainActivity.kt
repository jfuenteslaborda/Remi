package com.android.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var monthTitle: TextView
    private lateinit var calendarGrid: RecyclerView
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton

    private var currentYearMonth = YearMonth.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        monthTitle = findViewById(R.id.monthTitle)
        calendarGrid = findViewById(R.id.calendarGrid)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)

        calendarGrid.layoutManager = GridLayoutManager(this, 7)

        updateCalendar()

        prevButton.setOnClickListener {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateCalendar()
        }

        nextButton.setOnClickListener {
            currentYearMonth = currentYearMonth.plusMonths(1)
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        val monthName = currentYearMonth.month.getDisplayName(
            TextStyle.FULL,
            Locale("es", "ES")
        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }
        monthTitle.text = monthName

        val days = getDaysInMonth(currentYearMonth)
        val events = getSampleEvents()

        val adapter = DayAdapter(days, events)
        calendarGrid.adapter = adapter
    }

    private fun getDaysInMonth(yearMonth: YearMonth): List<Int> {
        val days = mutableListOf<Int>()

        val firstDay = yearMonth.atDay(1)
        val firstDayOfWeek = firstDay.dayOfWeek.value - 1 // 0 = lunes, 6 = domingo

        repeat(firstDayOfWeek) {
            days.add(0)
        }

        val lastDay = yearMonth.lengthOfMonth()
        for (i in 1..lastDay) {
            days.add(i)
        }

        while (days.size % 7 != 0) {
            days.add(0)
        }

        return days
    }

    private fun getSampleEvents(): Map<Int, List<String>> {
        return mapOf(
            4 to listOf("blue"),
            8 to listOf("green"),
            12 to listOf("red"),
            15 to listOf("blue", "green"),
            20 to listOf("blue"),
            21 to listOf("red"),
            25 to listOf("green"),
            28 to listOf("blue")
        )
    }
}
