package com.malchun.approval.poc.process.activities;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Random;
import java.util.random.RandomGenerator;

@Component
@Slf4j
@ActivityImpl(taskQueues = "ApprovalProcessQueue")
public class ApproveValidatorActivityImpl implements ApproveValidatorActivity {
    @Override
    public Boolean validateApprove(String approverMail) {
        log.debug("Validating approval for {}", approverMail);
//        try {
//            Thread.sleep(Duration.ofSeconds(5));
//        } catch (InterruptedException e) {
//            return false;
//        }
        log.debug("Validated {}", approverMail);
        return true;//Random.from(RandomGenerator.of("SHA1PRNG")).nextInt(16) < 14;
    }
}
