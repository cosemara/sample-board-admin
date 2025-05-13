package com.sample.boardadmin.config;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.sample.boardadmin.domain.constant.RoleType;
import com.sample.boardadmin.dto.AdminAccountDto;
import com.sample.boardadmin.service.AdminAccountService;
import java.util.Optional;
import java.util.Set;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
@TestConfiguration
public class TestSecurityConfig {

    @MockBean
    private AdminAccountService adminAccountService;

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchUser(anyString()))
            .willReturn(Optional.of(createAdminAccountDto()));
        given(adminAccountService.saveUser(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
            .willReturn(createAdminAccountDto());
    }

    private AdminAccountDto createAdminAccountDto() {
        return AdminAccountDto.of(
            "unoTest",
            "pw",
            Set.of(RoleType.USER),
            "uno-test@email.com",
            "uno-test",
            "test memo"
        );
    }

}