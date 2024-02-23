//package com.orderline.notice.service;
//
//import com.orderline.basic.exception.NotFoundException;
//import com.orderline.notice.enums.NotificationStatusTypeEnum;
//import com.orderline.notice.enums.NotificationTypeEnum;
//import com.orderline.notice.model.dto.NotificationHistoryDto;
//import com.orderline.notice.model.entity.NotificationHistory;
//import com.orderline.notice.repository.NotificationHistoryRepository;
//import com.orderline.user.model.entity.User;
//import com.orderline.user.repository.UserRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.time.LocalDate;
//import java.util.Optional;
//
//@Service
//public class NotificationHistoryService {
//    @Resource(name = "notificationHistoryRepository")
//    NotificationHistoryRepository notificationHistoryRepository;
//
//    @Resource(name = "userRepository")
//    UserRepository userRepository;
//
//    public Page<NotificationHistoryDto.ResponseNotificationDto> getList(
//            Long branchId,
//            NotificationTypeEnum notificationType,
//            String searchWord,
//            LocalDate startDate,
//            LocalDate endDate,
//            Pageable pageable
//    ){
//        Page<NotificationHistory> notificationHistoryPage = notificationHistoryRepository.findNotificationHistoryNativeQuery(branchId, notificationType, searchWord, startDate, endDate, pageable);
//
//        return notificationHistoryPage.map(notificationHistory -> {
//            Integer others = notificationHistoryRepository.countByMessageId(notificationHistory.getMessageId());
//            Integer failCount = notificationHistoryRepository.countByMessageIdAndStatusType(notificationHistory.getMessageId(), NotificationStatusTypeEnum.FAIL);
//
//            return NotificationHistoryDto.ResponseNotificationDto.toDto(notificationHistory, others, failCount);
//        });
//    }
//
////    public Page<NotificationHistoryDto.ResponseMessageUserDto> getMessageUserList(Long notificationId, NotificationStatusTypeEnum notificationStatusType, Pageable pageable) {
////        Page<NotificationHistory> notificationHistoryPage = Page.empty();
////        Optional<NotificationHistory> notificationHistoryOptional = notificationHistoryRepository.findById(notificationId);
////        if (!notificationHistoryOptional.isPresent()){
////            throw new NotFoundException("알림기록이 존재하지 않습니다.");
////        }
////
////        NotificationHistory notificationHistory = notificationHistoryOptional.get();
////
////        if (notificationStatusType == NotificationStatusTypeEnum.ALL){
////            notificationHistoryPage = notificationHistoryRepository.findByMessageId(notificationHistory.getMessageId(), pageable);
////        }else {
////            notificationHistoryPage = notificationHistoryRepository.findByMessageIdAndStatusType(notificationHistory.getMessageId(), notificationStatusType, pageable);
////        }
////
////        return notificationHistoryPage.map(notificationHistoryByMessageId -> {
////            Optional<User> userOptional = userRepository.findById(notificationHistoryByMessageId.getTargetUserId());
////
////            return NotificationHistoryDto.ResponseMessageUserDto.toDto(notificationHistoryByMessageId, userOptional.get());
////        });
////    }
//
////    private Long setMessageId(Long branchId, Long requestUnixTime){
////        StringBuilder stringBuilder = new StringBuilder();
////        SecureRandom rand = new SecureRandom();
////
////        stringBuilder.append(branchId);
////        for (int i = 0; i <= 2; i++) {
////            stringBuilder.append(rand.nextInt(10));
////        }
////        stringBuilder.append(requestUnixTime);
////
////        return Long.parseLong(stringBuilder.toString());
////    }
//}
