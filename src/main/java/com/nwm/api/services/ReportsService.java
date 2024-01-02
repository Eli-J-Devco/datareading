/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.ibatis.session.SqlSession;

import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.DailyDateEntity;
import com.nwm.api.entities.EnergyExpectationsEntity;
import com.nwm.api.entities.MonthlyDateEntity;
import com.nwm.api.entities.QuarterlyDateEntity;
import com.nwm.api.entities.ReportsEntity;
import com.nwm.api.entities.SiteEntity;
import com.nwm.api.entities.ViewReportEntity;

public class ReportsService extends DB {
	
	private static String readProperty(ResourceBundle resourceBundle, String key, String defaultValue) {
		String value = defaultValue;
		try {
			value = resourceBundle.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
			dbLog.error(e);
		}
		return value;
	}
	
	/**
	 * @description get monthly  report 
	 * @author long.pham
	 * @since 2022-08-23
	 * @param id_site, date_from, data_to
	 */
	
	public Object getDailyReport(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		DecimalFormat df = new DecimalFormat("###.#");
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getDetailReport", obj);
			if (dataObj == null) {
				return null;
			}
			
			List dataListDeviceMeter = queryForList("Reports.getListDeviceTypeMeter", obj);
			if(dataListDeviceMeter.size() > 0 ) {
				obj.setGroupDevices(dataListDeviceMeter);
				List dataPower = queryForList("Reports.getDataEnergyMeterDailyReport", obj);
				if (dataPower.size() > 0) {
					dataObj.setDataReports(dataPower);
				}
			} else {
				List dataListInverter = queryForList("Reports.getListDeviceTypeInverter", obj);
				if(dataListInverter.size() > 0) {
					obj.setGroupDevices(dataListInverter);
					List dataPower = queryForList("Reports.getDataEnergyInverterDailyReport", obj);
					if (dataPower.size() > 0) {
						dataObj.setDataReports(dataPower);
					}
				} 
			}
			
			// Create list date 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			SimpleDateFormat catFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			
			SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:00");
			Date startDate = dateFormat.parse(obj.getStart_date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			List<DailyDateEntity> categories = new ArrayList<DailyDateEntity> ();
			int minute = 5;
			int forCount = 288 * 3;
			if(obj.getData_intervals() == 1) {
				minute = 5;
				forCount = 288 * 3;
			} else if(obj.getData_intervals() == 2) {
				minute = 15;
				forCount = 96*3;
			} else if(obj.getData_intervals() == 3) {
				minute = 60;
				forCount = 24*3;
			}
			for(int t = 0; t < forCount; t++) {
				cal.setTime(startDate);
				DailyDateEntity headerDate = new DailyDateEntity();
				cal.add(Calendar.MINUTE, t * minute);
				
				headerDate.setTime_format(dateFormat.format(cal.getTime()));
				String hours = dateFormatHour.format(cal.getTime());
				headerDate.setCategories_time(catFormat.format(cal.getTime()));
				headerDate.setEnergy(0.001);
				headerDate.setPower(0.001);
				headerDate.setIrradiance(0.001);
				headerDate.setHour_time(hours);
				categories.add(headerDate);
			}
			
			List dataPower = dataObj.getDataReports();
			List<DailyDateEntity> dataNewPower = new ArrayList<DailyDateEntity> ();
			if(categories.size() > 0) {
				for (DailyDateEntity item : categories) {
					boolean flag = false;
					DailyDateEntity mapItemObj = new DailyDateEntity();
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
					        	mapItemObj.setTime_format(itemT.get("categories_time").toString());
					        	mapItemObj.setHour_time(itemT.get("hour_time").toString());
					        	
					        	
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
						DailyDateEntity mapItem = new DailyDateEntity();
						mapItem.setCategories_time(item.getCategories_time());
						mapItem.setTime_format(item.getTime_format());
						mapItem.setHour_time(item.getHour_time());
						mapItem.setIrradiance(item.getIrradiance());
						mapItem.setEnergy(item.getEnergy());
						mapItem.setPower(item.getPower());
						
						dataNewPower.add(mapItem);
					} else {
						dataNewPower.add(mapItemObj);
					}
				}
			}
			
			dataObj.setDataReports(dataNewPower);
			
			
			// get irradiance 
			List dataListDeviceIrr = queryForList("Reports.getListDeviceTypeIrradiance", obj);
			if (dataListDeviceIrr.size() > 0) {
				obj.setGroupDevices(dataListDeviceIrr);
				List dataIrradiance = queryForList("Reports.getDataIrradiance", obj);
				if(dataNewPower.size() > 0) {
					List<DailyDateEntity> dataNewIrr = new ArrayList<DailyDateEntity> ();
					for (DailyDateEntity item : dataNewPower) {
						for( int v = 0; v < dataIrradiance.size(); v++){
							Map<String, Object> itemT = (Map<String, Object>) dataIrradiance.get(v);
							String categoriesTime = item.getTime_format();
							String powerTime = itemT.get("categories_time").toString();
					        if (categoriesTime.equals(powerTime)) {
					        	item.setIrradiance(Double.parseDouble(itemT.get("irradiance").toString()));
					        	break;
					        }
					    }
						dataNewIrr.add(item);
					}
					dataObj.setDataReports(dataNewIrr);
				}
			}
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public DailyDateEntity findUsingIterator( String name, List<DailyDateEntity> items) {
//	    Iterator<DailyDateEntity> iterator = items.iterator();
	    
	    for (DailyDateEntity item : items) {
	        if (item.getCategories_time().equals(name)) {
	            return item;
	        }
	    }
	    return null;
	    
//	    while (iterator.hasNext()) {
//	    	DailyDateEntity item = iterator.next();
//	        if (item.getCategories_time().equals(name)) {
//	            return item;
//	        }
//	    }
//	    return null;
	}
	
	/**
	 * @description get monthly  report 
	 * @author long.pham
	 * @since 2022-08-23
	 * @param id_site, date_from, data_to
	 */
	
	public Object getAnnuallyReport(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getDetailReport", obj);
			if (dataObj == null) {
				return null;
			}
			
			obj.setTable_data_report(dataObj.getTable_data_report());
			List dataListInverter = queryForList("Reports.getListDeviceTypeInverter", obj);
			List dataEnergy = queryForList("Reports.getDataEnergyAnnuallyReport", obj);
			
			if (dataEnergy.size() > 0) {
				dataObj.setDataReports(dataEnergy);
			}
				
			if(dataListInverter.size() > 0) {
				List dataAvailability = queryForList("Reports.getInverterAvailability", obj);
				dataObj.setDataAvailability(dataAvailability);
				dataObj.setDeviceType("inverter");
			} else {
				List dataAvailability = queryForList("Reports.getMeterAvailability", obj);
				dataObj.setDataAvailability(dataAvailability);
				dataObj.setDeviceType("meter");
			}
			
			List dataEnergyExpectations = queryForList("Reports.getReportEnergyExpectations", obj);
			dataObj.setDataExpectations(dataEnergyExpectations);
			
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	

	/**
	 * @description get monthly  report 
	 * @author long.pham
	 * @since 2022-08-23
	 * @param id_site, date_from, data_to
	 */
	
	public Object getQuarterlyReport(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getDetailReport", obj);
			if (dataObj == null) {
				return null;
			}
			
			obj.setTable_data_report(dataObj.getTable_data_report());
			List dataEnergy = queryForList("Reports.getDataEnergyQurterlyReport", obj);
			if (dataEnergy.size() > 0) {
				dataObj.setDataReports(dataEnergy);
			}
			
			EnergyExpectationsEntity expec = (EnergyExpectationsEntity) queryForObject("Reports.getExpectationsRow", obj);
			
			List<QuarterlyDateEntity> categories = new ArrayList<QuarterlyDateEntity> ();
			
			SimpleDateFormat dateFormat;
			SimpleDateFormat catFormat;
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
			
			boolean quarterlyReportByDay = dataObj.getData_intervals() == 11;
			
			if (quarterlyReportByDay) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				catFormat = new SimpleDateFormat("MM/dd/yyyy");

				List dataInverterAvailability = queryForList("Reports.getDataInverterAvailabilityByDay", obj);
				if (dataInverterAvailability.size() > 0) { 
					dataObj.setDataAvailability(dataInverterAvailability);
				}
				
				List dataWeatherStation = queryForList("Reports.getDataWeatherStationByDay", obj);
				if (dataWeatherStation != null && dataWeatherStation.size() > 0) {
					dataObj.setDataWeatherStation(dataWeatherStation);
				}
			} else {
				dateFormat = new SimpleDateFormat("yyyy-MM");
				catFormat = new SimpleDateFormat("MMM-yyyy");
			}
			
			Date startDate = dateFormat.parse(obj.getStart_date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			
			for (int i = 0; i < 3; i++) {
				int month = Integer.parseInt(monthFormat.format(cal.getTime()).toString());
				int expecValue = 0;
				if(expec != null) {
					switch ( month ) {
					case  1: expecValue = expec.getJan(); break;
					case  2: expecValue = expec.getFeb(); break;
					case  3: expecValue = expec.getMar(); break;
					case  4: expecValue = expec.getApr(); break;
					case  5: expecValue = expec.getMay(); break;
					case  6: expecValue = expec.getJun(); break;
					case  7: expecValue = expec.getJul(); break;
					case  8: expecValue = expec.getAug(); break;
					case  9: expecValue = expec.getSep(); break;
					case  10: expecValue = expec.getOct(); break;
					case  11: expecValue = expec.getNov(); break;
					case  12: expecValue = expec.getDec(); break;
					default:
						expecValue = 0;
					}
				}
				
				if (quarterlyReportByDay) {
					int numOfDaysInMonth = cal.getActualMaximum(Calendar.DATE);
					
					for (int j = 0; j < numOfDaysInMonth; j++) {
						QuarterlyDateEntity category = new QuarterlyDateEntity();
						category.setTime_format(dateFormat.format(cal.getTime()));
						category.setCategories_time(catFormat.format(cal.getTime()));
						category.setEstimated((double) expecValue/numOfDaysInMonth);
						categories.add(category);
						cal.add(Calendar.DATE, 1);
					}
				} else {
					QuarterlyDateEntity category = new QuarterlyDateEntity();
					category.setTime_format(dateFormat.format(cal.getTime()));
					category.setCategories_time(catFormat.format(cal.getTime()));
					category.setEstimated((double) expecValue);
					categories.add(category);
					cal.add(Calendar.MONTH, 1);
				}
			}
			
			List data = dataObj.getDataReports();
			List<QuarterlyDateEntity> dataNew = new ArrayList<QuarterlyDateEntity> ();
			
			if (categories.size() > 0) {
				for (QuarterlyDateEntity item : categories) {
					boolean flag = false;
					QuarterlyDateEntity mapItemObj = new QuarterlyDateEntity();
					
					if(data != null && data.size() > 0) {
						for( int v = 0; v < data.size(); v++) {
							Map<String, Object> itemT = (Map<String, Object>) data.get(v);
							String categoriesTime = item.getTime_format();
							String powerTime = itemT.get(quarterlyReportByDay ? "time_format_by_day" : "time_format").toString();
							
							if (categoriesTime.equals(powerTime)) {
								flag = true;
								mapItemObj.setCategories_time(itemT.get(quarterlyReportByDay ? "categories_time_by_day" : "categories_time").toString());
								mapItemObj.setTime_format(itemT.get(quarterlyReportByDay ? "time_format_by_day" : "time_format").toString());
								mapItemObj.setActual(Double.parseDouble(itemT.get("chart_energy_kwh").toString()));
								mapItemObj.setEstimated(item.getEstimated());
								break;
							}
						}
					}
					
					if(flag == false) {
						QuarterlyDateEntity mapItem = new QuarterlyDateEntity();
						mapItem.setCategories_time(item.getCategories_time());
						mapItem.setTime_format(item.getTime_format());
						mapItem.setActual(null);
						mapItem.setEstimated(null);
						dataNew.add(mapItem);
					} else {
						dataNew.add(mapItemObj);
					}
				}
			}
			
			dataObj.setDataReports(dataNew);
			
			if (quarterlyReportByDay) {
				List dataInverterAvailability = dataObj.getDataAvailability();
				List<QuarterlyDateEntity> dataInverterNew = new ArrayList<QuarterlyDateEntity> ();
				
				if (categories.size() > 0) {
					for (QuarterlyDateEntity item : categories) {
						boolean flag = false;
						QuarterlyDateEntity mapItemObj = new QuarterlyDateEntity();
						
						if (dataInverterAvailability != null && dataInverterAvailability.size() > 0) {
							for( int v = 0; v < dataInverterAvailability.size(); v++) {
								Map<String, Object> itemT = (Map<String, Object>) dataInverterAvailability.get(v);
								String categoriesTime = item.getTime_format();
								String powerTime = itemT.get("time_format").toString();
								
								if (categoriesTime.equals(powerTime)) {
									flag = true;
									mapItemObj.setCategories_time(itemT.get("categories_time").toString());
									mapItemObj.setTime_format(itemT.get("time_format").toString());
									mapItemObj.setInverterAvailability(Double.parseDouble(itemT.get("InverterAvailability").toString()));
									break;
								}
							}
						}
						
						if(flag == false) {
							QuarterlyDateEntity mapItem = new QuarterlyDateEntity();
							mapItem.setCategories_time(item.getCategories_time());
							mapItem.setTime_format(item.getTime_format());
							mapItem.setInverterAvailability(null);
							dataInverterNew.add(mapItem);
						} else {
							dataInverterNew.add(mapItemObj);
						}
					}
					
					dataObj.setDataAvailability(dataInverterNew);
				}
				
				
				List dataWeatherStation = dataObj.getDataWeatherStation();
				List<QuarterlyDateEntity> dataWeatherStationNew = new ArrayList<QuarterlyDateEntity> ();
				
				if (categories.size() > 0) {
					for (QuarterlyDateEntity item : categories) {
						boolean flag = false;
						QuarterlyDateEntity mapItemObj = new QuarterlyDateEntity();
						
						if (dataWeatherStation != null && dataWeatherStation.size() > 0) {
							for( int v = 0; v < dataWeatherStation.size(); v++) {
								Map<String, Object> itemT = (Map<String, Object>) dataWeatherStation.get(v);
								String categoriesTime = item.getTime_format();
								String powerTime = itemT.get("time_format").toString();
								
								if (categoriesTime.equals(powerTime)) {
									flag = true;
									mapItemObj.setCategories_time(itemT.get("categories_time").toString());
									mapItemObj.setTime_format(itemT.get("time_format").toString());
									mapItemObj.setPOAAVG(itemT.get("POAAVG") != null ? Double.parseDouble(itemT.get("POAAVG").toString()) : null);
									mapItemObj.setTCellAVG(itemT.get("TCellAVG") != null ? Double.parseDouble(itemT.get("TCellAVG").toString()) : null);
									break;
								}
							}
						}
						
						if(flag == false) {
							QuarterlyDateEntity mapItem = new QuarterlyDateEntity();
							mapItem.setCategories_time(item.getCategories_time());
							mapItem.setTime_format(item.getTime_format());
							mapItem.setPOAAVG(null);
							mapItem.setTCellAVG(null);
							dataWeatherStationNew.add(mapItem);
						} else {
							dataWeatherStationNew.add(mapItemObj);
						}
					}
					
					dataObj.setDataWeatherStation(dataWeatherStationNew);
				}
				
			}
			
			
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * @description update site rec_id
	 * @author long.pham
	 * @since 2021-01-11
	 * @param id
	 */
	public boolean updateRECID(SiteEntity obj) {
		try {
			return update("Reports.updateRECID", obj) > 0;
		} catch (Exception ex) {
			log.error("Reports.updateRECID", ex);
			return false;
		}
	}
	
	
	/**
	 * @description update site gu_id
	 * @author long.pham
	 * @since 2023-03-27
	 * @param id
	 */
	public boolean updateGUID(SiteEntity obj) {
		try {
			return update("Reports.updateGUID", obj) > 0;
		} catch (Exception ex) {
			log.error("Reports.updateGUID", ex);
			return false;
		}
	}
	
	/**
	 * @description get list site for page employee manage site
	 * @author long.pham
	 * @since 2021-01-07
	 */

	public List getListSiteByEmployee(ReportsEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("Reports.getListSiteByEmployee", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}
	/**
	 * @description Get list site sub-group by employee
	 * @author Hung.Bui
	 * @since 2023-07-24
	 */
	
	public List getListSiteSubGroupByEmployee(ReportsEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("Reports.getListSiteSubGroupByEmployee", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}

	/**
	 * @description insert report
	 * @author long.pham
	 * @since 2021-12-27
	 */
	public ReportsEntity insertReports(ReportsEntity obj) {
		SqlSession session = this.beginTransaction();
		try {
			session.insert("Reports.insertReports", obj);
			int insertLastId = obj.getId();
			
			List dataSite = obj.getDataSite();
			if (insertLastId > 0 && dataSite.size() > 0) {
				session.insert("Reports.insertReportSiteMap", obj);
			} else {
				return null;
			}
			
			session.commit();
			return obj;
		} catch (Exception ex) {
			session.rollback();
			log.error("Reports.insertReports", ex);
			return null;
		} finally {
			session.close();
		}

	}

	/**
	 * @description update role
	 * @author long.pham
	 * @since 2021-01-08
	 * @param id
	 */
	public boolean updateReports(ReportsEntity obj) {

		SqlSession session = this.beginTransaction();
		try {
			session.update("Reports.updateReports", obj);
			
			List dataSite = obj.getDataSite();
			if (dataSite.size() <= 0) {
				throw new Exception();
			}
			
			session.delete("Reports.deleteReportSiteMap", obj);
			session.insert("Reports.insertReportSiteMap", obj);
			
			session.commit();
			return true;
		} catch (Exception ex) {
			session.rollback();
			log.error("Reports.updateReports", ex);
			return false;
		} finally {
			session.close();
		}
	}

	/**
	 * @description get list site by id customer
	 * @author long.pham
	 * @since 2021-12-27
	 * @param object
	 */

	public List getList(ReportsEntity obj) {
		List dataList = new ArrayList();
		try {
			dataList = queryForList("Reports.getList", obj);
			if (dataList == null)
				return new ArrayList();
		} catch (Exception ex) {
			return new ArrayList();
		}
		return dataList;
	}

	public int getTotalRecord(ReportsEntity obj) {
		try {
			return (int) queryForObject("Reports.getListCount", obj);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * @description delete site
	 * @author long.pham
	 * @since 2021-01-11
	 * @param id
	 */
	public boolean deleteReports(ReportsEntity obj) {
		SqlSession session = this.beginTransaction();
		try {
			session.delete("Reports.deleteReportSiteMap", obj);
			int rowDelete = session.delete("Reports.deleteReports", obj);
			session.commit();
			return rowDelete > 0;
		} catch (Exception ex) {
			session.rollback();
			log.error("Reports.deleteReports", ex);
			return false;
		} finally {
			session.close();
		}
	}
	
	
	
	/**
	 * @description get monthly  report 
	 * @author long.pham
	 * @since 2022-06-12
	 * @param id_site
	 */
	
	public Object getMonthlyReport(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getDetailReport", obj);
			if (dataObj == null) {
				return null;
			}
			
			obj.setTable_data_report(dataObj.getTable_data_report());
			if (dataObj.getType_report() == 1) {
				List dataEnergy = queryForList("Reports.getDataEnergyMonthlyReport", obj);
				if (dataEnergy.size() > 0) {
					dataObj.setDataReports(dataEnergy);
				}
				
				EnergyExpectationsEntity expec = (EnergyExpectationsEntity) queryForObject("Reports.getExpectationsRow", obj);
				
				// Create list date 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
				SimpleDateFormat catFormat = new SimpleDateFormat("MM/dd/yyyy");
				
				SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
				SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
				
				Date startDate = dateFormat.parse(obj.getStart_date() + " AM");
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				List<MonthlyDateEntity> categories = new ArrayList<MonthlyDateEntity> ();
				int day = 1;
				int forCount = Integer.parseInt(dayFormat.format(cal.getTime()).toString());
				
				int month = Integer.parseInt(monthFormat.format(cal.getTime()).toString());
				
				int expecValue = 0;
				if(expec != null) {
					switch ( month ) {
					case  1: expecValue = expec.getJan(); break;
					case  2: expecValue = expec.getFeb(); break;
					case  3: expecValue = expec.getMar(); break;
					case  4: expecValue = expec.getApr(); break;
					case  5: expecValue = expec.getMay(); break;
					case  6: expecValue = expec.getJun(); break;
					case  7: expecValue = expec.getJul(); break;
					case  8: expecValue = expec.getAug(); break;
					case  9: expecValue = expec.getSep(); break;
					case  10: expecValue = expec.getOct(); break;
					case  11: expecValue = expec.getNov(); break;
					case  12: expecValue = expec.getDec(); break;
					default:
						expecValue = 0;
					}
					
				}
				
				for(int t = 0; t < forCount; t++) {
					cal.setTime(startDate);
					MonthlyDateEntity headerDate = new MonthlyDateEntity();
					cal.add(Calendar.DATE, t * day);
					headerDate.setTime_format(dateFormat.format(cal.getTime()));
					headerDate.setCategories_time(catFormat.format(cal.getTime()));
					headerDate.setActual(0.0);
					headerDate.setEstimated((double) expecValue / forCount);
					headerDate.setPercent( expecValue > 0 ? (0.0 / expecValue) : 0);
					categories.add(headerDate);
				}
				
				
				
				List data = dataObj.getDataReports();
				List<MonthlyDateEntity> dataNew = new ArrayList<MonthlyDateEntity> ();
				if(categories.size() > 0) {
					for (MonthlyDateEntity item : categories) {
						boolean flag = false;
						MonthlyDateEntity mapItemObj = new MonthlyDateEntity();
						if(data != null && data.size() > 0) {
							for( int v = 0; v < data.size(); v++){
								Map<String, Object> itemT = (Map<String, Object>) data.get(v);
								String categoriesTime = item.getTime_format();
								String powerTime = itemT.get("time_format").toString();
								
								if (categoriesTime.equals(powerTime)) {
									flag = true;
									mapItemObj.setCategories_time(itemT.get("categories_time").toString());
									
									mapItemObj.setTime_format(itemT.get("time_format").toString());
									mapItemObj.setActual(Double.parseDouble(itemT.get("chart_energy_kwh").toString()) );
									mapItemObj.setEstimated( (double) expecValue/forCount );
									double energy = Double.parseDouble(itemT.get("chart_energy_kwh")!= null ? itemT.get("chart_energy_kwh").toString() : "0.0");
									Double percent = (expecValue / forCount > 0) ?  ((energy /  (expecValue / forCount)) * 100) : 0;
									mapItemObj.setPercent(percent);
									break;
								}
							}
						}
						
						if(flag == false) {
							MonthlyDateEntity mapItem = new MonthlyDateEntity();
							mapItem.setCategories_time(item.getCategories_time());
							mapItem.setActual(item.getActual());
							mapItem.setEstimated(item.getEstimated());
							mapItem.setPercent(item.getPercent());
							mapItem.setTime_format(item.getTime_format());
							dataNew.add(mapItem);
						} else {
							dataNew.add(mapItemObj);
						}
					}
				}
				
				dataObj.setDataReports(dataNew);
			} else {
				List siteList = new ArrayList<>();
				
				switch (dataObj.getType_option()) {
					// entire portfolio
					case 1:
						siteList = queryForList("Reports.getListSiteByIdEmployee", dataObj);
						break;
					// selected site
					case 2:
						siteList = queryForList("Reports.getListSelectedSiteByIdSite", dataObj);
						break;
					// sub-group
					case 3:
						siteList = queryForList("Reports.getListSiteBySubGroup", dataObj);
						break;
					default:
						break;
				}
				
				if (dataObj.getData_intervals() == 12) {
					List dataSiteList = new ArrayList<>();
					
					for (int i = 0; i < siteList.size(); i++) {
						Map<String, Object> siteItem = (Map<String, Object>) siteList.get(i);
						
						SimpleDateFormat dateFromFormat = new SimpleDateFormat("yyyy-MM-dd"); 
						SimpleDateFormat dateToFormat = new SimpleDateFormat("MM/dd/yyyy");
						SimpleDateFormat monthFormat = new SimpleDateFormat("MM/YYYY");
						
						obj.setId_site((int) siteItem.get("id"));
						List dataEnergy = queryForList("Reports.getDataEnergyMonthlyBuiltinReport", obj);
						
						if (dataEnergy.size() > 0) {
							siteItem.put("dataReport", dataEnergy);
						} else {
							Map<String, Object> mapItem = new HashMap<String, Object>();
							mapItem.put("id", siteItem.get("id"));
							mapItem.put("name", siteItem.get("name"));
							mapItem.put("time_format", monthFormat.format(dateFromFormat.parse(obj.getStart_date())));
							mapItem.put("chart_energy_kwh", 0.0);
							
							siteItem.put("dataReport", Arrays.asList(mapItem));
						}
						
						siteItem.put("id", siteItem.get("id"));
						siteItem.put("name", siteItem.get("name"));
						siteItem.put("startDate", dateToFormat.format(dateFromFormat.parse(obj.getStart_date())));
						siteItem.put("endDate", dateToFormat.format(dateFromFormat.parse(obj.getEnd_date())));
						
						dataSiteList.add(siteItem);
					}
					
					dataObj.setDataSite(dataSiteList);
				}
			}
			
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * @description get custom  report 
	 * @author Hung.Bui
	 * @since 2022-12-15
	 * @param id_site, date_from, date_to
	 */
	
	public Object getCustomReport(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getDetailReport", obj);

			if (dataObj == null) {
				return null;
			}
			
			obj.setTable_data_report(dataObj.getTable_data_report());
			List dataPower = queryForList("Reports.getDataEnergyCustomReport", obj);
			if (dataPower.size() > 0 ) {
				dataObj.setDataReports(dataPower);
			}
			
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	
	
	
	
	
	
	

	public Object getSiteDetail(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("Reports.getSiteDetail", obj);
			if (dataObj == null) {
				return null;
			}
			
			switch (dataObj.getId_site_type()) {
				case 1:
				case 6:
				case 2:
				case 4:
					obj.setTable_name("model_shark100");
					break;
				
				case 3:
					obj.setTable_name("");
					break;
				case 5:
				case 9:
					obj.setTable_name("model_veris_industries_e51c2_power_meter");
					break;
				case 7:
					obj.setTable_name("model_elkor_wattson_pv_meter");
					break;
				case 8:
					obj.setTable_name("");
					break;
			}
			
			List dataEnergy = queryForList("Reports.getReportYearDataEnergy", obj);
			dataObj.setDataReports(dataEnergy);
			
			List dataEnergyExpectations = queryForList("Reports.getReportEnergyExpectations", obj);
			dataObj.setDataExpectations(dataEnergyExpectations);
			
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * @description RENEWABLE ENERGY CREDITS
	 * @author long.pham
	 * @since 2022-06-06
	 * @param {}
	 */
	
	public Object renewableReportMonth(ViewReportEntity obj) {
		ViewReportEntity dataObj = new ViewReportEntity();
		try {
			dataObj = (ViewReportEntity) queryForObject("SitesReports.getDetailReport", obj);
			if (dataObj == null) {
				return null;
			}
			List dataListDeviceMeter = queryForList("SitesReports.getListDeviceTypeMeter", obj);
			if(dataListDeviceMeter.size() > 0 ) {
				obj.setGroupDevices(dataListDeviceMeter);
				for (int i = 0; i < dataListDeviceMeter.size(); i++) {
					Map<String, Object> deviceItem = (Map<String, Object>) dataListDeviceMeter.get(i);
					List dataEnergy = queryForList("SitesReports.getDataEnergyMeterMonth", obj);
					if (dataEnergy.size() > 0) {
						dataObj.setDataReports(dataEnergy);
					}
				}
			} else {
				List dataListInverter = queryForList("SitesReports.getListDeviceTypeInverter", obj);
				if(dataListInverter.size() > 0) {
					for (int i = 0; i < dataListInverter.size(); i++) {
						Map<String, Object> deviceItem = (Map<String, Object>) dataListInverter.get(i);
						obj.setGroupDevices(dataListInverter);
						List dataPower = queryForList("SitesReports.getDataEnergyInverterMonth", obj);
						if (dataPower.size() > 0) {
							dataObj.setDataReports(dataPower);
						}
					}
				} 
			}
			return dataObj;
		} catch (Exception ex) {
			return null;
		}
	}

	
	/**
	 * @description get list rec report 
	 * @author long.pham
	 * @since 2022-06-22
	 * @param arr id_site
	 */

	public List getListREC(ReportsEntity obj) {
		try {
			List dataList = queryForList("Reports.getDataEnergyRECReport", obj);
			if (dataList.size() <=0)
				return new ArrayList();
			return dataList;
		} catch (Exception ex) {
			return new ArrayList();
		}
	}
	
}
