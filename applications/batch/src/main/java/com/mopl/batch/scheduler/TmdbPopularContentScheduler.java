package com.mopl.batch.scheduler;

import com.mopl.batch.job.TmdbPopularContentJob;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TmdbPopularContentScheduler {

    private final TmdbPopularContentJob job;

    @Scheduled(cron = " 0 0 0 * * *")
    public void run() {
        job.run();
    }
}
