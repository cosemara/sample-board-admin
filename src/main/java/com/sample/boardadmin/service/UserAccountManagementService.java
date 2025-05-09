package com.sample.boardadmin.service;

import com.sample.boardadmin.dto.UserAccountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    public List<UserAccountDto> getUserAccounts() {
        return List.of();
    }

    public UserAccountDto getUserAccount(String userId) {
        return null;
    }

    public void deleteUserAccount(String userId) {

    }

}
