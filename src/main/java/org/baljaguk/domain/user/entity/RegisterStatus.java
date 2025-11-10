package org.baljaguk.domain.user.entity;

public enum RegisterStatus {
    REQUESTED,   // 신청됨 (대기 중)
    ACCEPTED,    // 승인됨
    REJECTED,    // 거절됨
    CANCELED     // 사용자가 스스로 신청 취소
}
