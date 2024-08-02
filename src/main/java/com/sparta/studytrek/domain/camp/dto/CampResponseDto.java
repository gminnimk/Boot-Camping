//package com.sparta.studytrek.domain.camp.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class CampResponseDto {
//    private Long id;
//    private String name;
//    private String description;
//    private String track;         // 추가된 필드
//    private String environment;   // 추가된 필드
//    private String cost;          // 추가된 필드
//}
package com.sparta.studytrek.domain.camp.dto;

public record CampResponseDto(Long id, String name, String description) {}
