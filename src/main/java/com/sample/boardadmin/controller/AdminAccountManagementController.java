package com.sample.boardadmin.controller;

import com.sample.boardadmin.dto.response.AdminAccountResponse;
import com.sample.boardadmin.service.AdminAccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@Controller
public class AdminAccountManagementController {

    private final AdminAccountService adminAccountService;

    @GetMapping("/admin/members")
    public String members() {
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountResponse> getMembers() {
        return adminAccountService.users().stream()
                                  .map(AdminAccountResponse::from)
                                  .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId) {
        adminAccountService.deleteUser(userId);
    }
}
