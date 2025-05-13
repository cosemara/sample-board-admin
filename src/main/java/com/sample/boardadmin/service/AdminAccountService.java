package com.sample.boardadmin.service;

import com.sample.boardadmin.domain.AdminAccount;
import com.sample.boardadmin.domain.constant.RoleType;
import com.sample.boardadmin.dto.AdminAccountDto;
import com.sample.boardadmin.repository.AdminAccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    public Optional<AdminAccountDto> searchUser(String username) {
        return Optional.empty();
    }

    public AdminAccountDto saveUser(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return null;
    }

    public List<AdminAccountDto> users() {
        return List.of();
    }

    public void deleteUser(String username) {

    }
}
