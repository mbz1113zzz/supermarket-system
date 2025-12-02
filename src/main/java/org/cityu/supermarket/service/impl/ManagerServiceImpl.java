package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.mapper.ManagerMapper;
import org.cityu.supermarket.entity.Manager;
import org.cityu.supermarket.service.ManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


/**
 * Manager service implementation class.
 * 
 * @version 0.0.1
 * @date 2025-11-29
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    ManagerMapper managerMapper;
    
    @Resource
    PasswordEncoder passwordEncoder;
    
    @Override
    public Manager selectByManagerId(String managerId) {
       return managerMapper.selectByManagerId(managerId);
    }

    @Override
    public Manager managerLogin(String managerId, String password) {
        // 1. fetch admin record
        Manager manager = managerMapper.selectByManagerId(managerId);
        
        if (manager == null) {
            return null;
        }
        
        // 2. validate password with BCrypt
        // handles internally
        boolean passwordMatch = passwordEncoder.matches(password, manager.getPassword());
        
        if (passwordMatch) {
            // 3. success, refresh login time
            managerMapper.updateManagerTime(manager.getManagerId());
            return manager;
        }
        
        // password mismatch, return null
        return null;
    }
    /*
     * note: mybatis date mapping issue
     * switched to TIMESTAMP to keep precision
     */
}
