package com.sample.boardadmin.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VisitCounterService {

    private final MeterRegistry meterRegistry;

    private static final List<String> viewEndpoints = List.of(
        "/management/articles",
        "/management/article-comments",
        "/management/user-accounts",
        "/admin/members"
    );

    public long visitCount() {
        long sum;

        try {
            sum = meterRegistry.get("http.server.requests")
                               .timers()
                               .stream()
                               .filter(timer -> viewEndpoints.contains(timer.getId().getTag("uri")))
                               .mapToLong(Timer::count)
                               .sum();
        } catch (MeterNotFoundException e) {
            sum = 0L;
        }

        return sum;
    }

}