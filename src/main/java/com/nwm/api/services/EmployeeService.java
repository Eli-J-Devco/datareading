/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.CustomerEntity;
import com.nwm.api.entities.EmployeeEntity;
import com.nwm.api.entities.EmployeeManageEntity;
import com.nwm.api.entities.EmployeeRoleMapEntity;
import com.nwm.api.entities.EmployeeSiteMapEntity;
import com.nwm.api.entities.UserEntity;

public class EmployeeService extends DB {
	
	/**
	 * @description update account lock
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public boolean updateLockAccount(UserEntity obj) {
		try {
			return update("Employee.updateLockAccount", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateLockAccount", ex);
			return false;
		}
	}
	
	/**
	 * @description update account lock and is_send_email_unblock
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public boolean updateLockAccountAndEmail(UserEntity obj) {
		try {
			return update("Employee.updateLockAccountAndEmail", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateLockAccountAndEmail", ex);
			return false;
		}
	}
	
	/**
	 * @description get list role
	 * @author long.pham
	 * @since 2021-01-05
	 */
	
	public List getAll(EmployeeEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("Employee.getAll", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}

	/**
	 * @description get admin by (email)
	 * @author long.pham
	 * @since 2020-12-22
	 * @param username
	 * @return object
	 */

	public UserEntity getAdminByUserName(String userName) {
		UserEntity user = new UserEntity();
		try {
			user = (UserEntity) queryForObject("Employee.findByAdminLoginId", userName);
			if (user == null)
				return new UserEntity();

			// Get permission by user id
			List dataList = new ArrayList();
			if (user.getId() != 0 && user.getRoles() != null) {
				dataList = queryForList("Employee.getPermissionByUser", user);
			}

			user.setPermissions(dataList);

		} catch (Exception ex) {
			return new UserEntity();
		}
		return user;
	}

	/**
	 * @description get list Employee
	 * @author long.pham
	 * @since 2021-01-06
	 */

	public List getList(EmployeeManageEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("Employee.getList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}

	/**
	 * @description get total record Employee
	 * @author long.pham
	 * @since 2021-01-06
	 */
	public int getTotalRecord(EmployeeManageEntity obj) {
		try {
			return (int) queryForObject("Employee.getListCount", obj);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * @description update role status
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public boolean updateStatus(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateStatus", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateStatus", ex);
			return false;
		}
	}

	/**
	 * @description insert Employee
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public EmployeeManageEntity insertEmployee(EmployeeManageEntity obj) {
		SqlSession session = this.beginTransaction();
		try {
			List roles = obj.getRoles();
			if (roles.size() <= 0) {
				throw new Exception();
			}

			session.insert("Employee.insertEmployee", obj);
			int insertOrderLastId = obj.getId();
			if (insertOrderLastId == 0) return null;
			
			EmployeeRoleMapEntity employeeRoleMaptItem = new EmployeeRoleMapEntity();
			employeeRoleMaptItem.setId_employee(insertOrderLastId);
			employeeRoleMaptItem.setRoleList(roles);
			session.insert("Employee.insertEmployeeRoleMap", employeeRoleMaptItem);
			
			List dataSite = obj.getDataSite();
			if(dataSite != null && dataSite.size() > 0) {
				EmployeeSiteMapEntity employeeSiteMaptItem = new EmployeeSiteMapEntity();
				employeeSiteMaptItem.setId_employee(insertOrderLastId);
				employeeSiteMaptItem.setSiteList(dataSite);
				session.insert("Employee.insertEmployeeSiteMap", employeeSiteMaptItem);
			}

			session.commit();
			return obj;
		} catch (Exception ex) {
			session.rollback();
			log.error("Employee.insertEmployee", ex);
			return null;
		} finally {
			session.close();
		}

	}
	
	
	/**
	 * @description update password employee
	 * @author long.pham
	 * @since 2021-04-06
	 * @param id
	 */
	
	public boolean updatePasswordEmployee(EmployeeManageEntity obj) {
		try {
			return update("Employee.updatePasswordEmployee", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updatePasswordEmployee", ex);
			return false;
		}
	}
	
	/**
	 * @description update Employee
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public boolean updateEmployee(EmployeeManageEntity obj) {
		SqlSession session = this.beginTransaction();
		try {
			List roles = obj.getRoles();
			if (roles.size() <= 0) {
				throw new Exception();
			}

			session.delete("Employee.deleteEmpyeeRoleMap", obj);
			session.update("Employee.updateEmployee", obj);
			session.delete("Employee.deleteSiteEmployeeMap", obj);

			EmployeeRoleMapEntity employeeRoleMaptItem = new EmployeeRoleMapEntity();
			employeeRoleMaptItem.setId_employee(obj.getId());
			employeeRoleMaptItem.setRoleList(roles);
			session.insert("Employee.insertEmployeeRoleMap", employeeRoleMaptItem);
			
			List dataSite = obj.getDataSite();
			if(dataSite != null && dataSite.size() > 0) {
				EmployeeSiteMapEntity employeeSiteMaptItem = new EmployeeSiteMapEntity();
				employeeSiteMaptItem.setId_employee(obj.getId());
				employeeSiteMaptItem.setSiteList(dataSite);
				session.insert("Employee.insertEmployeeSiteMap", employeeSiteMaptItem);
			}

			session.commit();
			return true;
		} catch (Exception ex) {
			session.rollback();
			log.error("Employee.updateEmployee", ex);
			return false;
		} finally {
			session.close();
		}

	}

	/**
	 * @description delete Employee
	 * @author long.pham
	 * @since 2021-01-06
	 * @param id
	 */
	public boolean deleteEmployee(EmployeeManageEntity obj) {
		try {
			return update("Employee.deleteEmployee", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.deleteEmployee", ex);
			return false;
		}
	}

	/**
	 * @description get Employee by email
	 * @author long.pham
	 * @since 2021-01-06
	 * @param email
	 */

	public EmployeeManageEntity getEmployeeByEmail(String email) {
		EmployeeManageEntity employee = new EmployeeManageEntity();
		try {
			employee = (EmployeeManageEntity) queryForObject("Employee.getEmployeeByEmail", email);
			if (employee == null)
				return new EmployeeManageEntity();
		} catch (Exception ex) {
			log.error("Employee.getEmployeeByEmail", ex);
			return new EmployeeManageEntity();
		}
		return employee;
	}

	/**
	 * @description get Employee Exist email and id
	 * @author long.pham
	 * @since 2021-01-06
	 * @param email, id
	 */
	public boolean checkEmployeeEmailExist(EmployeeManageEntity dataE) {
		try {
			return (int) queryForObject("Employee.checkEmployeeEmailExist", dataE) > 0;
		} catch (Exception e) {

		}
		return true;
	}

	/**
	 * @description get Employee by email
	 * @author long.pham
	 * @since 2021-01-06
	 * @param email
	 */

	public EmployeeManageEntity getEmployeeById(int id) {
		EmployeeManageEntity employee = new EmployeeManageEntity();
		try {
			employee = (EmployeeManageEntity) queryForObject("Employee.getEmployeeById", id);
			if (employee == null)
				return new EmployeeManageEntity();
		} catch (Exception ex) {
			log.error("Employee.getEmployeeById", ex);
			return new EmployeeManageEntity();
		}
		return employee;
	}
	
	/**
	 * @description update table columns in Portfolio
	 * @author duy.phan
	 * @since 2022-12-22
	 * @param id
	 */
	public boolean updateTableColumn(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateTableColumn", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateTableColumn", ex);
			return false;
		}
	}
	
	/**
	 * @description get table columns in Portfolio
	 * @author duy.phan
	 * @since 2023-04-18
	 * @param id
	 */
	public EmployeeManageEntity getTableColumn(EmployeeManageEntity obj) {
		EmployeeManageEntity employee = new EmployeeManageEntity();
		try {
			employee = (EmployeeManageEntity) queryForObject("Employee.getTableColumnEmployeeById", obj);
			if (employee == null)
				return new EmployeeManageEntity();
		} catch (Exception ex) {
			log.error("Employee.getTableColumnEmployeeById", ex);
			return new EmployeeManageEntity();
		}
		return employee;
	}
	
	/**
	 * @description update display alert per page
	 * @author duy.phan
	 * @since 2023-07-24
	 * @param id, alert_per_page
	 */
	public boolean updateAlertPerPage(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateAlertPerPage", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateAlertPerPage", ex);
			return false;
		}
	}
	
	/**
	 * @description update display alert per page
	 * @author duy.phan
	 * @since 2023-07-24
	 * @param id, alert_per_page
	 */
	public boolean updateSitePerPage(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateSitePerPage", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateSitePerPage", ex);
			return false;
		}
	}
	
	/**
	 * @description get alert filter
	 * @author duy.phan
	 * @since 2023-04-18
	 * @param id
	 */
	public EmployeeManageEntity getAlertFilter(int id) {
		EmployeeManageEntity employee = new EmployeeManageEntity();
		try {
			employee = (EmployeeManageEntity) queryForObject("Employee.getAlertFilterEmployeeById", id);
			if (employee == null)
				return new EmployeeManageEntity();
		} catch (Exception ex) {
			log.error("Employee.getAlertFilterEmployeeById", ex);
			return new EmployeeManageEntity();
		}
		return employee;
	}
	
	/**
	 * @description update alert filter in Alert
	 * @author duy.phan
	 * @since 2022-12-22
	 * @param id
	 */
	public boolean updateAlertFilter(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateAlertFilter", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateAlertFilter", ex);
			return false;
		}
	}
	
	/**
	 * @description update lock account
	 * @author duy.phan
	 * @since 2022-12-22
	 * @param id
	 */
	public boolean updateLockedAccount(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateLockedAccount", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateLockedAccount", ex);
			return false;
		}
	}
	
	/**
	 * @description update unlock account
	 * @author duy.phan
	 * @since 2022-12-22
	 * @param id
	 */
	public boolean updateUnlockedAccount(EmployeeManageEntity obj) {
		try {
			return update("Employee.updateUnLockedAccount", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateUnLockedAccount", ex);
			return false;
		}
	}
	
	/**
	 * @description update unlock account
	 * @author duy.phan
	 * @since 2022-12-22
	 * @param id
	 */
	public boolean updateSendEmailUnblock(UserEntity obj) {
		try {
			return update("Employee.updateSendEmailUnblock", obj) > 0;
		} catch (Exception ex) {
			log.error("Employee.updateSendEmailUnblock", ex);
			return false;
		}
	}

}
