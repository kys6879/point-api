package com.musinsa.pointapi.common;

import java.time.LocalDateTime;

public class CommonDateService {

    public static LocalDateTime getToday() {return LocalDateTime.now();}
    public static LocalDateTime getAfterOneYear(LocalDateTime dateTime) {return dateTime.plusYears(1);}

}
