package ir.faj.jalas.jalas.utility


import com.github.mfathi91.time.PersianDate
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import com.ibm.icu.util.ULocale
import com.ibm.icu.util.Calendar

/**
 * Created by r on 4/17/18.
 */

private val mongoFormat = SimpleDateFormat("dd-MM-yyyy")
private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


fun String.fromMongoDateString() = mongoFormat.parse(this)

fun Date.toMongoDateString() = mongoFormat.format(this) ?: throw RuntimeException("Failed to format $this")
fun Date.toTimeString() = timeFormat.format(this) ?: throw RuntimeException("Failed to format $this")

fun Date.endOfDay() = DateTime(this)
        .withHourOfDay(23)
        .withMinuteOfHour(59)
        .withSecondOfMinute(59)
        .withMillisOfSecond(999)
        .toDate()

fun Date.startOfDay(): Date
{
    return try {
        DateTime(this)
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0)
                .toDate()
    }
    catch (e: Exception){
        DateTime(this)
                .withHourOfDay(1)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0)
                .toDate()
    }
}

fun Date.startOfMonthSolar() = PersianCalendarUtils.getStartOfMonth(this)
fun Date.addMonthsSolar(months: Int) = PersianCalendarUtils.addMonth(months, this)
fun Date.daysDiffFrom(other: Date) = Math.abs(Days.daysBetween(DateTime(this), DateTime(other)).days)
fun Date.addDays(days: Int) = DateTime(this).plusDays(days).toDate()
fun Date.addHours(hours: Int) = DateTime(this).plusHours(hours).toDate()
fun Date.addMinuts(minutes: Int) = DateTime(this).plusMinutes(minutes).toDate()
fun Date.addSecound(secound: Int) = DateTime(this).plusSeconds(secound).toDate()


fun min(first: Date, second: Date): Date = if(first.before(second)) first else second
fun max(first: Date, second: Date): Date = if(first.after(second)) first else second
fun Date.toPersianDateString(): String? {

    val utility = SolarCalendarUtility(this)
    val year = utility.year
    val month = utility.month
    val day = utility.date
    return "$year/$month/$day"
}

fun Date.dayOf() = SolarCalendarUtility(this).date


private fun isPersianLeapYear(yr: Int) = !((yr % 4 == 0) || (!(yr % 100 == 0) && (yr % 400 == 0)))

fun Date.getPersianNumberOfDayInMonth(): Int {
    val utility = SolarCalendarUtility(this)
    val month = utility.month
    val year = utility.year

    return when {
        month <= 6 -> 31
        month <= 11 -> 30
        isPersianLeapYear(year) -> 30
        else -> 29
    }
}

fun Date.convertToJalaliDateAsArray(): List<String> {
    return convertToJalaliDate().toString().split("-")
}

fun Date.convertToJalaliDate(): PersianDate {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateParts = dateFormat.format(this).split("-")

    return PersianDate.fromGregorian(LocalDate.of(
            dateParts[0].toInt(),
            dateParts[1].toInt(),
            dateParts[2].toInt())
    )
}

fun String.toDate(): Date {
    return SimpleDateFormat("dd-MM-yyyy").parse(this)
}

object PersianCalendarUtils {


    fun getStartOfMonth(date: Date): Date {
        val calendar = getAndconfigCalender()
        calendar.time = date
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) * -1 + 1
        calendar.add(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 10)
        calendar.set(Calendar.SECOND, 10)
        calendar.set(Calendar.MILLISECOND, 10)
        return calendar.time
    }

    fun monthsBetween(start: Date, end: Date): Int {
        val startCalendar = getAndconfigCalender()
        val endCalender = getAndconfigCalender()

        startCalendar.time = start
        endCalender.time = end
        val monthDiff = endCalender.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        return Math.abs((Math.abs(startCalendar.get(Calendar.YEAR) - endCalender.get(Calendar.YEAR))) * 12 + monthDiff)

    }

    fun addHour(hours: Int, date: Date): Date {
        val calendar = getAndconfigCalender()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        return calendar.time
    }

    fun addMonth(months: Int, date: Date): Date {
        val calendar = getAndconfigCalender()
        calendar.time = date
        calendar.add(Calendar.MONTH, months)
        return calendar.time
    }

    fun getAndconfigCalender(): Calendar {
        var calendar: Calendar
        val locale = ULocale("fa_IR@calendar=persian")

        calendar = Calendar.getInstance(locale)
        calendar.firstDayOfWeek = 7
        return calendar
    }

}
