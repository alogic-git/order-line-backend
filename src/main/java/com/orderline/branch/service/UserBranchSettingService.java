package com.orderline.branch.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.model.dto.UserBranchSettingDto;
import com.orderline.branch.model.entity.UserBranchSetting;
import com.orderline.branch.repository.UserBranchSettingRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserBranchSettingService {
    @Resource(name = "userBranchSettingRepository")
    UserBranchSettingRepository userBranchSettingRepository;

    public UserBranchSettingDto.ResponseAlarmSettingDto getBranchSetting(Long userId, Long branchId){

        UserBranchSetting userBranchSetting = userBranchSettingRepository.findByUserIdAndBranchId(userId, branchId)
                .orElseThrow(() -> new NotFoundException("해당 지점에 설정 값이 존재하지 않습니다."));
        return UserBranchSettingDto.ResponseAlarmSettingDto.toDto(userBranchSetting);
    }

    public UserBranchSettingDto.ResponseAlarmSettingDto updateTuteeBranchSetting(Long branchId, Long userId, UserBranchSettingDto.RequestTuteeAlarmSettingDto requestTuteeAlarmSettingDto){
        UserBranchSetting userBranchSetting = userBranchSettingRepository.findByUserIdAndBranchId(userId, branchId)
                .orElseThrow(() -> new NotFoundException("설정 조회에 문제가 발생 했습니다. 새로고침 후 다시 시도 해주세요"));

        userBranchSetting.updateAlarmChangeScheduleToMeTuteeRoleYn(requestTuteeAlarmSettingDto.getAlarmChangeScheduleToMeTuteeRoleYn());
        userBranchSetting.updateAlarmNoticeScheduleStartTime(requestTuteeAlarmSettingDto.getAlarmNoticeScheduleStartTime());
        userBranchSetting.updateAlarmNoticeTicketEndDate(requestTuteeAlarmSettingDto.getAlarmNoticeTicketEndDate());

        userBranchSettingRepository.save(userBranchSetting);

        return UserBranchSettingDto.ResponseAlarmSettingDto.toDto(userBranchSetting);
    }

    public UserBranchSettingDto.ResponseAlarmSettingDto updateStaffBranchSetting(Long branchId, Long userId, UserBranchSettingDto.RequestStaffAlarmSettingDto requestStaffAlarmSettingDto){
        UserBranchSetting userBranchSetting = userBranchSettingRepository.findByUserIdAndBranchId(userId, branchId)
                .orElseThrow(() -> new NotFoundException("설정 조회에 문제가 발생 했습니다. 새로고침 후 다시 시도 해주세요"));

        userBranchSetting.updateAlarmChangeScheduleToMeStaffRoleYn(requestStaffAlarmSettingDto.getAlarmChangeScheduleToMeStaffRoleYn());
        userBranchSetting.updateAlarmNoticeScheduleStartTime(requestStaffAlarmSettingDto.getAlarmNoticeScheduleStartTime());
        userBranchSetting.updateAlarmNoticeTicketEndDate(requestStaffAlarmSettingDto.getAlarmNoticeTicketEndDate());

        userBranchSettingRepository.save(userBranchSetting);

        return UserBranchSettingDto.ResponseAlarmSettingDto.toDto(userBranchSetting);
    }
}
