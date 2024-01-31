package com.ptglue.schedule.model.dto;

import io.swagger.annotations.ApiModelProperty;
import com.ptglue.schedule.model.entity.Holiday;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HolidayDto {

    @Getter
    @Builder
    public static class ResponseHolidayDto{

        @ApiModelProperty(value = "휴일 id")
        private Long id;

        @ApiModelProperty(value = "휴일", example = "2023-12-25")
        private LocalDate holidayDate;

        @ApiModelProperty(value = "휴일 명", example = "크리스마스")
        private String holidayName;

        @ApiModelProperty(value = "국가 코드", example = "KR")
        private String countryCode;

        public static ResponseHolidayDto toDto(final Holiday holiday){
            return ResponseHolidayDto.builder()
                    .id(holiday.getId())
                    .holidayDate(holiday.getHolidayDate())
                    .holidayName(holiday.getHolidayName())
                    .countryCode(holiday.getCountryCode())
                    .build();
        }
    }
    
    @Builder
    @Getter
    public static class RequestHolidayDto {
        @ApiModelProperty(value = "휴일", example = "2023-12-25")
        private LocalDate holidayDate;

        @ApiModelProperty(value = "휴일 명", example = "크리스마스")
        private String holidayName;

        @ApiModelProperty(value = "국가 코드", example = "KR")
        private String countryCode;
        public Holiday toEntity(){
            return Holiday.builder()
                    .holidayDate(holidayDate)
                    .holidayName(holidayName)
                    .countryCode(countryCode)
                    .build();
        }
    }
}