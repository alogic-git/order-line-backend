package com.ptglue.setting.model.dto;

import com.ptglue.branch.enums.FunctionTypeEnum;

import java.util.Arrays;

public class ArchiveMockupDto {

    public static ArchiveDto.ResponseArchiveDto getArchiveMockup1() {
        return ArchiveDto.ResponseArchiveDto.builder()
                .category(FunctionTypeEnum.MANAGER)
                .categoryId(1L)
                .name("관리자 이름")
                .detail(Arrays.asList("글루 아이디", "관리자 전화번호"))
                .build();
    }

    public static ArchiveDto.ResponseArchiveDto getArchiveMockup2() {
        return ArchiveDto.ResponseArchiveDto.builder()
                .category(FunctionTypeEnum.KLASS)
                .categoryId(2L)
                .name("클래스이름")
                .detail(Arrays.asList("담당자이름", "정원 N명", "수업시간 20분"))
                .build();
    }

    public static ArchiveDto.ResponseArchiveListDto getArchiveListMockup() {
        return ArchiveDto.ResponseArchiveListDto.builder()
                .results(Arrays.asList(getArchiveMockup1(), getArchiveMockup2()))
                .currentPage(1)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }
}
