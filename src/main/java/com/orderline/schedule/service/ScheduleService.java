package com.orderline.schedule.service;


import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.branch.enums.ReservationTypeEnum;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.model.dto.RepeatScheduleDto;
import com.orderline.schedule.model.dto.ScheduleDto;
import com.orderline.schedule.model.entity.RepeatSchedule;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.model.entity.Schedule;
import com.orderline.klass.model.entity.Klass;
import com.orderline.klass.repository.KlassRepository;
import com.orderline.schedule.repository.RepeatScheduleRepository;
import com.orderline.schedule.repository.ReservationRepository;
import com.orderline.schedule.repository.ScheduleRepository;
import com.orderline.klass.model.domain.FreeChoiceKlassDomain;
import com.orderline.ticket.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Resource(name = "scheduleRepository")
    private ScheduleRepository scheduleRepository;

    @Resource(name = "branchRepository")
    private BranchRepository branchRepository;

    @Resource(name = "repeatScheduleRepository")
    private RepeatScheduleRepository repeatScheduleRepository;

    @Resource(name = "klassRepository")
    private KlassRepository klassRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "reservationRepository")
    private ReservationRepository reservationRepository;

    @Resource(name = "ticketService")
    private TicketService ticketService;

    public ScheduleDto.ResponseScheduleDto get(Long scheduleId) {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
        if (!scheduleOptional.isPresent()) {
            throw new NotFoundException("일정이 존재하지 않습니다.");
        }

        Schedule schedule = scheduleOptional.get();
        return ScheduleDto.ResponseScheduleDto.toDto(schedule);
    }

    public RepeatScheduleDto.ResponseRepeatScheduleDto getRepeatSchedule(Long scheduleId){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if(!scheduleOptional.isPresent()){
            throw  new NotFoundException("일정이 존재하지 않습니다.");
        }

        Schedule schedule = scheduleOptional.get();

        try{
            return RepeatScheduleDto.ResponseRepeatScheduleDto.toDto(schedule.getRepeatSchedule());
        }catch(NullPointerException e){
            throw new NotFoundException("해당 일정 에서는 반복되는 일정이 설정되어 있지 않습니다.");
        }
    }

    public List<ScheduleDto.ResponseScheduleListDto> getListByMainTutorId(Long branchId, Long mainTutorId ,ZonedDateTime startDt, ZonedDateTime endDt){
        List<Schedule> scheduleList = scheduleRepository.findByBranchIdAndMainTutorIdAndStartDtGreaterThanEqualAndStartDtLessThanEqual(branchId, mainTutorId, startDt, endDt);

        List<ScheduleDto.ResponseScheduleKlassListDto> responseScheduleKlassListDtoList = scheduleList.stream().map(ScheduleDto.ResponseScheduleKlassListDto::toDto).collect(Collectors.toList());

        return ScheduleDto.ResponseScheduleListDto.toDtoList(responseScheduleKlassListDtoList);
    }

    public List<ScheduleDto.ResponseScheduleListDto> getList(Long branchId, ZonedDateTime startDt, ZonedDateTime endDt){
        List<Schedule> scheduleList = scheduleRepository.findByBranchIdAndAndStartDtGreaterThanEqualAndStartDtLessThanEqual(branchId, startDt, endDt);

        List<ScheduleDto.ResponseScheduleKlassListDto> responseScheduleKlassListDtoList = scheduleList.stream().map(ScheduleDto.ResponseScheduleKlassListDto::toDto).collect(Collectors.toList());

        return ScheduleDto.ResponseScheduleListDto.toDtoList(responseScheduleKlassListDtoList);
    }

    public List<ScheduleDto.ResponseScheduleDto> getDuplicateCheck(Long branchId, Long startDtUnix, Long endDtUnix){
        ZonedDateTime startDt = TimeFunction.toZonedDateTime(startDtUnix);
        ZonedDateTime endDt = TimeFunction.toZonedDateTime(endDtUnix);

        List<Schedule> scheduleList = scheduleRepository.findByBranchIdAndStartDtBetweenOrEndDtBetweenOrStartDtLessThanEqualAndEndDtGreaterThanEqual(branchId, startDt, endDt, startDt, endDt, startDt, endDt);

        return scheduleList.stream().map(ScheduleDto.ResponseScheduleDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ScheduleDto.ResponseScheduleDto createSchedule(Long branchId, ScheduleDto.RequestScheduleDto requestScheduleDto) {
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()) {
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Klass klass = null;
        if (requestScheduleDto.getKlassId() != null) {
            Optional<Klass> klassOptional = klassRepository.findById(requestScheduleDto.getKlassId());
            if (!klassOptional.isPresent()) {
                throw new NotFoundException("클래스가 존재하지 않습니다.");
            }
            klass = klassOptional.get();
        }

        Optional<User> mainTutorOptional = userRepository.findById(requestScheduleDto.getMainTutorId());
        if (!mainTutorOptional.isPresent()) {
            throw new NotFoundException("메인 강사가 존재하지 않습니다.");
        }
        User mainTutor = mainTutorOptional.get();

        User subTutor = null;
        if (requestScheduleDto.getSubTutorId() != null) {
            Optional<User> subTutorOptional = userRepository.findById(requestScheduleDto.getSubTutorId());
            if (!subTutorOptional.isPresent()) {
                throw new NotFoundException("보조 강사가 존재하지 않습니다.");
            }
            subTutor = subTutorOptional.get();
        }

        if (requestScheduleDto.getRepeatScheduleId() != null) {
            Optional<RepeatSchedule> repeatScheduleOptional = repeatScheduleRepository.findById(requestScheduleDto.getRepeatScheduleId());
            if (!repeatScheduleOptional.isPresent()) {
                throw new NotFoundException("반복일정이 존재하지 않습니다.");
            }
            RepeatSchedule repeatSchedule = repeatScheduleOptional.get();

            List<ScheduleDto.RequestScheduleDto> scheduleDtoList = createScheduleList(repeatSchedule, requestScheduleDto);
            List<Schedule> scheduleList = new ArrayList<>();
            for (ScheduleDto.RequestScheduleDto scheduleDto : scheduleDtoList) {
                Schedule schedule = scheduleDto.toEntity(branch, mainTutor, subTutor, klass, repeatSchedule);
                scheduleList.add(schedule);
            }
            scheduleRepository.saveAll(scheduleList);
            return ScheduleDto.ResponseScheduleDto.toDto(scheduleList.get(0));
        }
        Schedule schedule = requestScheduleDto.toEntity(branch, mainTutor, subTutor, klass, null);
        scheduleRepository.save(schedule);
        return ScheduleDto.ResponseScheduleDto.toDto(schedule);
    }

    public Map<DayOfWeek, List<String>> createDayOfWeekStarEndTimeMap(RepeatSchedule repeatSchedule){
        EnumMap<DayOfWeek, List<String>> dayOfWeekStarEndTimeMap = new EnumMap<>(DayOfWeek.class);
        dayOfWeekStarEndTimeMap.put(DayOfWeek.SUNDAY, repeatSchedule.getSunStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getSunStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.MONDAY, repeatSchedule.getMonStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getMonStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.TUESDAY, repeatSchedule.getTueStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getTueStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.WEDNESDAY, repeatSchedule.getWedStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getWedStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.THURSDAY, repeatSchedule.getThrStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getThrStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.FRIDAY, repeatSchedule.getFriStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getFriStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.SATURDAY, repeatSchedule.getSatStartEndTime() == null ? null : Arrays.asList(repeatSchedule.getSatStartEndTime().split(",")));
        return dayOfWeekStarEndTimeMap;
    }

    public List<ScheduleDto.RequestScheduleDto> createScheduleList(RepeatSchedule repeatSchedule, ScheduleDto.RequestScheduleDto requestScheduleDto) {
        List<ScheduleDto.RequestScheduleDto> scheduleDtoList = new ArrayList<>();
        LocalDate startDate = repeatSchedule.getStartDate();
        LocalDate endDate = repeatSchedule.getEndDate();

        Map<DayOfWeek, List<String>> dayOfWeekStarEndTimeMap = createDayOfWeekStarEndTimeMap(repeatSchedule);

        //사작일 부터 하루씩 + 하면서 반복일정 생성
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            //요일에 해당하는 수업 시간 list를 가져온다.
            List<String> startEndTimeList = dayOfWeekStarEndTimeMap.get(date.getDayOfWeek());
            if (startEndTimeList != null) {
                for (String startEnd : startEndTimeList) {
                    String[] time = startEnd.split("~");
                    //시간 별로 반복 일정 생성
                    scheduleDtoList.add(ScheduleDto.RequestScheduleDto.builder()
                            .startDt(TimeFunction.toUnixTime(date, time[0]))
                            .endDt(TimeFunction.toUnixTime(date, time[1]))
                            .statusType(requestScheduleDto.getStatusType())
                            .privateYn(requestScheduleDto.getPrivateYn())
                            .privateMemo(requestScheduleDto.getPrivateMemo())
                            .publicMemo(requestScheduleDto.getPublicMemo())
                            .reservationEnableYn(requestScheduleDto.getReservationEnableYn())
                            .reservationEnableTime(requestScheduleDto.getReservationEnableTime())
                            .cancelEnableTime(requestScheduleDto.getCancelEnableTime())
                            .minTuteeLackCancelTime(requestScheduleDto.getMinTuteeLackCancelTime())
                            .build());
                }
            }
        }
        return scheduleDtoList;
    }

    public ScheduleDto.RequestOffScheduleWithDateDto createOffScheduleList(RepeatSchedule repeatSchedule, ScheduleDto.RequestOffScheduleDto requestOffScheduleDto) {
        List<LocalDate> offDate = new ArrayList<>();
        List<ScheduleDto.RequestOffScheduleDto> offScheduleDtoList = new ArrayList<>();
        LocalDate startDate = repeatSchedule.getStartDate();
        LocalDate endDate = repeatSchedule.getEndDate();

        Map<DayOfWeek, List<String>> dayOfWeekStarEndTimeMap = createDayOfWeekStarEndTimeMap(repeatSchedule);

        //사작일 부터 하루씩 + 하면서 반복일정 생성
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            //요일에 해당하는 수업 시간 list를 가져온다.
            List<String> startEndTimeList = dayOfWeekStarEndTimeMap.get(date.getDayOfWeek());
            if (startEndTimeList != null) {
                for (String startEnd : startEndTimeList) {
                    String[] time = startEnd.split("~");
                    //시간 별로 반복 일정 생성
                    offScheduleDtoList.add(ScheduleDto.RequestOffScheduleDto.builder()
                            .startDt(TimeFunction.toUnixTime(date, time[0]))
                            .endDt(TimeFunction.toUnixTime(date, time[1]))
                            .privateYn(requestOffScheduleDto.getPrivateYn())
                            .privateMemo(requestOffScheduleDto.getPrivateMemo())
                            .publicMemo(requestOffScheduleDto.getPublicMemo())
                            .build());
                }
                //하루종일 휴무일 경우만 수강권 자동 연장 기간에 포함?
                if(startEndTimeList.size() == 1 && startEndTimeList.get(0).equals("00:00~24:00")){
                    offDate.add(date);
                }
            }
        }
        return ScheduleDto.RequestOffScheduleWithDateDto.builder()
                .offScheduleDtoList(offScheduleDtoList)
                .offDate(offDate)
                .build();
    }

    @Transactional
    public ScheduleDto.ResponseScheduleDto createOffSchedule(Long branchId, ScheduleDto.RequestOffScheduleDto requestOffScheduleDto){

        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Schedule schedule = requestOffScheduleDto.toEntity(branch, null);

        //휴무 일정 생성
        scheduleRepository.save(schedule);

        //휴무가 하루 종일인 경우
        boolean isFullDay = Duration.between(schedule.getStartDt(), schedule.getEndDt()).toHours() == 24;
        if (isFullDay) {
            LocalDate startDate = schedule.getStartDt().toLocalDate();
            ticketService.pauseTicket(branchId, Collections.singletonList(startDate), requestOffScheduleDto.getAutoExtendTicketYn());
        }

        return ScheduleDto.ResponseScheduleDto.toDto(schedule);
    }

    @Transactional
    public ScheduleDto.ResponseScheduleDto createRepeatOffSchedule(Long branchId, ScheduleDto.RequestOffScheduleDto requestOffScheduleDto){
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        List<Schedule> createScheduleList = new ArrayList<>();
        Optional<RepeatSchedule> repeatScheduleOptional = repeatScheduleRepository.findById(requestOffScheduleDto.getRepeatScheduleId());
        if (!repeatScheduleOptional.isPresent()) {
            throw new NotFoundException("반복 일정이 존재하지 않습니다.");
        }
        RepeatSchedule repeatSchedule = repeatScheduleOptional.get();

        //반복 일정만큼 휴무일 추가
        ScheduleDto.RequestOffScheduleWithDateDto offScheduleWithDateDto = createOffScheduleList(repeatSchedule, requestOffScheduleDto);

        for (ScheduleDto.RequestOffScheduleDto offScheduleDto : offScheduleWithDateDto.getOffScheduleDtoList()) {
            Schedule schedule = offScheduleDto.toEntity(branch, repeatSchedule);
            createScheduleList.add(schedule);
        }

        //휴무 일정 생성
        scheduleRepository.saveAll(createScheduleList);

        //휴무가 하루 종일인 휴무 날짜 리스트
        if (!offScheduleWithDateDto.getOffDate().isEmpty()) {
            List<LocalDate> offDateList = offScheduleWithDateDto.getOffDate();
            ticketService.pauseTicket(branchId, offDateList, requestOffScheduleDto.getAutoExtendTicketYn());
        }

        return ScheduleDto.ResponseScheduleDto.toDto(createScheduleList.get(0));
    }

    @Transactional
    public ScheduleDto.ResponseScheduleDto cancel(Long scheduleId) {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
        if(!scheduleOptional.isPresent()){
            throw new NotFoundException("일정이 존재하지 않습니다.");
        }

        Schedule schedule = scheduleOptional.get();

        schedule.cancelSchedule();
        scheduleRepository.save(schedule);

        List <Reservation> reservations = reservationRepository.findByScheduleId(scheduleId);
        reservations.forEach(reservation -> {
            reservation.cancelReservation();
            reservation.updateReservationStatus(ReservationStatusTypeEnum.CANCELLATION);
            //알림 추가
        });
        reservationRepository.saveAll(reservations);

        return ScheduleDto.ResponseScheduleDto.toDto(schedule);
    }

    @Transactional
    public ScheduleDto.ResponseScheduleDto update(Long scheduleId, ScheduleDto.RequestScheduleDto  requestScheduleDto){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
        if (!scheduleOptional.isPresent()){
            throw new NotFoundException("일정이 존재하지 않습니다");
        }
        Schedule schedule = scheduleOptional.get();

        Optional<User> mainTutorOptional = userRepository.findById(requestScheduleDto.getMainTutorId());
        if(!mainTutorOptional.isPresent()){
            throw new NotFoundException("메인 강사가 존재하지 않습니다.");
        }
        User mainTutor = mainTutorOptional.get();

        User subTutor = null;
        if (requestScheduleDto.getSubTutorId() != null){
            Optional<User> subTutorOptional = userRepository.findById(requestScheduleDto.getSubTutorId());
            if(!subTutorOptional.isPresent()){
                throw new NotFoundException("보조 강사가 존재하지 않습니다.");
            }
            subTutor = subTutorOptional.get();
        }

        schedule.updateSchedule(requestScheduleDto, mainTutor, subTutor);
        scheduleRepository.save(schedule);

        return ScheduleDto.ResponseScheduleDto.toDto(schedule);
    }

    // getKlassFreeChoiceSchedule method 가 테스팅 가능 하면 이 method 도 테스팅 가능
    public List<ScheduleDto.ResponseScheduleDto> getFreeChoiceScheduleList(LocalDate startDate, LocalDate endDate, Long branchId){
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (!optionalBranch.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = optionalBranch.get();
        if(!branch.getReservationType().equals(ReservationTypeEnum.FREE_CHOICE)){
            throw new NotFoundException("자유 선택 지점이 아닙니다.");
        }

        // 시간 pre-processing
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1).withSecond(0);
        ZonedDateTime startDt = startDate.atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime endDt = endDate.plusDays(1).atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime reservationEnableDt = now.plusDays(branch.getReservationAvailableDate());
        if(startDt.isBefore(now)) startDt = now;
        if(endDt.isAfter(reservationEnableDt)) endDt = reservationEnableDt;

        // klass, schedule 목록 조회
        List<Klass> klasses = klassRepository.findByBranchId(branchId);
        List<Schedule> branchOtherSchedules = scheduleRepository.findByBranchIdAndStartDtGreaterThanEqualAndStartDtLessThanEqual(branchId, startDt, endDt);
        List<ScheduleDto.ResponseScheduleDto> branchOtherSchedulesDto = branchOtherSchedules.stream().map(ScheduleDto.ResponseScheduleDto::toDto).collect(Collectors.toList());

        List<ScheduleDto.ResponseScheduleDto> freeChoiceSchedules = new ArrayList<>();

        for(Klass klass : klasses){
            FreeChoiceKlassDomain freeChoiceKlassDomain = new FreeChoiceKlassDomain(klass);
            List<ScheduleDto.ResponseScheduleDto> klassFreeChoiceSchedules = freeChoiceKlassDomain.getKlassFreeChoiceSchedule(startDt, endDt);

            if (Boolean.FALSE.equals(klass.getDuplicateReservationYn())) {
                klassFreeChoiceSchedules = removeDuplicateSchedule(klassFreeChoiceSchedules, branchOtherSchedulesDto);
            }
            freeChoiceSchedules.addAll(klassFreeChoiceSchedules);
        }
        return freeChoiceSchedules;
    }

    public List<ScheduleDto.ResponseScheduleDto> getFreeChoiceSchedule(LocalDate startDate, LocalDate endDate, Long branchId, Long klassId){
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (!optionalBranch.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = optionalBranch.get();
        if(!branch.getReservationType().equals(ReservationTypeEnum.FREE_CHOICE)){
            throw new NotFoundException("자유 선택 지점이 아닙니다.");
        }

        Optional<Klass> optionalKlass = klassRepository.findById(klassId);
        if (!optionalKlass.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Klass klass = optionalKlass.get();

        // 시간 pre-processing
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1).withSecond(0);
        ZonedDateTime startDt = startDate.atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime endDt = endDate.plusDays(1).atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime reservationEnableDt = now.plusDays(branch.getReservationAvailableDate());
        if(startDt.isBefore(now)) startDt = now;
        if(endDt.isAfter(reservationEnableDt)) endDt = reservationEnableDt;

        // klass, schedule 목록 조회
        List<Schedule> branchOtherSchedules = scheduleRepository.findByBranchIdAndStartDtGreaterThanEqualAndStartDtLessThanEqual(branchId, startDt, endDt);
        List<ScheduleDto.ResponseScheduleDto> branchOtherSchedulesDto = branchOtherSchedules.stream().map(ScheduleDto.ResponseScheduleDto::toDto).collect(Collectors.toList());

        FreeChoiceKlassDomain freeChoiceKlassDomain = new FreeChoiceKlassDomain(klass);
        List<ScheduleDto.ResponseScheduleDto> klassFreeChoiceSchedules = freeChoiceKlassDomain.getKlassFreeChoiceSchedule(startDt, endDt);

        if (Boolean.FALSE.equals(klass.getDuplicateReservationYn())) {
            klassFreeChoiceSchedules = removeDuplicateSchedule(klassFreeChoiceSchedules, branchOtherSchedulesDto);
        }
        return klassFreeChoiceSchedules;
    }
    public boolean isDuplicateSchedule(ZonedDateTime sourceStartDt, ZonedDateTime sourceEndDt, ZonedDateTime targetStartDt, ZonedDateTime targetEndDt){
        return (!sourceStartDt.isBefore(targetStartDt) && sourceStartDt.isBefore(targetEndDt))
                || (sourceEndDt.isAfter(targetStartDt) && !sourceEndDt.isAfter(targetEndDt))
                || (sourceStartDt.isBefore(targetStartDt) && sourceEndDt.isAfter(targetEndDt));
    }

    public List<ScheduleDto.ResponseScheduleDto> removeDuplicateSchedule(List<ScheduleDto.ResponseScheduleDto> sourceSchedules,
                                                                                List<ScheduleDto.ResponseScheduleDto> targetSchedules){

        List<ScheduleDto.ResponseScheduleDto> schedulesWithoutDuplicated = new ArrayList<>();
        boolean isDuplicate;
        for(ScheduleDto.ResponseScheduleDto targetSchedule : targetSchedules){
            isDuplicate = false;
            for (ScheduleDto.ResponseScheduleDto sourceSchedule : sourceSchedules) {
                if(isDuplicateSchedule(TimeFunction.toZonedDateTime(targetSchedule.getStartDt()), TimeFunction.toZonedDateTime(targetSchedule.getEndDt()),
                        TimeFunction.toZonedDateTime(sourceSchedule.getStartDt()), TimeFunction.toZonedDateTime(sourceSchedule.getEndDt()))){
                    isDuplicate = true;
                }
            }
            if(!isDuplicate) schedulesWithoutDuplicated.add(targetSchedule);
        }
        return schedulesWithoutDuplicated;
    }


}