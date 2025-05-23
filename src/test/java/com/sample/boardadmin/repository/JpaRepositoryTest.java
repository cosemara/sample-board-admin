package com.sample.boardadmin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sample.boardadmin.domain.AdminAccount;
import com.sample.boardadmin.domain.constant.RoleType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DisplayName("JPA 연결 테스트")
@DataJpaTest
class JpaRepositoryTest {

    private final AdminAccountRepository adminAccountRepository;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenAdminAccounts_whenSelecting_thenWorksFine() {
        // given

        // when
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();

        // then
        assertThat(adminAccounts).isNotNull().hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenAdminAccount_whenInserting_thenWorksFine() {
        // given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = AdminAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        // when
        adminAccountRepository.save(adminAccount);

        // then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount+1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() {
        // given
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("uno");
        adminAccount.addRoleType(RoleType.DEVELOPER);
        adminAccount.addRoleTypes(Set.of(RoleType.USER, RoleType.ADMIN));
        adminAccount.removeRoleType(RoleType.ADMIN);

        // when
        AdminAccount updatedAccount = adminAccountRepository.saveAndFlush(adminAccount);

        // then
        assertThat(updatedAccount).hasFieldOrPropertyWithValue("userId", "uno")
                                  .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenAdminAccount_whenDeleting_thenWorksFine() {
        // given
        long previousCount = adminAccountRepository.count();
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("uno");

        // when
        adminAccountRepository.delete(adminAccount);

        // then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount-1);
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }
}