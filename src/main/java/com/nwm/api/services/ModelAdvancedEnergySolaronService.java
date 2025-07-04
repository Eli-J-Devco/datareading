/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;


import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.AlertEntity;
import com.nwm.api.entities.ModelAdvancedEnergySolaronEntity;
import com.nwm.api.utils.Lib;
import com.nwm.api.utils.LibErrorCode;

public class ModelAdvancedEnergySolaronService extends DB {

	/**
	 * @description set data ModelAdvancedEnergySolaron
	 * @author long.pham
	 * @since 2022-12-20
	 * @param data
	 */
	
	public ModelAdvancedEnergySolaronEntity setModelAdvancedEnergySolaron(String line) {
		try {
			List<String> words = Lists.newArrayList(Splitter.on(',').split(line));
			if (words.size() > 0) {
				ModelAdvancedEnergySolaronEntity dataModelAdvancedEnergySolaron = new ModelAdvancedEnergySolaronEntity();
				Double power = Double.parseDouble(!Lib.isBlank(words.get(13)) ? words.get(13) : "0.001");
				Double energy = Double.parseDouble(!Lib.isBlank(words.get(5)) ? words.get(5) : "0.001");
		
				dataModelAdvancedEnergySolaron.setTime(words.get(0).replace("'", ""));
				dataModelAdvancedEnergySolaron.setError(Integer.parseInt(!Lib.isBlank(words.get(1)) ? words.get(1) : "0"));
				dataModelAdvancedEnergySolaron.setLow_alarm(Integer.parseInt(!Lib.isBlank(words.get(2)) ? words.get(2) : "0"));
				dataModelAdvancedEnergySolaron.setHigh_alarm(Integer.parseInt(!Lib.isBlank(words.get(3)) ? words.get(3) : "0"));
				
				dataModelAdvancedEnergySolaron.setToday_kwh(Double.parseDouble(!Lib.isBlank(words.get(4)) ? words.get(4) : "0.001"));
				dataModelAdvancedEnergySolaron.setYtd_kwh_total(energy);
				dataModelAdvancedEnergySolaron.setLife_kwh_total(Double.parseDouble(!Lib.isBlank(words.get(6)) ? words.get(6) : "0.001"));
				dataModelAdvancedEnergySolaron.setYtd_kwh(Double.parseDouble(!Lib.isBlank(words.get(7)) ? words.get(7) : "0.001"));
				dataModelAdvancedEnergySolaron.setLife_kwh(Double.parseDouble(!Lib.isBlank(words.get(8)) ? words.get(8) : "0.001"));
				dataModelAdvancedEnergySolaron.setLast_15min_kwh(Double.parseDouble(!Lib.isBlank(words.get(9)) ? words.get(9) : "0.001"));
				dataModelAdvancedEnergySolaron.setTimestamp_15minutes(Double.parseDouble(!Lib.isBlank(words.get(10)) ? words.get(10) : "0.001"));
				dataModelAdvancedEnergySolaron.setLast_restart(Double.parseDouble(!Lib.isBlank(words.get(11)) ? words.get(11) : "0.001"));
				
				dataModelAdvancedEnergySolaron.setUptime(Double.parseDouble(!Lib.isBlank(words.get(12)) ? words.get(12) : "0.001"));
				dataModelAdvancedEnergySolaron.setAc_power(power);
				dataModelAdvancedEnergySolaron.setAc_frequency(Double.parseDouble(!Lib.isBlank(words.get(14)) ? words.get(14) : "0.001"));
				dataModelAdvancedEnergySolaron.setPv_voltage(Double.parseDouble(!Lib.isBlank(words.get(15)) ? words.get(15) : "0.001"));
				dataModelAdvancedEnergySolaron.setPv_current(Double.parseDouble(!Lib.isBlank(words.get(16)) ? words.get(16) : "0.001"));
				dataModelAdvancedEnergySolaron.setCommon_mode(Double.parseDouble(!Lib.isBlank(words.get(17)) ? words.get(17) : "0.001"));
				dataModelAdvancedEnergySolaron.setAmbient_temperature(Double.parseDouble(!Lib.isBlank(words.get(18)) ? words.get(18) : "0.001"));
				dataModelAdvancedEnergySolaron.setCoolant_temperature(Double.parseDouble(!Lib.isBlank(words.get(19)) ? words.get(19) : "0.001"));
				dataModelAdvancedEnergySolaron.setReactor_temperature(Double.parseDouble(!Lib.isBlank(words.get(20)) ? words.get(20) : "0.001"));
				dataModelAdvancedEnergySolaron.setCabinet_temperature(Double.parseDouble(!Lib.isBlank(words.get(21)) ? words.get(21) : "0.001"));
				
				dataModelAdvancedEnergySolaron.setBus_voltage(Double.parseDouble(!Lib.isBlank(words.get(22)) ? words.get(22) : "0.001"));
				dataModelAdvancedEnergySolaron.setGround_current(Double.parseDouble(!Lib.isBlank(words.get(23)) ? words.get(23) : "0.001"));
				dataModelAdvancedEnergySolaron.setReactive_power(Double.parseDouble(!Lib.isBlank(words.get(24)) ? words.get(24) : "0.001"));
				dataModelAdvancedEnergySolaron.setActive_faults1(Double.parseDouble(!Lib.isBlank(words.get(25)) ? words.get(25) : "0.001"));
				dataModelAdvancedEnergySolaron.setActive_faults2(Double.parseDouble(!Lib.isBlank(words.get(26)) ? words.get(26) : "0.001"));
				dataModelAdvancedEnergySolaron.setActive_faults3(Double.parseDouble(!Lib.isBlank(words.get(27)) ? words.get(27) : "0.001"));
				dataModelAdvancedEnergySolaron.setStatus(Double.parseDouble(!Lib.isBlank(words.get(28)) ? words.get(28) : "0.001"));
				dataModelAdvancedEnergySolaron.setWarnings1(Double.parseDouble(!Lib.isBlank(words.get(29)) ? words.get(29) : "0.001"));
				dataModelAdvancedEnergySolaron.setWarnings2_reserved(Double.parseDouble(!Lib.isBlank(words.get(30)) ? words.get(30) : "0.001"));
				dataModelAdvancedEnergySolaron.setWarnings3_reserved(Double.parseDouble(!Lib.isBlank(words.get(31)) ? words.get(31) : "0.001"));
				
				dataModelAdvancedEnergySolaron.setLimits(Double.parseDouble(!Lib.isBlank(words.get(32)) ? words.get(32) : "0.001"));
				dataModelAdvancedEnergySolaron.setYear(Double.parseDouble(!Lib.isBlank(words.get(33)) ? words.get(33) : "0.001"));
				dataModelAdvancedEnergySolaron.setMonth(Double.parseDouble(!Lib.isBlank(words.get(34)) ? words.get(34) : "0.001"));
				dataModelAdvancedEnergySolaron.setDay(Double.parseDouble(!Lib.isBlank(words.get(35)) ? words.get(35) : "0.001"));
				dataModelAdvancedEnergySolaron.setHour(Double.parseDouble(!Lib.isBlank(words.get(36)) ? words.get(36) : "0.001"));
				dataModelAdvancedEnergySolaron.setMinutes(Double.parseDouble(!Lib.isBlank(words.get(37)) ? words.get(37) : "0.001"));
				dataModelAdvancedEnergySolaron.setSeconds(Double.parseDouble(!Lib.isBlank(words.get(38)) ? words.get(38) : "0.001"));
				dataModelAdvancedEnergySolaron.setCurrent_time(Double.parseDouble(!Lib.isBlank(words.get(39)) ? words.get(39) : "0.001"));
				
				// set custom field nvmActivePower and nvmActiveEnergy
				dataModelAdvancedEnergySolaron.setNvmActivePower(power);
				dataModelAdvancedEnergySolaron.setNvmActiveEnergy(energy);
				return dataModelAdvancedEnergySolaron;
				
			} else {
				return new ModelAdvancedEnergySolaronEntity();
			}
			
			
		} catch (Exception ex) {
			log.error("insert", ex);
			return new ModelAdvancedEnergySolaronEntity();
		}
	}
	
	/**
	 * @description insert data from datalogger to model_advanced_energy_solaron
	 * @author long.pham
	 * @since 2020-12-11
	 * @param data from datalogger
	 */

	public boolean insertModelAdvancedEnergySolaron(ModelAdvancedEnergySolaronEntity obj) {
		try {
			if(obj.getOffset_data_old() !=0) {
				Double energy = obj.getNvmActiveEnergy();
				energy = energy + obj.getOffset_data_old();
				obj.setNvmActiveEnergy(energy);
				obj.setYtd_kwh_total(energy);
			}
			
			ModelAdvancedEnergySolaronEntity dataObj = (ModelAdvancedEnergySolaronEntity) queryForObject("ModelAdvancedEnergySolaron.getLastRow", obj);
			// filter data 
			if(dataObj != null && ( obj.getError() > 0 || obj.getNvmActiveEnergy() < dataObj.getNvmActiveEnergy() || obj.getNvmActiveEnergy() == 0.001 || obj.getNvmActiveEnergy() < 0) ) {
				obj.setNvmActiveEnergy(dataObj.getNvmActiveEnergy());
				obj.setYtd_kwh_total(dataObj.getNvmActiveEnergy());
			}
						
			 double measuredProduction = 0;
			 if(dataObj != null && dataObj.getId_device() > 0 && dataObj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() != 0.001 ) {
				 measuredProduction = obj.getNvmActiveEnergy() - dataObj.getNvmActiveEnergy();				 
			 }
			 

			 obj.setMeasuredProduction(measuredProduction);
			 
			Object insertId = insert("ModelAdvancedEnergySolaron.insertModelAdvancedEnergySolaron", obj);
			if (insertId == null) {
				return false;
			}
			ZoneId zoneIdLosAngeles = ZoneId.of("America/Los_Angeles"); // "America/Los_Angeles"
	        ZonedDateTime zdtNowLosAngeles = ZonedDateTime.now(zoneIdLosAngeles);
	        int hours = zdtNowLosAngeles.getHour();
	        
	        if (hours >= 9 && hours <= 17 && dataObj.getEnable_alert() >= 1) {
	        	checkTriggerAlertModelAdvancedEnergySolaron(obj);
	        }
			
			return true;
		} catch (Exception ex) {
			log.error("insert", ex);
			return false;
		}

	}
	
	
	/**
	 * @description get last row "data table name" by device
	 * @author long.pham
	 * @since 2021-05-18
	 * @param datatablename
	 */

	public ModelAdvancedEnergySolaronEntity checkAlertWriteCode(ModelAdvancedEnergySolaronEntity obj) {
		ModelAdvancedEnergySolaronEntity rowItem = new ModelAdvancedEnergySolaronEntity();
		try {
			
			List dataList = queryForList("ModelAdvancedEnergySolaron.checkAlertWriteCode", obj);
			if(dataList.size() > 0) {
				int totalFault1 = 0, totalFault2 = 0, totalFault3 = 0, totalLimits = 0, totalWarning = 0, totalStatus = 0;
				for(int i =0; i < dataList.size(); i ++) {
					Map<String, Object> item = (Map<String, Object>) dataList.get(i);
					double active_faults1 = (double) item.get("active_faults1");
					if(Double.compare(obj.getActive_faults1(), active_faults1) == 0 && obj.getActive_faults1() > 0 && active_faults1 > 0) { 
						totalFault1++;
					}
					
					double active_faults2 = (double) item.get("active_faults2");
					if(Double.compare(obj.getActive_faults2(), active_faults2) == 0 && obj.getActive_faults2() > 0 && active_faults2 > 0) { 
						totalFault2++;
					}
					
					double active_faults3 = (double) item.get("active_faults3");
					if(Double.compare(obj.getActive_faults3(), active_faults3) == 0 && obj.getActive_faults3() > 0 && active_faults3 > 0) { 
						totalFault3++;
					}
					
					double limits = (double) item.get("limits");
					if(Double.compare(obj.getLimits(), limits) == 0 && obj.getLimits() > 0 && limits > 0) { 
						totalLimits++;
					}
					
					double status = (double) item.get("status");
					if(Double.compare(obj.getStatus(), status) == 0 && obj.getStatus() > 0 && status > 0) { 
						totalStatus++;
					}
					
					double warnings1 = (double) item.get("warnings1");
					if(Double.compare(obj.getWarnings1(), warnings1) == 0 && obj.getWarnings1() > 0 && warnings1 > 0) { 
						totalWarning++;
					}
				}
				rowItem.setTotalFault1(totalFault1);
				rowItem.setTotalFault2(totalFault2);
				rowItem.setTotalFault3(totalFault3);
				rowItem.setTotalLimits(totalLimits);
				rowItem.setTotalStatus(totalStatus);
				rowItem.setTotalWarning(totalWarning);
				
			}
			
			if (rowItem == null)
				return new ModelAdvancedEnergySolaronEntity();
		} catch (Exception ex) {
			log.error("ModelAdvancedEnergySolaron.ModelAdvancedEnergySolaronEntity", ex);
			return new ModelAdvancedEnergySolaronEntity();
		}
		return rowItem;
	}
	
	
	
	/**
	 * @description check trigger alert fault code
	 * @author long.pham
	 * @since 2022-09-26
	 * @param data from datalogger
	 */

	public void checkTriggerAlertModelAdvancedEnergySolaron(ModelAdvancedEnergySolaronEntity obj) {
		// Check device alert by fault code
		long fault1 = (obj.getActive_faults1() > 0 && obj.getActive_faults1() != 0.001) ? (long) obj.getActive_faults1() : 0;
		long fault2 = (obj.getActive_faults2() > 0 && obj.getActive_faults2() != 0.001) ? (long) obj.getActive_faults2() : 0;
		long fault3 = (obj.getActive_faults3() > 0 && obj.getActive_faults3() != 0.001) ? (long) obj.getActive_faults3() : 0;
		long limitCode = (obj.getLimits() > 0 && obj.getLimits() != 0.001) ? (long) obj.getLimits() : 0;
		long statusCode = (obj.getStatus() > 0 && obj.getStatus() != 0.001) ? (long) obj.getStatus() : 0;
		long warningCode = (obj.getWarnings1() > 0 && obj.getWarnings1() != 0.001) ? (long) obj.getWarnings1() : 0;
		
		ModelAdvancedEnergySolaronEntity rowItem = (ModelAdvancedEnergySolaronEntity) checkAlertWriteCode(obj);
		
		if(warningCode > 0 && rowItem.getTotalWarning() >= 20) {
			try {
				String toBinary = Long.toBinaryString(warningCode);
				String toBinary32Bit = String.format("%32s", toBinary).replaceAll(" ", "0");
				int v = 0;
				for (int b = toBinary32Bit.length() - 1; b >= 0; b--) {
					int index = b;
					int bitLevel = Integer.parseInt(toBinary32Bit.substring(index, Math.min(index + 1, toBinary32Bit.length())));
					if (bitLevel == 1) {
						int errorId = LibErrorCode.GetWarningsCodeModelAdvancedSolaron(v);
						
						if (errorId > 0) {
							AlertEntity alertDeviceItem = new AlertEntity();
							alertDeviceItem.setId_device(obj.getId_device());
							alertDeviceItem.setStart_date(obj.getTime());
							alertDeviceItem.setId_error(errorId);
							boolean checkAlertDeviceExist = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem) > 0;
							boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem) > 0;
							if (!checkAlertDeviceExist && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem);
							}
						}
					}
					v += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Close warning code 
			try {
				if(rowItem.getTotalWarning() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					// type 6 is warning code
					alertItemClose.setFaultCodeLevel(5);
					List dataListWarningCode = new ArrayList();
					dataListWarningCode = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListWarningCode.size() > 0) {
						for(int i = 0; i < dataListWarningCode.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListWarningCode.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		if(statusCode > 0  && rowItem.getTotalStatus() >= 20) {
			try {
				String toBinary = Long.toBinaryString(statusCode);
				String toBinary32Bit = String.format("%32s", toBinary).replaceAll(" ", "0");
				int v = 0;
				for (int b = toBinary32Bit.length() - 1; b >= 0; b--) {
					int index = b;
					int bitLevel = Integer.parseInt(toBinary32Bit.substring(index, Math.min(index + 1, toBinary32Bit.length())));
					if (bitLevel == 1) {
						int errorId = LibErrorCode.GetStatusCodeModelAdvancedSolaron(v);
						if (errorId > 0) {
							AlertEntity alertDeviceItem = new AlertEntity();
							alertDeviceItem.setId_device(obj.getId_device());
							alertDeviceItem.setStart_date(obj.getTime());
							alertDeviceItem.setId_error(errorId);
							boolean checkAlertDeviceExist = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem) > 0;
							boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem) > 0;
							if (!checkAlertDeviceExist && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem);
							}
						}
					}
					v += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Close status code 
			try {
				if(rowItem.getTotalStatus() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					// type 5 is status code
					alertItemClose.setFaultCodeLevel(5);
					List dataListStatusCode = new ArrayList();
					dataListStatusCode = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListStatusCode.size() > 0) {
						for(int i = 0; i < dataListStatusCode.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListStatusCode.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		if(limitCode > 0  && rowItem.getTotalLimits() >= 20) {
			try {
				String toBinary = Long.toBinaryString(limitCode);
				String toBinary32Bit = String.format("%32s", toBinary).replaceAll(" ", "0");
				int v = 0;
				for (int b = toBinary32Bit.length() - 1; b >= 0; b--) {
					int index = b;
					int bitLevel = Integer.parseInt(toBinary32Bit.substring(index, Math.min(index + 1, toBinary32Bit.length())));
					if (bitLevel == 1) {
						int errorId = LibErrorCode.GetLimitCodeModelAdvancedSolaron(v);
						if (errorId > 0) {
							AlertEntity alertDeviceItem = new AlertEntity();
							alertDeviceItem.setId_device(obj.getId_device());
							alertDeviceItem.setStart_date(obj.getTime());
							alertDeviceItem.setId_error(errorId);
							boolean checkAlertDeviceExist = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem) > 0;
									boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem) > 0;
									if (!checkAlertDeviceExist && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem);
							}
						}
					}
					v += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Close limits code 
			try {
				if(rowItem.getTotalLimits() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					// Type 4 is limits code
					alertItemClose.setFaultCodeLevel(4);
					List dataListLimitCode = new ArrayList();
					dataListLimitCode = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListLimitCode.size() > 0) {
						for(int i = 0; i < dataListLimitCode.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListLimitCode.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		if (fault1 > 0  && rowItem.getTotalFault1() >= 20) {
			try {
				String toBinary = Long.toBinaryString(fault1);
				String toBinary32Bit = String.format("%32s", toBinary).replaceAll(" ", "0");
				int v = 0;
				for (int b = toBinary32Bit.length() - 1; b >= 0; b--) {
					int index = b;
					int bitLevel = Integer
							.parseInt(toBinary32Bit.substring(index, Math.min(index + 1, toBinary32Bit.length())));
					if (bitLevel == 1) {
						int errorId = LibErrorCode.GetErrorCodeModelAdvancedSolaron(v, 1);
						if (errorId > 0) {
							AlertEntity alertDeviceItem = new AlertEntity();
							alertDeviceItem.setId_device(obj.getId_device());
							alertDeviceItem.setStart_date(obj.getTime());
							alertDeviceItem.setId_error(errorId);
							boolean checkAlertDeviceExist = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem) > 0;
									boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem) > 0;
									if (!checkAlertDeviceExist && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem);
							}
						}
					}
					v += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// Close fault code 1
			try {
				if(rowItem.getTotalFault1() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					alertItemClose.setFaultCodeLevel(1);
					List dataListFault1 = new ArrayList();
					dataListFault1 = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListFault1.size() > 0) {
						for(int i = 0; i < dataListFault1.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListFault1.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (fault2 > 0  && rowItem.getTotalFault2() >= 20) {
			try {
				String toBinary2 = Long.toBinaryString(fault2);
				String toBinary32Bit2 = String.format("%32s", toBinary2).replaceAll(" ", "0");
				int v2 = 0;
				for (int b2 = toBinary32Bit2.length() - 1; b2 >= 0; b2--) {
					int index2 = b2;
					int bitLevel2 = Integer
							.parseInt(toBinary32Bit2.substring(index2, Math.min(index2 + 1, toBinary32Bit2.length())));
					if (bitLevel2 == 1) {
						int errorId2 = LibErrorCode.GetErrorCodeModelAdvancedSolaron(v2, 2);
						if (errorId2 > 0) {
							AlertEntity alertDeviceItem2 = new AlertEntity();
							alertDeviceItem2.setId_device(obj.getId_device());
							alertDeviceItem2.setStart_date(obj.getTime());
							alertDeviceItem2.setId_error(errorId2);
							boolean checkAlertDeviceExist2 = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem2) > 0;
									boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem2) > 0;
									if (!checkAlertDeviceExist2 && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem2);
							}
						}
					}
					v2 += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Close fault code 2
			try {
				if(rowItem.getTotalFault2() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					alertItemClose.setFaultCodeLevel(2);
					List dataListFault2 = new ArrayList();
					dataListFault2 = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListFault2.size() > 0) {
						for(int i = 0; i < dataListFault2.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListFault2.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (fault3 > 0   && rowItem.getTotalFault3() >= 20) {
			try {
				String toBinary3 = Long.toBinaryString(fault3);
				String toBinary32Bit3 = String.format("%32s", toBinary3).replaceAll(" ", "0");
				int v3 = 0;
				for (int b3 = toBinary32Bit3.length() - 1; b3 >= 0; b3--) {
					int index3 = b3;
					int bitLevel3 = Integer
							.parseInt(toBinary32Bit3.substring(index3, Math.min(index3 + 1, toBinary32Bit3.length())));
					if (bitLevel3 == 1) {
						int errorId3 = LibErrorCode.GetErrorCodeModelAdvancedSolaron(v3, 3);
						if (errorId3 > 0) {
							AlertEntity alertDeviceItem3 = new AlertEntity();
							alertDeviceItem3.setId_device(obj.getId_device());
							alertDeviceItem3.setStart_date(obj.getTime());
							alertDeviceItem3.setId_error(errorId3);
							boolean checkAlertDeviceExist3 = (int) queryForObject("BatchJob.checkAlertlExist",
									alertDeviceItem3) > 0;
									boolean errorExits = (int) queryForObject("BatchJob.checkErrorExist", alertDeviceItem3) > 0;
									if (!checkAlertDeviceExist3 && errorExits) {
								insert("BatchJob.insertAlert", alertDeviceItem3);
							}
						}
					}
					v3 += 1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// Close fault code 3
			try {
				if(rowItem.getTotalFault3() == 0) {
					AlertEntity alertItemClose = new AlertEntity();
					alertItemClose.setId_device(obj.getId_device());
					alertItemClose.setFaultCodeLevel(3);
					List dataListFault3 = new ArrayList();
					dataListFault3 = queryForList("ModelAdvancedEnergySolaron.getListTriggerFaultCode", alertItemClose);
					if(dataListFault3.size() > 0) {
						for(int i = 0; i < dataListFault3.size(); i++) {
							Map<String, Object> itemFault = (Map<String, Object>) dataListFault3.get(i);
							int id =  Integer.parseInt(itemFault.get("id").toString());
							int idError =  Integer.parseInt(itemFault.get("id_error").toString());
							alertItemClose.setEnd_date(itemFault.get("end_date").toString());
							alertItemClose.setId(id );
							alertItemClose.setId_error(idError);
							update("Alert.UpdateErrorRow", alertItemClose);
						}
					}
				}
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
