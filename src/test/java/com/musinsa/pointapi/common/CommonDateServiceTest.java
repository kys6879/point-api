package com.musinsa.pointapi.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class CommonDateServiceTest {

    @DisplayName("특정 날짜로부터 1년 후 구하기")
    @Test
    public void getAfterOneYearTest() {

        /* Given */
        LocalDateTime localDateTime = LocalDateTime.of(2020,3,2,12,0);

        /* When */
        LocalDateTime afterOneYear = CommonDateService.getAfterOneYear(localDateTime);

        /* Then */
        LocalDateTime expected = LocalDateTime.of(2021,3,2,12,0);

        assertEquals(expected,afterOneYear);
    }


}
