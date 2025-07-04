/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.DeviceEntity;
import com.nwm.api.entities.EmployeeChartFilterEntity;
import com.nwm.api.entities.SitesAnalyticsReportEntity;


public class SitesAnalyticsService extends DB {

	/**
	 * @description get list device by id_site
	 * @author long.pham
	 * @since 2022-02-22
	 * @param id_site
	 */
	public List getListDeviceBySite(DeviceEntity obj) {
		try {
			List<Map<String, Object>> dataList = queryForList("SitesAnalytics.getListDeviceBySite", obj);
			ObjectMapper mapper = new ObjectMapper();
			dataList.forEach(item -> {
					try {
						List<Map<String, Object>> parameters = mapper.readValue(item.get("parameters").toString(), new TypeReference<List<Map<String, Object>>>(){});
						item.put("parameters", parameters.stream().sorted((param1, param2) -> param1.get("name").toString().compareTo(param2.get("name").toString())).collect(Collectors.toList()));
					} catch (JsonProcessingException e) {
						item.put("parameters", new ArrayList<Map<String, Object>>());
					}
			});
			return dataList;
		} catch (Exception ex) {
			return new ArrayList<Map<String, Object>>();
		}
	}
	
	/**
	 * @description fulfill data in specific range of time
	 * @author Hung.Bui
	 * @since 2024-05-03
	 * @param dateTimeList
	 * @param dataList
	 * @return
	 */
	private List<Map<String, Object>> fulfillData(List<Map<String, Object>> dateTimeList, List<Map<String, Object>> dataList) {
		try {
			if (dataList == null || dateTimeList.size() == 0) return dataList;
			List<Map<String, Object>> fulfilledDataList = new ArrayList<Map<String, Object>>();
			int count = 0;
			for (int i = 0; i < dateTimeList.size(); i++) {
				Map<String, Object> dateTimeItem = dateTimeList.get(i);
				if (i - count > dataList.size() - 1) {
					fulfilledDataList.add(dateTimeItem);
					continue;
				}
				Map<String, Object> dataItem = dataList.get(i - count);
				if (dateTimeItem.get("time_full").toString().equals(dataItem.get("time_full").toString())) {
					fulfilledDataList.add(dataItem);
				} else {
					fulfilledDataList.add(dateTimeItem);
					count++;
				}
			}
			
			return fulfilledDataList;
		} catch (Exception e) {
			return dataList;
		}
		
	}
	
	/**
	 * @description create date time list
	 * @author Hung.Bui
	 * @since 2024-05-03
	 * @param obj device object
	 * @param start start date time
	 * @param end end date time
	 * @return
	 */
	public List<Map<String, Object>> getDateTimeList(DeviceEntity obj, LocalDateTime start, LocalDateTime end) {
		List<Map<String, Object>> dateTimeList = new ArrayList<>();
		
		try {
			int interval = 0;
			DateTimeFormatter fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			DateTimeFormatter categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
			ChronoUnit timeUnit = ChronoUnit.MINUTES;
			boolean isDiffLessThan45Days = ChronoUnit.DAYS.between(start, end) < 45;
		
			switch (obj.getData_send_time()) {
				case 8: // 1 minute
					interval = 1;
					timeUnit = ChronoUnit.MINUTES;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	                switch (obj.getFilterBy()) {
	                	case "today":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
	                		break;
	                	case "3_day":
	                	case "this_week":
	                	case "last_week":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL HH:mm");
	                		break;
	                	case "this_month":
	                	case "last_month":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
	                }
					break;
					
				case 1: // 5 minutes
					interval = 5;
					timeUnit = ChronoUnit.MINUTES;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	                switch (obj.getFilterBy()) {
	                	case "today":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
	                		break;
	                	case "3_day":
	                	case "this_week":
	                	case "last_week":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL HH:mm");
	                		break;
	                	case "this_month":
	                	case "last_month":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
	                }
					break;
					
				case 2: // 15 minutes
					interval = 15;
					timeUnit = ChronoUnit.MINUTES;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	                switch (obj.getFilterBy()) {
	                	case "today":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
	                		break;
	                	case "3_day":
	                	case "this_week":
	                	case "last_week":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL HH:mm");
	                		break;
	                	case "this_month":
	                	case "last_month":
	                	case "12_month":
	                	case "year":
	                	case "lifetime":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
	                }
					break;
					
				case 3: // 1 hour
					interval = 1;
					timeUnit = ChronoUnit.HOURS;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	                switch (obj.getFilterBy()) {
	                	case "today":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
	                		break;
	                	case "3_day":
	                	case "this_week":
	                	case "last_week":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL HH:mm");
	                		break;
	                	case "this_month":
	                	case "last_month":
	                	case "12_month":
	                	case "year":
	                	case "lifetime":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
	                }
					break;
					
				case 4: // 1 day
					interval = 1;
					timeUnit = ChronoUnit.DAYS;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					switch (obj.getFilterBy()) {
	                	case "today":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                		break;
	                	case "3_day":
	                	case "this_week":
	                	case "last_week":
	                		categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL");
	                		break;
	                	case "this_month":
	                	case "last_month":
	                	case "12_month":
	                	case "year":
	                	case "lifetime":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
					}
					break;
					
				case 5: // 7 days
					interval = 7;
					timeUnit = ChronoUnit.DAYS;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                switch (obj.getFilterBy()) {
	                	case "this_month":
	                	case "last_month":
	                	case "12_month":
	                	case "year":
	                	case "lifetime":
	                	case "custom":
	                		categoryTimeFormat = isDiffLessThan45Days ? DateTimeFormatter.ofPattern("MM/dd") : DateTimeFormatter.ofPattern("LLL. yyyy");
	                		break;
	                }
					break;
					
				case 6: // 1 month
					interval = 1;
					timeUnit = ChronoUnit.MONTHS;
					fullTimeFormat = DateTimeFormatter.ofPattern("MM/yyyy");
					categoryTimeFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
					start = start.withDayOfMonth(1);
					break;
					
				case 7: // 1 year
					interval = 1;
					timeUnit = ChronoUnit.YEARS;
					fullTimeFormat = DateTimeFormatter.ofPattern("yyyy");
					categoryTimeFormat = DateTimeFormatter.ofPattern("yyyy");
					start = start.withDayOfYear(1);
					break;
					
				default:
					return dateTimeList;
					
			}
			
			while (!start.isAfter(end)) {
				Map<String, Object> dateTime = new HashMap<String, Object>();
				dateTime.put("time_full", start.format(fullTimeFormat));
				dateTime.put("categories_time", start.format(categoryTimeFormat));
				dateTimeList.add(dateTime);
				start = start.plus(interval, timeUnit);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return dateTimeList;
	}
	
	/**
	 * @description convert date time format
	 * @author Hung.Bui
	 * @since 2024-11-11
	 */
	public List<Map<String, Object>> convertDateTimeFormat(DeviceEntity obj, List<Map<String, Object>> dataList, LocalDateTime start, LocalDateTime end) {
		try {
			if (obj.getDate_format() == null || obj.getTime_format() == 0 || obj.getLocale() == null) return dataList;
			Locale locale = new Locale(obj.getLocale());
			DateTimeFormatter fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			DateTimeFormatter categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
			DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(obj.getDate_format() + (obj.getTime_format() == 2 ? " hh:mm a" : " HH:mm"), locale);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(obj.getDate_format(), locale);
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(obj.getTime_format() == 2 ? "ha" : "HH:mm", locale);
			
			for (Map<String, Object> data: dataList) {
				switch (obj.getFilterBy()) {
					case "today":
						switch (obj.getData_send_time()) {
							case 8: // 1 minute
							case 1: // 5 minutes
							case 2: // 15 minutes
							case 3: // 1 hour
								categoryTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
		                		data.put("time_full", LocalDateTime.parse(data.get("time_full").toString(), fullTimeFormat).format(dateTimeFormat));
		        				data.put("categories_time", LocalTime.parse(data.get("categories_time").toString(), categoryTimeFormat).format(timeFormat));
		                		break;
							case 4: // 1 day
							case 5: // 7 days
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								categoryTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		                		data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
		        				data.put("categories_time", LocalDate.parse(data.get("categories_time").toString(), categoryTimeFormat).format(DateTimeFormatter.ofPattern("dd. LLL", locale)));
								break;
						}
						break;
					
					case "3_day":
                	case "this_week":
                	case "last_week":
                		switch (obj.getData_send_time()) {
							case 8: // 1 minute
							case 1: // 5 minutes
							case 2: // 15 minutes
							case 3: // 1 hour
								categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL HH:mm");
		                		data.put("time_full", LocalDateTime.parse(data.get("time_full").toString(), fullTimeFormat).format(dateTimeFormat));
		        				data.put("categories_time", MonthDay.parse(data.get("categories_time").toString(), categoryTimeFormat).format(DateTimeFormatter.ofPattern("dd. LLL", locale)) + " " + LocalTime.parse(data.get("categories_time").toString(), categoryTimeFormat).format(timeFormat));
		                		break;
							case 4: // 1 day
							case 5: // 7 days
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL");
		                		data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
		                		data.put("categories_time", MonthDay.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
		                		break;
						}
						break;
					
                	case "this_month":
                	case "last_month":
                		categoryTimeFormat = DateTimeFormatter.ofPattern("MM/dd");
                		switch (obj.getData_send_time()) {
	                		case 8: // 1 minute
							case 1: // 5 minutes
							case 2: // 15 minutes
							case 3: // 1 hour
		        				data.put("time_full", LocalDateTime.parse(data.get("time_full").toString(), fullTimeFormat).format(dateTimeFormat));
		                		break;
							case 4: // 1 day
							case 5: // 7 days
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		                		data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
		                		break;
						}
                		data.put("categories_time", MonthDay.parse(data.get("categories_time").toString(), categoryTimeFormat).format(DateTimeFormatter.ofPattern(obj.getLocale().equals("vi") ? "dd/MM" : "MM/dd", locale)));
                		break;
                		
                	case "12_month":
                	case "year":
                	case "lifetime":
                		categoryTimeFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
                		switch (obj.getData_send_time()) {
							case 2: // 15 minutes
							case 3: // 1 hour
		                		data.put("time_full", LocalDateTime.parse(data.get("time_full").toString(), fullTimeFormat).format(dateTimeFormat));
		                		data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
		                		break;
							case 4: // 1 day
							case 5: // 7 days
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		                		data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
		                		data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
		                		break;
							case 6: // 1 month
		                		data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
								break;
						}
                		break;
                	
                	case "custom":
                		boolean isDiffLessThan45Days = ChronoUnit.DAYS.between(start, end) < 45;
	            		switch (obj.getData_send_time()) {
							case 8: // 1 minute
							case 1: // 5 minutes
							case 2: // 15 minutes
							case 3: // 1 hour
								categoryTimeFormat = DateTimeFormatter.ofPattern(isDiffLessThan45Days ? "MM/dd" : "LLL. yyyy");
		                		data.put("time_full", LocalDateTime.parse(data.get("time_full").toString(), fullTimeFormat).format(dateTimeFormat));
		                		if (isDiffLessThan45Days) data.put("categories_time", MonthDay.parse(data.get("categories_time").toString(), categoryTimeFormat).format(DateTimeFormatter.ofPattern(obj.getLocale().equals("vi") ? "dd/MM" : "MM/dd", locale)));
		                		else data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
		                		break;
							case 4: // 1 day
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								categoryTimeFormat = DateTimeFormatter.ofPattern(isDiffLessThan45Days ? "MM/dd" : "LLL. yyyy");
		                		data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
		                		if (isDiffLessThan45Days) data.put("categories_time", MonthDay.parse(data.get("categories_time").toString(), categoryTimeFormat).format(DateTimeFormatter.ofPattern(obj.getLocale().equals("vi") ? "dd/MM" : "MM/dd", locale)));
		                		else data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
		                		break;
							case 5: // 7 days
								fullTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								categoryTimeFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
								data.put("time_full", LocalDate.parse(data.get("time_full").toString(), fullTimeFormat).format(dateFormat));
								data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(categoryTimeFormat.withLocale(locale)));
								break;
							case 6: // 1 month
								categoryTimeFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
								dateFormat = categoryTimeFormat.withLocale(locale);
								data.put("categories_time", YearMonth.parse(data.get("categories_time").toString(), categoryTimeFormat).format(dateFormat));
								break;
						}
						break;
				}
			}
		} catch (Exception e) {
		}
		
		return dataList;
	}
	
	
	/**
	 * @description get list device parameter
	 * @author long.pham
	 * @since 2022-02-22
	 * @param arr
	 */
	

	public List getChartParameterDevice(DeviceEntity obj) {
		try {
			List dataDevice = obj.getDataDevice();
			DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
			DateTimeFormatter isoDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						
			if(dataDevice.size() > 0) {
				List<CompletableFuture<Map<String, Object>>> list = new ArrayList<CompletableFuture<Map<String, Object>>>();
				
				LocalDateTime startDate = LocalDateTime.parse(obj.getStart_date(), inputDateFormat).withHour(0).withMinute(0).withSecond(0);
				LocalDateTime endDate = LocalDateTime.parse(obj.getEnd_date(), inputDateFormat).withHour(23).withMinute(59).withSecond(59);
				long diff5Days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
				
				for(int i = 0; i < dataDevice.size(); i++) {
					int k = i;
					
					CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
						Map<String, Object> maps = new HashMap<>();
						
						try {
							Map<String, Object> map = (Map<String, Object>) dataDevice.get(k);
							
							map.put("filterEnabled", obj.isFilterEnabled());
							map.put("filterBy", obj.getFilterBy());
							map.put("start_date", startDate.format(isoDateFormat));
							map.put("end_date", endDate.format(isoDateFormat));
							map.put("diff5Days", diff5Days <= 5 && diff5Days > 0);
							map.put("data_send_time", obj.getData_send_time());
							
							// get list of time to exclude data from
							List hiddenDataList = queryForList("SitesAnalytics.getHiddenDataListByDevice", map);
							map.put("hidden_data_list", hiddenDataList);
							// if device is virtual device, use table_data_virtual
							if ((int) map.get("id_device_type") == 12 && (int) map.get("id_device_group") != 81) map.put("datatablename", map.get("table_data_virtual"));
							// if data is more than 3 months, use view_tablename, else use datatablename
							else map.put("datatablename", map.get(startDate.isBefore(LocalDateTime.now().minusMonths(3)) ? "datatablename" : "view_tablename"));
							
							List<Map<String, Object>> getDataChartParameter = queryForList("SitesAnalytics.getDataChartParameter", map);
							
							maps.put("id", map.get("id"));
							maps.put("device_name", map.get("name"));
							maps.put("id_device_group", map.get("id_device_group"));
							maps.put("id_device_type", map.get("id_device_type"));
							maps.put("order", map.get("order"));
							maps.put("data", convertDateTimeFormat(obj, fulfillData(getDateTimeList(obj, startDate, endDate), getDataChartParameter), startDate, endDate));
						} catch (Exception ex) {
							log.error("getChartParameterDevice", ex);
						}
						
						return maps;
					});
					
					list.add(future);
				}
				
				return list.stream().map(future -> future.join()).collect(Collectors.toList());
			}
		} catch (Exception ex) {
		}
		return new ArrayList();
	}
	
	/**
	 * @description Get recently filter list
	 * @author Hung.Bui
	 * @since 2024-06-07
	 * @param obj { id_employee, hash_id_site }
	 * @return
	 */
	public List<EmployeeChartFilterEntity> getListFilter(EmployeeChartFilterEntity obj) {
		try {
			List<EmployeeChartFilterEntity> dataList = queryForList("SitesAnalytics.getListFilter", obj);
			if (dataList == null) return new ArrayList<EmployeeChartFilterEntity>();
			return dataList;
		} catch (Exception ex) {
			return new ArrayList<EmployeeChartFilterEntity>();
		}
	}
	
	/**
	 * @description save filter
	 * @author Hung.Bui
	 * @since 2024-06-07
	 * @param obj { id_employee, hash_id_site, params, created_date, name, is_favorite }
	 * @return
	 */
	public EmployeeChartFilterEntity saveFilter(EmployeeChartFilterEntity obj) {
		try {
			Integer insertId = (Integer) insert("SitesAnalytics.saveFilter", obj);
			if (insertId != null && insertId <= 0) return null;
			Integer total = (Integer) queryForObject("SitesAnalytics.getFiltersCount", obj);
			if(total > 10) delete("SitesAnalytics.deleteFilter", obj);
			
			return obj;
		} catch (Exception ex) {
			log.error("insert", ex);
			return null;
		}
	}
	

	public void sendCustomReport(SitesAnalyticsReportEntity obj) {

	}
}
