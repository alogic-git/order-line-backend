package com.orderline.product.model.dto;

import com.orderline.product.model.entity.Product;
import com.orderline.product.model.entity.ProductTag;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.branch.model.dto.BranchTagDto;
import com.orderline.branch.model.entity.Branch;
import com.orderline.klass.model.dto.KlassDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    @Getter
    @Builder
    public static class ResponseProductDto{

        @ApiModelProperty(value = "수강권 ID")
        private Long productId;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "수강권 이름")
        private String productName;

        @ApiModelProperty(value = "기본 예약 횟수")
        private Integer reservationCount;

        @ApiModelProperty(value = "기본 유효 기간")
        private Integer period;

        @ApiModelProperty(value = "기본 가격")
        private Integer price;

        @ApiModelProperty(value = "회원이 볼 수 없는 메모")
        private String privateMemo;

        @ApiModelProperty(value = "일시정지 가능 여부")
        private Boolean stopEnableYn;

        @ApiModelProperty(value = "일시정지 가능 일수")
        private Integer stopEnableDate;

        @ApiModelProperty(value = "일시정지 총 가능 일수")
        private Integer stopEnableTotalDate;

        @ApiModelProperty(value = "일시정지 가능 횟수")
        private Integer stopEnableCount;

        @ApiModelProperty(value = "일일 다중 수강 횟수")
        private Integer dailyDuplicateReservationNum;

        @ApiModelProperty(value = "일일 예약 가능 횟수")
        private Integer dailyReservationEnableNum;

        @ApiModelProperty(value = "일일 취소 가능 횟수")
        private Integer dailyCancelEnableNum;

        @ApiModelProperty(value = "주간 예약 가능 횟수")
        private Integer weeklyReservationEnableNum;

        @ApiModelProperty(value = "주간 취소 가능 횟수")
        private Integer weeklyCancelEnableNum;

        @ApiModelProperty(value = "기간 종료시 자동 종료")
        private Boolean autoFinishYn;

        @ApiModelProperty(value = "보관 여부")
        private Boolean archiveYn;

        @ApiModelProperty(value = "수강권 태그")
        private List<BranchTagDto.ResponseBranchTagDto> tagList;

        @ApiModelProperty(value = "클래스 리스트")
        private List<KlassDto.ResponseKlassDto> klassList;

        public void updateTagList(final List<BranchTagDto.ResponseBranchTagDto> productTagList){
            this.tagList = productTagList;
        }
        public static ResponseProductDto toDto(final Product product){
            return ResponseProductDto.builder()
                    .productId(product.getId())
                    .branchId(product.getBranch().getId())
                    .productName(product.getProductName())
                    .reservationCount(product.getReservationCount())
                    .period(product.getPeriod())
                    .price(product.getPrice())
                    .privateMemo(product.getPrivateMemo())
                    .stopEnableYn(product.getStopEnableYn())
                    .stopEnableDate(product.getStopEnableDate())
                    .stopEnableTotalDate(product.getStopEnableTotalDate())
                    .stopEnableCount(product.getStopEnableCount())
                    .dailyDuplicateReservationNum(product.getDailyDuplicateReservationNum())
                    .dailyReservationEnableNum(product.getDailyReservationEnableNum())
                    .dailyCancelEnableNum(product.getDailyCancelEnableNum())
                    .weeklyReservationEnableNum(product.getWeeklyReservationEnableNum())
                    .weeklyCancelEnableNum(product.getWeeklyCancelEnableNum())
                    .autoFinishYn(product.getAutoFinishYn())
                    .archiveYn(product.getArchiveYn())
                    .build();
        }

        public static ResponseProductDto toDto(final Product product, final List<ProductTag> tagList){
            return ResponseProductDto.builder()
                    .productId(product.getId())
                    .branchId(product.getBranch().getId())
                    .productName(product.getProductName())
                    .reservationCount(product.getReservationCount())
                    .period(product.getPeriod())
                    .price(product.getPrice())
                    .privateMemo(product.getPrivateMemo())
                    .stopEnableYn(product.getStopEnableYn())
                    .stopEnableDate(product.getStopEnableDate())
                    .stopEnableTotalDate(product.getStopEnableTotalDate())
                    .stopEnableCount(product.getStopEnableCount())
                    .dailyDuplicateReservationNum(product.getDailyDuplicateReservationNum())
                    .dailyReservationEnableNum(product.getDailyReservationEnableNum())
                    .dailyCancelEnableNum(product.getDailyCancelEnableNum())
                    .weeklyReservationEnableNum(product.getWeeklyReservationEnableNum())
                    .weeklyCancelEnableNum(product.getWeeklyCancelEnableNum())
                    .autoFinishYn(product.getAutoFinishYn())
                    .archiveYn(product.getArchiveYn())
                    .tagList(tagList.stream().map(BranchTagDto.ResponseBranchTagDto::toDto).collect(Collectors.toList()))
                    .build();
        }

        public static ResponseProductDto toDto(final Product product, final List<BranchTagDto.ResponseBranchTagDto> tagList, final List<KlassDto.ResponseKlassDto> klassList){
            return ResponseProductDto.builder()
                    .productId(product.getId())
                    .branchId(product.getBranch().getId())
                    .productName(product.getProductName())
                    .reservationCount(product.getReservationCount())
                    .period(product.getPeriod())
                    .price(product.getPrice())
                    .privateMemo(product.getPrivateMemo())
                    .stopEnableYn(product.getStopEnableYn())
                    .stopEnableDate(product.getStopEnableDate())
                    .stopEnableTotalDate(product.getStopEnableTotalDate())
                    .stopEnableCount(product.getStopEnableCount())
                    .dailyDuplicateReservationNum(product.getDailyDuplicateReservationNum())
                    .dailyReservationEnableNum(product.getDailyReservationEnableNum())
                    .dailyCancelEnableNum(product.getDailyCancelEnableNum())
                    .weeklyReservationEnableNum(product.getWeeklyReservationEnableNum())
                    .weeklyCancelEnableNum(product.getWeeklyCancelEnableNum())
                    .autoFinishYn(product.getAutoFinishYn())
                    .archiveYn(product.getArchiveYn())
                    .tagList(tagList)
                    .klassList(klassList)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseProductListDto{

        @ApiModelProperty(value = "지점 리스트")
        private List<ResponseProductDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseProductListDto build(Page<ResponseProductDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseProductListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestProductDto {

        @NotBlank(message = "수강권 이름을 입력해주세요.")
        @ApiModelProperty(value = "수강권 이름(필수)", example = "고배배 수강권")
        private String productName;

        @NotNull(message = "기본 예약 횟수를 입력해주세요.")
        @PositiveOrZero(message = "기본 예약 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "기본 예약 횟수(필수)", example = "10")
        private Integer reservationCount;

        @NotNull(message = "기본 유효 기간을 입력해주세요.")
        @PositiveOrZero(message = "기본 유효 기간은 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "기본 유효 기간(필수)", example = "30")
        private Integer period;

        @NotNull(message = "기본 가격을 입력해주세요.")
        @ApiModelProperty(value = "기본 가격(필수)", example = "100000")
        private Integer price;

        @Size(max = 200, message = "메모는 최대 200자까지 입력 가능합니다.")
        @ApiModelProperty(value = "비공개 메모")
        private String privateMemo;

        @ApiModelProperty(value = "공개 메모")
        private String publicMemo;

        @ApiModelProperty(value = "일시정지 가능 여부", example = "false")
        private Boolean stopEnableYn;

        @PositiveOrZero(message = "일시정지 가능 일수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일시정지 가능 일수")
        private Integer stopEnableDate;

        @PositiveOrZero(message = "일시정지 총 가능 일수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일시정지 총 가능 일수")
        private Integer stopEnableTotalDate;

        @PositiveOrZero(message = "일시정지 가능 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일시정지 가능 횟수")
        private Integer stopEnableCount;

        @PositiveOrZero(message = "일일 다중 수강 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일일 다중 수강 횟수")
        private Integer dailyDuplicateReservationNum;

        @PositiveOrZero(message = "일일 예약 가능 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일일 예약 가능 횟수")
        private Integer dailyReservationEnableNum;

        @PositiveOrZero(message = "일일 취소 가능 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "일일 취소 가능 횟수")
        private Integer dailyCancelEnableNum;

        @PositiveOrZero(message = "주간 예약 가능 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "주간 예약 가능 횟수")
        private Integer weeklyReservationEnableNum;

        @PositiveOrZero(message = "주간 취소 가능 횟수는 0이상의 숫자로 입력해주세요.")
        @ApiModelProperty(value = "주간 취소 가능 횟수")
        private Integer weeklyCancelEnableNum;

        @ApiModelProperty(value = "기간 종료시 자동 종료", example = "true")
        private Boolean autoFinishYn;

        @ApiModelProperty(value = "보관 여부", example = "false")
        private Boolean archiveYn;

        @Valid
        @ApiModelProperty(value = "태그 리스트")
        private List<BranchTagDto.RequestBranchTagDto> tagList;

        public Product toEntity(final Branch branch){
            return Product.builder()
                    .branch(branch)
                    .productName(productName)
                    .reservationCount(reservationCount)
                    .period(period)
                    .price(price)
                    .privateMemo(privateMemo)
                    .stopEnableYn(stopEnableYn)
                    .stopEnableDate(stopEnableDate)
                    .stopEnableTotalDate(stopEnableTotalDate)
                    .stopEnableCount(stopEnableCount)
                    .dailyDuplicateReservationNum(dailyDuplicateReservationNum)
                    .dailyReservationEnableNum(dailyReservationEnableNum)
                    .dailyCancelEnableNum(dailyCancelEnableNum)
                    .weeklyReservationEnableNum(weeklyReservationEnableNum)
                    .weeklyCancelEnableNum(weeklyCancelEnableNum)
                    .autoFinishYn(autoFinishYn)
                    .archiveYn(archiveYn)
                    .build();
        }
    }

}
