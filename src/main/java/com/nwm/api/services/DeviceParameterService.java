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
import com.nwm.api.entities.DeviceGroupEntity;
import com.nwm.api.entities.DeviceParameterEntity;

public class DeviceParameterService extends DB {
	
	/**
	 * @description Get categorize data list
	 * @author Hung.Bui
	 * @since 2023-11-14
	 * @param  empty
	 * @return array
	 */
	
	public List getListCategorizeData(DeviceParameterEntity obj) {
		try {
			List dataList = queryForList("DeviceParameter.getListCategorizeData", obj);
			return dataList == null ? new ArrayList() : dataList;
		} catch (Exception ex) {
			return new ArrayList();
		}
	}
	
	/**
	 * @description get list parameter by device
	 * @author long.pham
	 * @since 2020-11-06
	 * @param array id_device
	 * @return array
	 */
	
	public List getListByDevice(DeviceParameterEntity obj) {
		List dataList = new ArrayList();
		try {
			List dataDevice = obj.getId_devices();
			if(dataDevice.size() > 0) {
				for(int i =0; i< dataDevice.size(); i++) {
					Map<String, Object> itemDevice = (Map<String, Object>) dataDevice.get(i);
					// Get parameter by device id
					List dataListParamert = queryForList("DeviceParameter.getListParameterByDevice", itemDevice);
					itemDevice.put("parameter", dataListParamert);
					dataList.add(itemDevice);
				}
			}
			
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description Get list device group
	 * @author Hung.Bui
	 * @since 2023-06-26
	 * @return data (status, message, array, total_row)
	 */
	
	public List getListDeviceGroup(DeviceParameterEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("DeviceParameter.getListDeviceGroup", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	public int getTotalRecordDeviceGroup(DeviceParameterEntity obj) {
		try {
			return (int)queryForObject("DeviceParameter.getTotalRecordDeviceGroup", obj);
		} catch (Exception ex) {
			return 0;
		}
	}
	
	
	
	/**
	 * @description Get all site by device group
	 * @author Long.Pham
	 * @since 2024-02-27
	 * @return data (status, message, array, total_row)
	 */
	
	public List getAllSiteByDeviceGroup(DeviceParameterEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("DeviceParameter.getAllSiteByDeviceGroup", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	public int getTotalAllSiteByDeviceGroup(DeviceParameterEntity obj) {
		try {
			return (int)queryForObject("DeviceParameter.getTotalAllSiteByDeviceGroup", obj);
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * @description Leviton get list parameter by device group
	 * @author Long.Pham
	 * @since 2024-05-02
	 * @param id_device_group
	 * @return data (status, message, array, total_row)
	 */
	
	public List LevitonGetParameterByDeviceGroup(DeviceParameterEntity obj) {
		List dataList = new ArrayList();
		try {
			List data = queryForList("DeviceParameter.LevitonGetParameterByDeviceGroup", obj);
			if (data == null)
				return new ArrayList();
			
			for(int i =0; i< data.size(); i++) {
				Map<String, Object> item = (Map<String, Object>) data.get(i);
				// Get parameter by device id
				List dataListParamerter = queryForList("DeviceParameter.getListParameterByField", item);
				item.put("parameters", dataListParamerter);
				dataList.add(item);
			}
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description Get list parameter by device group
	 * @author Hung.Bui
	 * @since 2023-06-26
	 * @param id_device_group
	 * @return data (status, message, array, total_row)
	 */
	
	public List getListParameterByDeviceGroup(DeviceParameterEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("DeviceParameter.getListParameterByDeviceGroup", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description Get list device group and categorize data
	 * @author Hung.Bui
	 * @since 2024-07-26
	 * @param id_device_group, id_categorize_data
	 * @return data (status, message, array, total_row)
	 */
	
	public List getListAllParameterByDeviceGroupAndCategorizeData(DeviceParameterEntity obj) {
		try {
			List dataList = queryForList("DeviceParameter.getListAllParameterByDeviceGroupAndCategorizeData", obj);
			return dataList == null ? new ArrayList() : dataList;
		} catch (Exception ex) {
			return new ArrayList();
		}
	}
	
	
	public int getTotalRecordParameterByDeviceGroup(DeviceParameterEntity obj) {
		try {
			return (int)queryForObject("DeviceParameter.getTotalRecordParameterByDeviceGroup", obj);
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * @description insert device parameter
	 * @author Hung.Bui
	 * @since 2023-06-26
	 */
	public DeviceParameterEntity insertDeviceParameter(DeviceParameterEntity obj) 
	{
		try
	    {
	       Object insertId = insert("DeviceParameter.insertDeviceParameter", obj);
	       if(insertId != null && insertId instanceof Integer) {
	    	   return obj;
	       }else {
	    	   return null;
	       }
	    }
	    catch(Exception ex)
	    {
	        log.error("DeviceParameter.insertDeviceParameter", ex);
	        return null;
	    }	
	}
	
	/**
	 * @description update device parameter
	 * @author Hung.Bui
	 * @since 2023-06-26
	 * @param id
	 */
	public boolean updateDeviceParameter(DeviceParameterEntity obj){
		SqlSession session = this.beginTransaction();
		try{
			DeviceParameterEntity param = session.selectOne("DeviceParameter.getParameterDetail", obj);
			update("DeviceParameter.updateDeviceParameter", obj);
			if (obj.getId_generic_parameter() != null && obj.getId_generic_parameter() != param.getId_generic_parameter()) update("DeviceParameter.resetDuplicatedAssignedGenericParameter", obj);
			if (param.getId_categorize_data() != obj.getId_categorize_data()) update("DeviceParameter.resetParameterFromTemplate", obj);
			session.commit();
			return true;
		}catch (Exception ex) {
			session.rollback();
			log.error("DeviceParameter.updateDeviceParameter", ex);
			return false;
		}finally {
			session.close();
		}
	}
	
	
	
	/**
	 * @description update device parameter
	 * @author Hung.Bui
	 * @since 2023-06-26
	 * @param id
	 */
	public boolean updateDeviceGroup(DeviceGroupEntity obj){
		try{
			return update("DeviceParameter.updateDeviceGroup", obj) > 0;
		}catch (Exception ex) {
			log.error("DeviceParameter.updateDeviceGroup", ex);
			return false;
		}
	}
}
