/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.AlertEntity;
import com.nwm.api.entities.ChartAlertDateEntity;
import com.nwm.api.entities.ScadaAlertEntity;
import com.nwm.api.entities.TablePreferenceEntity;

public class ScadaAlertService extends DB {
	
	/**
	 * @description get list type device
	 * @author long.pham
	 * @since 2020-11-06
	 * @returns array
	 */
	
	public List getDeviceTypeList(ScadaAlertEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("ScadaAlert.getDeviceTypeList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description get list error type
	 * @author long.pham
	 * @since 2021-03-30
	 * @returns array
	 */
	
	public List getErrorTypeList(ScadaAlertEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("ScadaAlert.getErrorTypeList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description get error level
	 * @author long.pham
	 * @since 2021-01-28
	 * @returns array
	 */
	
	public List getErrorLevelList(ScadaAlertEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("ScadaAlert.getErrorLevelList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	
	/**
	 * @description get list device by id site
	 * @author long.pham
	 * @since 2022-03-04
	 * @param id_site
	 * @return Array of Devices
	 */
	
	public List getListDeviceByIdSite(ScadaAlertEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("ScadaAlert.getListDeviceByIdSite", obj);
			
			return dataList;
				
		} catch (Exception ex) {
			return new ArrayList();
		}
	}
	
	/**
	 * @description get list alert by site
	 * @author long.pham
	 * @since 2020-11-16
	 * @param id_customer, id_site, start_date, end_date
	 */

	public List getListAlertBySiteId(ScadaAlertEntity obj) {
		try {
			// get user preference for table sorting column
			TablePreferenceEntity tablePreference = new TablePreferenceEntity();
			tablePreference.setId_employee(obj.getId_employee());
			tablePreference.setTable("ScadaAlerts");
			tablePreference = (TablePreferenceEntity) queryForObject("TablePreference.getPreference", tablePreference);
			
			if ((obj.getOrder_by() != null) && (obj.getSort_column() != null)) {
				if (tablePreference != null) {
					tablePreference.setOrder_by(obj.getOrder_by());
					tablePreference.setSort_column(obj.getSort_column());
					update("TablePreference.updatePreference", tablePreference);
				} else {
					tablePreference = new TablePreferenceEntity();
					tablePreference.setId_employee(obj.getId_employee());
					tablePreference.setTable("ScadaAlerts");
					tablePreference.setOrder_by(obj.getOrder_by());
					tablePreference.setSort_column(obj.getSort_column());
					insert("TablePreference.insertPreference", tablePreference);
				}
			} else {
				if (tablePreference != null) {
					obj.setOrder_by(tablePreference.getOrder_by());
					obj.setSort_column(tablePreference.getSort_column());
				}
			}	
			List rs = queryForList("ScadaAlert.getListAlertBySiteId", obj);
			if (rs == null) {
				return new ArrayList<>();
			}
			return rs;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * @description get user preference for table sorting column
	 * @author Hung.Bui
	 * @since 2023-02-27
	 * @param id_customer, id_site
	 */
	public TablePreferenceEntity getPreference(ScadaAlertEntity obj) {
		try {
			// get user preference for table sorting column
			TablePreferenceEntity tablePreference = new TablePreferenceEntity();
			tablePreference.setId_employee(obj.getId_employee());
			tablePreference.setTable("ScadaAlerts");
			tablePreference = (TablePreferenceEntity) queryForObject("TablePreference.getPreference", tablePreference);
			
			if (tablePreference == null) {
				return new TablePreferenceEntity();
			}
			return tablePreference;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * @description count total alert by site
	 * @author long.pham
	 * @since 2020-11-16
	 * @param id_customer, id_site, start_date, end_date
	 */

	public int getListBySiteIdTotalCount(ScadaAlertEntity obj) {
		try {
			ScadaAlertEntity totalRecord = (ScadaAlertEntity) queryForObject("ScadaAlert.getListBySiteIdTotalCount", obj);
			return totalRecord.getTotalRecord();
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * @description get list site by id_sites
	 * @author long.pham
	 * @since 2021-02-02
	 * @param arr id_sites
	 */

	public List getDataChart(ScadaAlertEntity obj) {
		try {
			
			// Create list date 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm"); 
			SimpleDateFormat catFormat = new SimpleDateFormat("MM-dd-yyyy");
			
//			SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:00");
			Date startDate = dateFormat.parse(obj.getStart_date() + " AM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			List<ChartAlertDateEntity> categories = new ArrayList<ChartAlertDateEntity> ();
			int minute = 15;
			int forCount = 96 * 3;

			for(int t = 0; t < forCount; t++) {
				cal.setTime(startDate);
				ChartAlertDateEntity headerDate = new ChartAlertDateEntity();
				cal.add(Calendar.MINUTE, t * minute);
				
				headerDate.setTime_format(timeFormat.format(cal.getTime()));
//				String hours = dateFormatHour.format(cal.getTime());
				headerDate.setCategories_time(catFormat.format(cal.getTime()));
				headerDate.setEnergy(0.001);
				headerDate.setPower(0.001);
				headerDate.setIrradiance(0.001);
				categories.add(headerDate);
			}
			
			
			List dataPower = queryForList("ScadaAlert.getDataChart", obj);
			List<ChartAlertDateEntity> dataNewPower = new ArrayList<ChartAlertDateEntity> ();
			if(categories.size() > 0) {
				for (ChartAlertDateEntity item : categories) {
					boolean flag = false;
					ChartAlertDateEntity mapItemObj = new ChartAlertDateEntity();
					if(dataPower != null && dataPower.size() > 0) {
						for( int v = 0; v < dataPower.size(); v++){
							Map<String, Object> itemT = (Map<String, Object>) dataPower.get(v);
							String categoriesTime = item.getTime_format();
							String powerTime = itemT.get("time_format").toString();
					        if (categoriesTime.equals(powerTime)) {
					        	flag = true;
					        	mapItemObj.setCategories_time(itemT.get("categories_time").toString());
					        	Double power = Double.parseDouble(itemT.get("power").toString());
					        	power = (power == -0.0) ? 0 : power;
					        	
					        	mapItemObj.setPower( power );
					        	mapItemObj.setIrradiance(item.getIrradiance());
					        	mapItemObj.setTime_format(itemT.get("time_format").toString());
					        	
					        	
					        	if(itemT.get("power") == null || Double.parseDouble(itemT.get("power").toString()) == 0.001) {
						        	mapItemObj.setEnergy( 0.001 );
					        	} else {
					        		Double energy = (double)Math.round(Double.parseDouble(itemT.get("power").toString()) * 15/60);
						        	mapItemObj.setEnergy( energy > 0 ? energy : 0 );
					        	}
					        	
					        	
					        	break;
					        }
					    }
					}
					
					
					
					if(flag == false) {
						ChartAlertDateEntity mapItem = new ChartAlertDateEntity();
						mapItem.setCategories_time(item.getCategories_time());
						mapItem.setTime_format(item.getTime_format());
						mapItem.setIrradiance(item.getIrradiance());
						mapItem.setEnergy(item.getEnergy());
						mapItem.setPower(item.getPower());
						
						dataNewPower.add(mapItem);
					} else {
						dataNewPower.add(mapItemObj);
					}
				}
			}

			return dataNewPower;
		} catch (Exception ex) {
			return null;
		}
	}

}
