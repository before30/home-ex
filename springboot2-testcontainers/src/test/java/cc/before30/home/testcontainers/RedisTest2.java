package cc.before30.home.testcontainers;

import cc.before30.home.testcontainers.domain.Point;
import cc.before30.home.testcontainers.domain.PointRedisRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

public class RedisTest2 extends AbstractRedisTest{

    @Autowired
    private PointRedisRepository pointRedisRepository;

    @After
    public void tearDown() {
        pointRedisRepository.deleteAll();
    }

    @Test
    public void defaultRegisterAndRetrieve2() {
        String id = "test_id";
        LocalDateTime refreshTime = LocalDateTime.now();
        Point point = Point.builder()
                .id(id)
                .amount(1_000L)
                .refreshTime(refreshTime)
                .build();

        pointRedisRepository.save(point);

        Point savePoint = pointRedisRepository.findById(id).get();
        Assert.assertEquals(1000L, (long)savePoint.getAmount());
        Assert.assertEquals(refreshTime, savePoint.getRefreshTime());
    }

    @Test
    public void modify2() {
        //given
        String id = "jojoldu";
        LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
        pointRedisRepository.save(Point.builder()
                .id(id)
                .amount(1000L)
                .refreshTime(refreshTime)
                .build());

        //when
        Point savedPoint = pointRedisRepository.findById(id).get();
        savedPoint.refresh(2000L, LocalDateTime.of(2018,6,1,0,0));
        pointRedisRepository.save(savedPoint);

        //then
        Point refreshPoint = pointRedisRepository.findById(id).get();
        Assert.assertEquals(2000L, (long)refreshPoint.getAmount());
    }
}
