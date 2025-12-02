package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.Manager;


/**
 * Manager service interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface ManagerService {
    /**
     * Fetch manager details by managerId.
     * @param managerId unique manager identifier
     * @return Manager entity
     */
   Manager selectByManagerId(String managerId);

    /**
     * Validate administrator credentials for login.
     * @param managerId login username
     * @param password raw password
     * @return matched Manager or null if invalid
     */
   Manager managerLogin(String managerId,String password);
}
