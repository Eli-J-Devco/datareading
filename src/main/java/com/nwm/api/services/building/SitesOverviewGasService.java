/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services.building;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.DeviceEntity;
import com.nwm.api.entities.building.ActualVsPredictedConsumptionEntity;
import com.nwm.api.entities.building.ChartConsumptionEntity;
import com.nwm.api.entities.building.SitesOverviewGasConsumptionEntity;
import com.nwm.api.entities.building.SitesOverviewGasEntity;
import com.nwm.api.entities.building.SitesOverviewGasEventEntity;
import com.nwm.api.entities.building.SitesOverviewGasSummaryEntity;
import com.nwm.api.utils.Lib;

public class SitesOverviewGasService extends DB {
	
	/**
	 * @description create date time list
	 * @author Hung.Bui
	 * @since 2025-02-17
	 * @param obj device object
	 * @param start start date time
	 * @param end end date time
	 * @return
	 */
	private List<ChartConsumptionEntity> getDateTimeList(SitesOverviewGasEntity obj) {
		List<ChartConsumptionEntity> dateTimeList = new ArrayList<ChartConsumptionEntity>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime start = LocalDateTime.parse(obj.getStart_date(), formatter).withHour(0).withMinute(0).withSecond(0);
		LocalDateTime end = LocalDateTime.parse(obj.getEnd_date(), formatter).withHour(23).withMinute(59).withSecond(59);
		
		try {
			DateTimeFormatter timefullFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:00");
			DateTimeFormatter categoryTimeFormat = DateTimeFormatter.ofPattern("HH:00");
			ChronoUnit timeUnit = ChronoUnit.HOURS;
		
			switch (obj.getId_filter()) {
				case "today":
				default:
					break;
				case "this_week":
					end = end.with(DayOfWeek.SUNDAY);
					timefullFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL");
					timeUnit = ChronoUnit.DAYS;
					break;
				case "this_month":
				case "last_month":
					end = end.with(TemporalAdjusters.lastDayOfMonth());
					timefullFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					categoryTimeFormat = DateTimeFormatter.ofPattern("dd. LLL");
					timeUnit = ChronoUnit.DAYS;
					break;
				case "this_year":
				case "12_month":
				case "lifetime":
					end = end.with(TemporalAdjusters.lastDayOfMonth());
					timefullFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
					categoryTimeFormat = DateTimeFormatter.ofPattern("LLL. yyyy");
					timeUnit = ChronoUnit.MONTHS;
					break;
			}
			
			while (!start.isAfter(end)) {
				ChartConsumptionEntity dateTime = new ChartConsumptionEntity();
				dateTime.setTime_full(start.format(timefullFormat));
				dateTime.setCategories_time(start.format(categoryTimeFormat));
				dateTimeList.add(dateTime);
				start = start.plus(1, timeUnit);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return dateTimeList;
	}
	
	/**
	 * @description Get consumption
	 * @author Hung.Bui
	 * @since 2025-02-17
	 * @param SitesOverviewGasEntity
	 * @return SitesOverviewGasConsumptionEntity
	 */
	public SitesOverviewGasConsumptionEntity getConsumption(SitesOverviewGasEntity obj) {
		SitesOverviewGasConsumptionEntity entity = new SitesOverviewGasConsumptionEntity();
		
		try {
			List<DeviceEntity> devices = this.getGasMeters(obj);
			if (devices.size() == 0) return entity;
			obj.setDevices(devices);
			
			List<ChartConsumptionEntity> dataList = queryForList("SitesOverviewGas.getConsumption", obj);
			List<ChartConsumptionEntity> fulfillData = Lib.fulfillData(getDateTimeList(obj), dataList, "time_full");
			if (fulfillData.size() > 0) {
				entity.setData(fulfillData);
				OptionalDouble avg = fulfillData.stream().filter(item -> item.getValue() != null).mapToDouble(item -> item.getValue()).average();
				entity.setAverage(avg.isPresent() ? new BigDecimal(avg.getAsDouble()).setScale(0, RoundingMode.HALF_UP).doubleValue() : null);
			}
		} catch (Exception ex) {
			log.error("SitesOverviewGas.getConsumption", ex);
		}
		
		return entity;
	}
	
	/**
	 * @description Get gas meters
	 * @author Hung.Bui
	 * @since 2025-02-17
	 * @param SitesOverviewGasEntity
	 * @return List<DeviceEntity>
	 */
	public List<DeviceEntity> getGasMeters(SitesOverviewGasEntity obj) {
		try {
			List<DeviceEntity> dataList = queryForList("SitesOverviewGas.getGasMeters", obj);
			if (dataList != null && dataList.size() > 0) return dataList;
		} catch (Exception ex) {
			log.error("SitesOverviewGas.getGasMeters", ex);
		}
		return new ArrayList<DeviceEntity>();
	}
	
	/**
	 * @description Get summary
	 * @author Hung.Bui
	 * @since 2025-02-19
	 * @param SitesOverviewGasEntity
	 * @return List
	 */
	public Map<String, SitesOverviewGasSummaryEntity> getSummary(SitesOverviewGasEntity obj) {
		Map<String, SitesOverviewGasSummaryEntity> map = new HashMap<String, SitesOverviewGasSummaryEntity>();
		
		try {
			List<DeviceEntity> devices = this.getGasMeters(obj);
			if (devices.size() == 0) return map;
			obj.setDevices(devices);
			
			List<ChartConsumptionEntity> dataList = queryForList("SitesOverviewGas.getSummary", obj);
			
			Double currentMonth = dataList.get(0).getValue();
			Double lastMonth = dataList.get(1).getValue();
			Double monthBeforeLastMonth = dataList.get(2).getValue();
			Double currentYear = dataList.get(3).getValue();
			Double lastYear = dataList.get(4).getValue();
			Double life_time = dataList.get(5).getValue();
			
			ZonedDateTime now = ZonedDateTime.now(ZoneId.of(obj.getTimezone_value()));
			Double dailyUsage = currentMonth / now.getDayOfMonth();
			Double lastDailyUsage = lastMonth / now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
			map.put("current_month", new SitesOverviewGasSummaryEntity(currentMonth, lastMonth == 0 ? (currentMonth == 0 ? 0 : 100) : new BigDecimal((currentMonth - lastMonth) / lastMonth * 100).setScale(1, RoundingMode.HALF_UP).doubleValue()));
			map.put("last_month", new SitesOverviewGasSummaryEntity(lastMonth, monthBeforeLastMonth == 0 ? (lastMonth == 0 ? 0 : 100) : new BigDecimal((lastMonth - monthBeforeLastMonth) / monthBeforeLastMonth * 100).setScale(1, RoundingMode.HALF_UP).doubleValue()));
			map.put("daily_usage", new SitesOverviewGasSummaryEntity(dailyUsage, lastDailyUsage == 0 ? (dailyUsage == 0 ? 0 : 100) : new BigDecimal((dailyUsage - lastDailyUsage) / lastDailyUsage * 100).setScale(1, RoundingMode.HALF_UP).doubleValue()));
			map.put("yearly_usage", new SitesOverviewGasSummaryEntity(currentYear, lastYear == 0 ? (currentYear == 0 ? 0 : 100) : new BigDecimal((currentYear - lastYear) / lastYear * 100).setScale(1, RoundingMode.HALF_UP).doubleValue()));
			map.put("lifetime", new SitesOverviewGasSummaryEntity(life_time, 0.0));
		} catch (Exception ex) {
			log.error("SitesOverviewGas.getSummary", ex);
		}
		
		return map;
	}
	
	/**
	 * @description Get alerts
	 * @author Hung.Bui
	 * @since 2025-02-20
	 * @param List<SitesOverviewGasEventEntity>
	 * @return List
	 */
	public List<SitesOverviewGasEventEntity> getEvents(SitesOverviewGasEntity obj) {
		List<SitesOverviewGasEventEntity> dataList = new ArrayList<SitesOverviewGasEventEntity>();
		
		try {
			List<DeviceEntity> devices = this.getGasMeters(obj);
			if (devices.size() == 0) return dataList;
			obj.setDevices(devices);
			
			dataList = queryForList("SitesOverviewGas.getEvents", obj);
		} catch (Exception ex) {
			log.error("SitesOverviewGas.getEvents", ex);
		}
		
		return dataList;
	}
	
	/**
	 * @description Get actual vs predicted consumption
	 * @author Hung.Bui
	 * @since 2025-03-12
	 * @param List<ActualVsPredictedConsumptionEntity>
	 * @return List
	 */
	public List<ActualVsPredictedConsumptionEntity> getActualVsPredicted(SitesOverviewGasEntity obj) {
		try {
			List<DeviceEntity> devices = this.getGasMeters(obj);
			if (devices.size() == 0) return new ArrayList<ActualVsPredictedConsumptionEntity>();
			List<CompletableFuture<ActualVsPredictedConsumptionEntity>> futureList = new ArrayList<CompletableFuture<ActualVsPredictedConsumptionEntity>>();
			
			String[] labels = new String[] {"actual", "predicted"};
			for (String label : labels) {
				CompletableFuture<ActualVsPredictedConsumptionEntity> future = CompletableFuture.supplyAsync(() -> {
					ActualVsPredictedConsumptionEntity entity = new ActualVsPredictedConsumptionEntity();
					
					try {
						SitesOverviewGasEntity params = new SitesOverviewGasEntity();
						params.setId_filter(obj.getId_filter());
						params.setStart_date(obj.getStart_date());
						params.setEnd_date(obj.getEnd_date());
						params.setTimezone_value(obj.getTimezone_value());
						params.setReading_field(obj.getReading_field());
						params.setDevices(devices);
						
						if (label.equals("predicted")) {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							LocalDateTime start = LocalDateTime.parse(obj.getStart_date(), formatter);
							LocalDateTime end = LocalDateTime.parse(obj.getEnd_date(), formatter);
							
							params.setStart_date(obj.getId_filter().equals("this_year") ? start.minusYears(1).format(formatter) : start.minusMonths(1).format(formatter));
							params.setEnd_date(obj.getId_filter().equals("this_year") ? end.minusYears(1).with(TemporalAdjusters.lastDayOfYear()).format(formatter) : end.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).format(formatter));
						}
						
						entity.setId(label);
						List<ChartConsumptionEntity> dataList = queryForList("SitesOverviewGas.getConsumption", params);
						entity.setData(Lib.fulfillData(getDateTimeList(params), dataList, "time_full"));
					} catch (Exception ex) {
						log.error("SitesOverviewGas.getActualVsPredicted", ex);
					}
					
					return entity;
				});
				
				futureList.add(future);
			}

			List<ActualVsPredictedConsumptionEntity> dataList = futureList.stream().map(future -> future.join()).collect(Collectors.toList());
			dataList.sort((s1, s2) -> s1.getId().equals("actual") ? -1 : 1);
			
			return dataList;
		} catch (Exception ex) {
			log.error("SitesOverviewGas.getActualVsPredicted", ex);
		}
		
		return new ArrayList<ActualVsPredictedConsumptionEntity>();
	}
}
