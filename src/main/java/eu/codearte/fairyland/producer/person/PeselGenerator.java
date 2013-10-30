package eu.codearte.fairyland.producer.person;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.String.format;

public class PeselGenerator {

    public static final int[] WEIGHTS = new int[]{1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    public String pesel(GregorianCalendar calendar, PersonHolder.Sex sex) {

        int yearField = calculateYear(calendar.get(Calendar.YEAR));
        int month = calculateMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int number = (int) (Math.random() * 999);
        int sexCode = calculateSex(sex);

        String pesel = format("%02d%02d%02d%03d%d", yearField, month, day, number, sexCode);

        return pesel + calculateControl(pesel);
    }

    /**
     * FIXME: Move to other class
     * @param pesel
     * @return
     */
    public static boolean isValidPesel(String pesel) {
        int size = pesel.length();
        if (size != 11) {
            return false;
        }
        int checksum = Integer.valueOf(pesel.substring(size - 1));
        int control = PeselGenerator.calculateControl(pesel);

        return control == checksum;

    }

    private int calculateSex(PersonHolder.Sex sex) {
        return ((int) (Math.random() * 5)) * 2 + (sex == PersonHolder.Sex.male ? 1 : 0);
    }

    private int calculateYear(int year) {
        return year % 100;
    }

    private int calculateMonth(int month, int year) {
        if (year >= 1800 && year < 1900) {
            month += 80;
        } else if (year >= 2000 && year < 2100) {
            month += 20;
        } else if (year > 2100 && year < 2200) {
            month += 40;
        } else if (year > 2200 && year < 2300) {
            month += 60;
        }
        return month;
    }

    private static int calculateControl(String pesel) {
        int j = 0, sum = 0, control = 0;
        for (int i = 0; i < 10; i++) {
            char c = pesel.charAt(i);
            j = Integer.valueOf(String.valueOf(c));
            sum += j * WEIGHTS[i];
        }
        control = 10 - (sum % 10);
        return control % 10;
    }
}
