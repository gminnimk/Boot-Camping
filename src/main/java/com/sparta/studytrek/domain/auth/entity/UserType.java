package com.sparta.studytrek.domain.auth.entity;

public enum UserType {
    NORMAL(Authority.NORMAL),  // 정상 유저
    LEAVE(Authority.LEAVE);  // 탈퇴 유저

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }

    public static class Authority {
        public static final String NORMAL = "NORMAL";
        public static final String LEAVE = "LEAVE";
    }
}