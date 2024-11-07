package hhplus.concertreservationservice.domain.queue.service;

import hhplus.concertreservationservice.domain.concert.entity.ConcertReservation;
import hhplus.concertreservationservice.domain.concert.entity.ConcertSeat;
import hhplus.concertreservationservice.domain.concert.repository.ConcertReservationRepository;
import hhplus.concertreservationservice.domain.concert.repository.ConcertSeatRepository;
import hhplus.concertreservationservice.domain.queue.dto.QueueCommand;
import hhplus.concertreservationservice.domain.queue.dto.QueueCommand.Enqueue;
import hhplus.concertreservationservice.domain.queue.dto.QueueCommand.VerifyQueueForPay;
import hhplus.concertreservationservice.domain.queue.dto.QueueInfo;
import hhplus.concertreservationservice.domain.queue.entity.Queue;
import hhplus.concertreservationservice.domain.queue.entity.QueueStatusType;
import hhplus.concertreservationservice.domain.queue.repository.QueueRepository;
import hhplus.concertreservationservice.domain.user.entity.User;
import hhplus.concertreservationservice.domain.user.repository.UserRepository;
import hhplus.concertreservationservice.global.exception.CustomGlobalException;
import hhplus.concertreservationservice.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueService {

//    @Value("${queue.batch-size}")
//    private int batchSize;
//
//    private final QueueRepository queueRepository;
//    private final ConcertReservationRepository concertReservationRepository;
//    private final ConcertSeatRepository concertSeatRepository;
//
//    public void verifyQueue(QueueCommand.VerifyQueue command){
//        // 요청받은 토큰의 대기열이 존재하지 않으면
//        Queue queue = queueRepository.findByQueueToken(command.queueToken())
//            .orElseThrow(() -> new CustomGlobalException(ErrorCode.QUEUE_NOT_FOUND));
//
//        // 아직 대기열에 WAITING으로 있다면 Exception
//        if (queue.getStatus() == QueueStatusType.WAITING) {
//            throw new CustomGlobalException(ErrorCode.QUEUE_STILL_WAITING);
//        }
//
//    }
//
//    public QueueInfo.Enqueue enqueueOrPoll(Enqueue command) {
//
//        // 값이 있을 때는 있는거 반환
//        Queue queue = queueRepository.findByUserId(command.userId())
//            // 값이 없을 때는 새로운 큐 생성하고 반환
//            .orElseGet(() ->
//                queueRepository.save(Queue.builder()
//                    .userId(command.userId())
//                    .queueToken(UUID.randomUUID().toString())
//                    .status(QueueStatusType.WAITING)
//                    .build())
//            );
//
//        // 폴링시 순번 계산. Pass면 대기자수 0번
//        if (queue.getStatus() == QueueStatusType.PASS) {
//            return QueueInfo.Enqueue.fromEntity(queue, 0L);
//        } else {
//            // 해당 대기열보다 앞선 대기열(WAITING 상태) 수를 카운트
//            long order = queueRepository.countWaitingUsersBefore(queue.getId());
//            return QueueInfo.Enqueue.fromEntity(queue, order + 1L);
//        }
//    }
//
//    public void activateProcess() {
//        List<Queue> waitingQueues = queueRepository.findByStatusOrderByIdAsc(
//            QueueStatusType.WAITING, PageRequest.of(0, batchSize));
//
//        log.info("Passed queue size : {}, time : {}", waitingQueues.size(), LocalDateTime.now());
//
//
//        // 상태를 WAITING에서 PASS로 변경
//        waitingQueues.forEach(Queue::pass);
//
//        // 스케줄링 완료 로깅
//        log.info("Complete scheduled activate queues. time : {}", LocalDateTime.now());
//    }
//
//    public void expireProcess() {
//        //
//        List<Queue> expiredQueues = queueRepository.findExpiredQueues(QueueStatusType.PASS,
//            LocalDateTime.now().minusMinutes(5));
//
//        log.info("expired queue size : {}, time : {}", expiredQueues.size(), LocalDateTime.now());
//
//        // jpa Batch Delete 사용하기!
//        queueRepository.deleteAllInBatch(expiredQueues);
//
//        // 스케줄링 완료 로깅
//        log.info("Complete scheduled expire queues. time : {}", LocalDateTime.now());
//    }
//
//    @Transactional
//    public boolean verifyQueueForPay(VerifyQueueForPay command) {
//        // 요청받은 토큰의 대기열 확인
//        Queue queue = queueRepository.findByQueueToken(command.queueToken())
//            .orElseThrow(() -> new CustomGlobalException(ErrorCode.QUEUE_NOT_FOUND));
//
//        // 아직 대기열에 WAITING으로 있다면 Exception
//        if (queue.getStatus() == QueueStatusType.WAITING) {
//            throw new CustomGlobalException(ErrorCode.QUEUE_STILL_WAITING);
//        }
//
//        // 예약 조회
//        ConcertReservation reservation = concertReservationRepository.findById(
//                command.reservationId())
//            .orElseThrow(() -> new CustomGlobalException(ErrorCode.CONCERT_RESERVATION_NOT_FOUND));
//
//        // 예약정보가 5분 지났으면
//        if(LocalDateTime.now().isAfter(reservation.getCreatedAt().plusMinutes(5))) {
//            // 대기열 삭제
//            queueRepository.delete(queue);
//
//            //좌석점유 해제 (좌석부터 조회)
//            ConcertSeat concertSeat = concertSeatRepository.findById(reservation.getConcertSeatId())
//                .orElseThrow(() -> new CustomGlobalException(ErrorCode.CONCERT_SEAT_NOT_FOUND));
//            concertSeat.cancelSeatByReservation();
//
//            // 예약정보 update 더티체킹 사용
//            reservation.cancelReservation();
//
//            // true를 리턴하면 퍼사드에서 Exception 발생
//            return true;
//        }
//        // false를 리턴하면 대기열 + 예약 검증에 통과.
//        return false;
//
//    }
//
//    @Transactional
//    public void expireToken(String queueToken) {
//
//        // 토큰 조회
//        Queue queue = queueRepository.findByQueueToken(queueToken)
//            .orElseThrow(() -> new CustomGlobalException(ErrorCode.QUEUE_NOT_FOUND));
//
//        // 토큰 삭제
//        queueRepository.delete(queue);
//
//    }


    private final QueueRepository queueRepository;
    private final UserRepository userRepository;

    // 대기열 추가
    public void addToQueue() {
        String token = UUID.randomUUID().toString();
        long currentTimeMillis = System.currentTimeMillis();
        queueRepository.addToQueue(token, currentTimeMillis);
    }

    // 대기 순번 확인
    public Long getQueuePosition(Long uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return queueRepository.getQueuePosition(user.getId().toString());
    }

    // 대기열 active
    public void waitToActiveQueue() {
        // 특정 범위의 데이터 조회
        Set<String> queueData = queueRepository.getWaitQueueData(0, 29);

        if (queueData != null) {
            for (String userId : queueData) {
                // "active" 입력
                queueRepository.setActiveStatus(userId);
                // 저장된 데이터는 waitQueue 제거
                queueRepository.removeWaitQueue(userId);
            }
        }
    }

    // 대기열 제거(결제완료)
    public void expireToken(String userId) {
        queueRepository.deleteActiveToken(userId);
    }

}

