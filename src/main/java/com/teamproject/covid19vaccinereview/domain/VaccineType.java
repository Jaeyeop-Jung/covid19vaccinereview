package com.teamproject.covid19vaccinereview.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum VaccineType {
    ASTRAZENECA, PFIZER, MODERNA, JANSSEN;

    private static final List<VaccineType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static VaccineType getRandomVaccineType()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
