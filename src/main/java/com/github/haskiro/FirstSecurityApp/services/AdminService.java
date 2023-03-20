package com.github.haskiro.FirstSecurityApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // метод сработает, только если у пользователя роль админ
    public void doAdminStuff() {
        System.out.println("Only admin here");
    }
}
