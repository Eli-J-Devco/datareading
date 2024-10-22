/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.util.ArrayList;
import java.util.List;

import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.StateEntity;

public class StateService extends DB {

	/**
	 * @description get list country
	 * @author long.pham
	 * @since 2020-10-30
	 * @returns array
	 */
	
	public List getList(StateEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("State.getList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}

}
