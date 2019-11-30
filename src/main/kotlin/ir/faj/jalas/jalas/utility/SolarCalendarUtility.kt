package ir.faj.jalas.jalas.utility

import java.util.*

class SolarCalendarUtility {
    var strWeekDay = ""
    var strMonth = ""
    var date: Int = 0
    var month: Int = 0
    var year: Int = 0



    constructor(gregorianDate: Date) {
        this.calcSolarCalendar(gregorianDate)
    }

    private fun calcSolarCalendar(gregorianDate: Date) {
        val calendar = GregorianCalendar()
        calendar.time = gregorianDate

        val gregorianYear = calendar.get(Calendar.YEAR)
        val gregorianMonth = calendar.get(Calendar.MONTH) + 1
        val gregorianDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val buf1 = IntArray(12)
        val buf2 = IntArray(12)
        buf1[0] = 0
        buf1[1] = 31
        buf1[2] = 59
        buf1[3] = 90
        buf1[4] = 120
        buf1[5] = 151
        buf1[6] = 181
        buf1[7] = 212
        buf1[8] = 243
        buf1[9] = 273
        buf1[10] = 304
        buf1[11] = 334
        buf2[0] = 0
        buf2[1] = 31
        buf2[2] = 60
        buf2[3] = 91
        buf2[4] = 121
        buf2[5] = 152
        buf2[6] = 182
        buf2[7] = 213
        buf2[8] = 244
        buf2[9] = 274
        buf2[10] = 305
        buf2[11] = 335
        val ld: Byte
        if (gregorianYear % 4 != 0) {
            this.date = buf1[gregorianMonth - 1] + gregorianDayOfMonth
            if (this.date > 79) {
                this.date -= 79
                if (this.date <= 186) {
                    when (this.date % 31) {
                        0 -> {
                            this.month = this.date / 31
                            this.date = 31
                        }
                        else -> {
                            this.month = this.date / 31 + 1
                            this.date %= 31
                        }
                    }

                    this.year = gregorianYear - 621
                } else {
                    this.date -= 186
                    when (this.date % 30) {
                        0 -> {
                            this.month = this.date / 30 + 6
                            this.date = 30
                        }
                        else -> {
                            this.month = this.date / 30 + 7
                            this.date %= 30
                        }
                    }

                    this.year = gregorianYear - 621
                }
            } else {
                if (gregorianYear > 1996 && gregorianYear % 4 == 1) {
                    ld = 11
                } else {
                    ld = 10
                }

                this.date += ld.toInt()
                when (this.date % 30) {
                    0 -> {
                        this.month = this.date / 30 + 9
                        this.date = 30
                    }
                    else -> {
                        this.month = this.date / 30 + 10
                        this.date %= 30
                    }
                }

                this.year = gregorianYear - 622
            }
        } else {
            this.date = buf2[gregorianMonth - 1] + gregorianDayOfMonth
            ld = if (gregorianYear >= 1996) 79 else 80

            if (this.date > ld) {
                this.date -= ld.toInt()
                if (this.date <= 186) {
                    when (this.date % 31) {
                        0 -> {
                            this.month = this.date / 31
                            this.date = 31
                        }
                        else -> {
                            this.month = this.date / 31 + 1
                            this.date %= 31
                        }
                    }

                    this.year = gregorianYear - 621
                } else {
                    this.date -= 186
                    when (this.date % 30) {
                        0 -> {
                            this.month = this.date / 30 + 6
                            this.date = 30
                        }
                        else -> {
                            this.month = this.date / 30 + 7
                            this.date %= 30
                        }
                    }

                    this.year = gregorianYear - 621
                }
            } else {
                this.date += 10
                when (this.date % 30) {
                    0 -> {
                        this.month = this.date / 30 + 9
                        this.date = 30
                    }
                    else -> {
                        this.month = this.date / 30 + 10
                        this.date %= 30
                    }
                }

                this.year = gregorianYear - 622
            }
        }

        when (this.month) {
            1 -> this.strMonth = "فروردين"
            2 -> this.strMonth = "ارديبهشت"
            3 -> this.strMonth = "خرداد"
            4 -> this.strMonth = "تير"
            5 -> this.strMonth = "مرداد"
            6 -> this.strMonth = "شهريور"
            7 -> this.strMonth = "مهر"
            8 -> this.strMonth = "آبان"
            9 -> this.strMonth = "آذر"
            10 -> this.strMonth = "دي"
            11 -> this.strMonth = "بهمن"
            12 -> this.strMonth = "اسفند"
        }

        when (weekDay) {
            0 -> this.strWeekDay = "يکشنبه"
            1 -> this.strWeekDay = "دوشنبه"
            2 -> this.strWeekDay = "سه شنبه"
            3 -> this.strWeekDay = "چهارشنبه"
            4 -> this.strWeekDay = "پنج شنبه"
            5 -> this.strWeekDay = "جمعه"
            6 -> this.strWeekDay = "شنبه"
        }

    }
}
