package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Manager;


/**
 * Manager mapper interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface ManagerMapper {
    /**
     * Takes managerId, returns Manager
     *
     * @param managerId
     * @return Manager
     */
    Manager selectByManagerId(String managerId);

    /**
     *
     * @param managerId
     * @param password
     * @return
     */
    Manager managerLogin(String managerId,String password);

    void updateManagerTime(String managerId);

    /**
     * Insert a new manager
     * @param manager
     */
    int insert(Manager manager);

    /**
     * Delete a manager
     * @param managerId
     */
    int deleteByManagerId(String managerId);

    /**
     * Selectively update manager info
     * @param manager
     */
    int updateByPrimaryKeySelective(Manager manager);

}