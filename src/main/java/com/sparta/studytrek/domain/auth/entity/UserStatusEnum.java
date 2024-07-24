package com.sparta.studytrek.domain.auth.entity;

public enum UserStatusEnum {

    APPROVER(Authority.APPROVER), // 승인 회원 상태
    GENERAL(Authority.GENERAL);   // 일반 회원 상태

    private final String situation;

    UserStatusEnum(String situation) {
        this.situation = situation;
    }

    public String getUserStatus() {
        return this.situation;
    }

    public static UserStatusEnum getDefault() {
        return GENERAL;
    }

    public static class Authority {
        public static final String APPROVER = "APPROVER";
        public static final String GENERAL = "GENERAL";
    }
}