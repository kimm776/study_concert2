package hhplus.concertreservationservice.infra.persistence.queue;

import hhplus.concertreservationservice.domain.queue.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepositoryImpl implements QueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addToQueue(String token, double currentTimeMillis) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add("waitQueue", token, currentTimeMillis);
    }

    @Override
    public Long getQueuePosition(String userId) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.rank("waitQueue", userId);
    }

    @Override
    public Set<String> getWaitQueueData(int start, int end) {
        return redisTemplate.opsForZSet().range("waitQueue", start, end);
    }

    @Override
    public void setActiveStatus(String userId) {
        redisTemplate.opsForValue().set(userId, userId, 5, TimeUnit.MINUTES);
    }

    @Override
    public void removeWaitQueue(String userId) {
        redisTemplate.opsForZSet().remove("waitQueue", userId);
    }

    @Override
    public void deleteActiveToken(String userId) {
        redisTemplate.delete(userId);
    }

}
