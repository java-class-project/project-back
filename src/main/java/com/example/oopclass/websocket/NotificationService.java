package com.example.oopclass.websocket;

import com.example.oopclass.domain.notification.Notification;
import com.example.oopclass.domain.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void notifyMeetingCreator(String creatorId, String applicantInfo) {
        String notificationMessage = "신청자 정보: " + applicantInfo + " - 수락 또는 거절?";
        NotificationMessage message = new NotificationMessage("MEETING_APPLICATION", notificationMessage);
        messagingTemplate.convertAndSendToUser(creatorId, "/topic/notifications", message);
        saveNotification(creatorId, notificationMessage);
    }

    private void saveNotification(String userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setCreatedAt(new Date());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    public void notifyApplicant(String applicantId, boolean accepted) {
        String notificationMessage = accepted ? "모임 신청이 완료되었습니다." : "모임 신청이 거절되었습니다.";
        NotificationMessage message = new NotificationMessage("MEETING_APPLICATION_RESPONSE", notificationMessage);
        messagingTemplate.convertAndSendToUser(applicantId, "/topic/notifications", message);
        saveNotification(applicantId, notificationMessage);

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(applicantId, notificationMessage, 1, TimeUnit.DAYS);
    }


}
