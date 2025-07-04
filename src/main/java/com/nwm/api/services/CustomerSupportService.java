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
import com.nwm.api.entities.CustomerSupportEntity;
import com.nwm.api.entities.SupportFileMapEntity;

public class CustomerSupportService extends DB {

	/**
	 * @description insert customer support
	 * @author long.pham
	 * @since 2021-12-20
	 */
	public CustomerSupportEntity insertCustomerSupport(CustomerSupportEntity obj) {
		SqlSession session = this.beginTransaction();
		try {
			session.insert("CustomerSupport.insertCustomerSupport", obj);
			int insertOrderLastId = obj.getId();
			if (insertOrderLastId > 0) {
				List fileUploads = obj.getFileUploads();
				if (fileUploads.size() > 0) {
					for (int i = 0; i < fileUploads.size(); i++) {
						Map<String, Object> fileObj = (Map<String, Object>) fileUploads.get(i);
						String fileName = fileObj.get("file_name").toString();
						String fileSize = fileObj.get("file_size").toString();
						String fileType = fileObj.get("file_type").toString();
						SupportFileMapEntity MaptItem = this._buildSupportFileMapItem(obj.getId(), fileName, fileSize,
								fileType);
						session.insert("CustomerSupport.insertSupportFile", MaptItem);
					}
				}
			} else {
				return null;
			}
			
			session.commit();
			return obj;
			

		} catch (Exception ex) {
			session.rollback();
			log.error("CustomerSupport.insertCustomerSupport", ex);
			return null;
		} finally {
			session.close();
		}
	}
	

	/**
	 * @description get list icon
	 * @author long.pham
	 * @since 2021-03-31
	 * @param {}
	 */

	public List getList(CustomerSupportEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("CustomerSupport.getList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	public int getTotalRecord(CustomerSupportEntity obj) {
		try {
			return (int)queryForObject("CustomerSupport.getTotalRecord", obj);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * build order product item
	 * 
	 * @param productItem
	 * @param productId
	 * @param insertOrderLastId
	 * @return
	 */
	private SupportFileMapEntity _buildSupportFileMapItem(int idSupport, String fileName, String fileSize,
			String fileType) {
		try {
			SupportFileMapEntity item = new SupportFileMapEntity();
			item.setId_support(idSupport);
			item.setFile_name(fileName);
			item.setFile_size(fileSize);
			item.setFile_type(fileType);
			return item;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @description update icon status
	 * @author long.pham
	 * @since 2021-05-17
	 * @param id
	 */
	public boolean updateStatus(CustomerSupportEntity obj){
		try{
			return update("CustomerSupport.updateStatus", obj)>0;
		}catch (Exception ex) {
			log.error("CustomerSupport.updateStatus", ex);
			return false;
		}
	}

}
