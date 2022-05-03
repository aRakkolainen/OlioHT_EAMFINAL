/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
package com.example.olio_ht.ui.home;

public class Date {
    private int day;
    private int month;
    private int year;
    Date(int d, int m, int y) {
        day = d;
        month = m;
        year = y;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
