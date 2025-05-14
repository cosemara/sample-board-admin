package com.sample.boardadmin.config;

import static org.mockito.BDDMockito.given;

import com.sample.boardadmin.service.VisitCounterService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@TestConfiguration
public class GlobalControllerConfig {

    @MockBean
    private VisitCounterService visitCounterService;

    @BeforeTestMethod
    public void securitySetup() {
        given(visitCounterService.visitCount()).willReturn(0L);
    }

}