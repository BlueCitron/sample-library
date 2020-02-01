package com.bluecitron.library.entity;

public enum MemberGrade {
    NORMAL("NORMAL", 3)
    , SILVER("SILVER", 5)
    , GOLD("GOLD", 7)
    , PLATINUM("PLATINUM", 10)
    , VIP("VIP", 15);

    private final String name;
    private final int rentalLimit;

    private MemberGrade(String name, int rentalLimit) {
        this.name = name;
        this.rentalLimit = rentalLimit;
    }

    public String getName() {
        return this.name;
    }

    public int getRentalLimit() {
        return this.rentalLimit;
    }
}
