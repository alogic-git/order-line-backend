package com.ptglue.schedule.model.entity;

import com.ptglue.schedule.model.dto.HolidayDto;
import com.ptglue.basic.model.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name="holiday")
public class Holiday extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    @Column(name = "holiday_name")
    private String holidayName;

    @Column(name = "country_code")
    private String countryCode;

    public void updateHoliday(HolidayDto.RequestHolidayDto requestHolidayDto) {
        this.holidayDate = requestHolidayDto.getHolidayDate();
        this.holidayName = requestHolidayDto.getHolidayName();
        this.countryCode = requestHolidayDto.getCountryCode();
    }
}