package com.orderline.profile.service;

import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.common.user.model.entity.ActivationCode;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.ActivationCodeRepository;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.ticket.repository.TicketRepository;
import com.orderline.profile.model.dto.TuteeProfileDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ProfileService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "activationCodeRepository")
    ActivationCodeRepository activationCodeRepository;

    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Transactional
    public TuteeProfileDto.ResponseTuteeProfileDto updateProfile(Long userId, TuteeProfileDto.RequestTuteeProfileDto requestProfileDto){
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()){
            throw new NotFoundException("회원을 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        if (!isNull(requestProfileDto.getPhone()) && !user.getPhone().equals(requestProfileDto.getPhone())){
            Optional<ActivationCode> activationCode = activationCodeRepository.findByPhoneAndActivationYn(requestProfileDto.getPhone(), true);

            if(!activationCode.isPresent()) throw new InternalServerErrorException("인증이 완료되지 않았습니다.");
        }

        user.updateName(requestProfileDto.getName());
        user.updateUserPhone(requestProfileDto.getPhone());
        user.updateBirthDate(requestProfileDto.getBirthDate());
        user.updatePhoneActiveYn(requestProfileDto.getPhoneActiveYn());

        userRepository.save(user);

        return TuteeProfileDto.ResponseTuteeProfileDto.toDto(user);
    }

    public TuteeProfileDto.ResponseUserProfileStatisticsDto getStatistics(Long userId){
        Integer ticketCount = ticketRepository.countByUserId(userId);
        Integer branchCount = branchUserRoleRepository.countByUserId(userId);

        return TuteeProfileDto.ResponseUserProfileStatisticsDto.toDto(ticketCount, branchCount);
    }

    @Transactional
    public void disableCurrentActivationCode(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()){
            throw new NotFoundException("회원을 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        ActivationCode activationCode = activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(user.getPhone());

        activationCode.updateDisableActivationStatus();
        activationCodeRepository.save(activationCode);
    }

    @Transactional
    public void updateCurrentUserPhone(String phone){
        Optional<User> cuurentUserOptional = userRepository.findByPhoneAndPhoneActiveYn(phone, true);
        if (cuurentUserOptional.isPresent()){
            User currentUser = cuurentUserOptional.get();

            currentUser.updatePhoneActiveYn(false);
            userRepository.save(currentUser);
        }
    }

    public Boolean isPhoneChanged(Long userId, String phone){
        Optional<User> userOptional = userRepository.findByIdAndPhoneActiveYn(userId, true);
        if (!userOptional.isPresent()){
            return true;
        }
        User user = userOptional.get();

        return !user.getPhone().equals(phone);
    }
}
