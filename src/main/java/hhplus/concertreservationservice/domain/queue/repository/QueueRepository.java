package hhplus.concertreservationservice.domain.queue.repository;

import hhplus.concertreservationservice.domain.queue.entity.Queue;
import hhplus.concertreservationservice.domain.queue.entity.QueueStatusType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface QueueRepository {
//
//    Queue save(Queue queue);
//
//    Optional<Queue> findByUserId(Long userId);
//
//    Optional<Queue> findByQueueToken(String token);
//
//    void delete(Queue queue);
//
//    long countWaitingUsersBefore(Long id);
//
//    List<Queue> findByStatusOrderByIdAsc(QueueStatusType queueStatusType, Pageable pageable);
//
//    List<Queue> findExpiredQueues(QueueStatusType queueStatusType, LocalDateTime now);
//
//    void deleteAllInBatch(List<Queue> expiredQueues);


    void addToQueue(String token, double currentTimeMillis);
    Long getQueuePosition(String userId);
    Set<String> getWaitQueueData(int start, int end);
    void setActiveStatus(String userId);
    void removeWaitQueue(String userId);
    void deleteActiveToken(String userId);

}
