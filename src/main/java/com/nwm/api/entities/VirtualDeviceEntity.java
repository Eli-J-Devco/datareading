/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

import java.util.List;

public class VirtualDeviceEntity {
	private String time;
	private int id;
	private int id_device;
	private int id_site;
	private double nvmActivePower;
	private double nvmActiveEnergy;
	private int id_device_type;
	private int id_device_group;
	private String datatablename;
	private String devicename;
	private int id_time_zone;
	private int timezone_datalogger;
	private String time_zone_value;
	private String display_timezone;
	private String virtual_device_type;
	private int data_send_time;
	private int level_sent_time = 5; // 5 minutes ~ 300, 15 minutes ~ 900, 1 hour
	private int data_inverval; // 5 minutes ~ 5, 15 minutes ~ 15, 1 hour ~ 60
	private List devices;
	private String name;
	private String start_date;
	private String end_date;
	private String built_since;
	private String commissioning;
	private List weathers;
	private double nvm_temperature;
	private double nvm_irradiance;
	private double dc_capacity = 0;
	private double ac_capacity = 0;
	private double expected_power_ac;
	
	private double pv_module_temperature_coeff;
	private double global_solar_irradiance_at_stc;
	private double stc_temperature;
	private double inverter_efficiency;
	private double t_avg;
	private double system_loss;
	private double pv_model;
	private double min_irradiance_limit;
	private double clip;
	private double bifaciality_factor;
	private double annual_pv_module_degradation;
	private double cable_losses;
	private double transformer_losses;
	private double soiling;
	private double other_losses;
	private int reverse_poa = 0;
	
	private List weatherRPOA;
	private List sensorAmbientTemp;
	private List weatherPOA;
	
	private String ids_device_poa = null;
	private String ids_device_rpoa = null;
	private String ids_device_panel_temp = null;
	private String ids_device_ambient_temp = null;
	
	private List ids;
	private String table_data_virtual;
	
	
	
	public double getExpected_power_ac() {
		return expected_power_ac;
	}
	public void setExpected_power_ac(double expected_power_ac) {
		this.expected_power_ac = expected_power_ac;
	}
	public String getBuilt_since() {
		return built_since;
	}
	public void setBuilt_since(String built_since) {
		this.built_since = built_since;
	}
	public String getCommissioning() {
		return commissioning;
	}
	public void setCommissioning(String commissioning) {
		this.commissioning = commissioning;
	}
	public double getAnnual_pv_module_degradation() {
		return annual_pv_module_degradation;
	}
	public void setAnnual_pv_module_degradation(double annual_pv_module_degradation) {
		this.annual_pv_module_degradation = annual_pv_module_degradation;
	}
	public double getCable_losses() {
		return cable_losses;
	}
	public void setCable_losses(double cable_losses) {
		this.cable_losses = cable_losses;
	}
	public double getTransformer_losses() {
		return transformer_losses;
	}
	public void setTransformer_losses(double transformer_losses) {
		this.transformer_losses = transformer_losses;
	}
	public double getSoiling() {
		return soiling;
	}
	public void setSoiling(double soiling) {
		this.soiling = soiling;
	}
	public double getOther_losses() {
		return other_losses;
	}
	public void setOther_losses(double other_losses) {
		this.other_losses = other_losses;
	}
	public int getData_inverval() {
		return data_inverval;
	}
	public void setData_inverval(int data_inverval) {
		this.data_inverval = data_inverval;
	}
	public List getIds() {
		return ids;
	}
	public void setIds(List ids) {
		this.ids = ids;
	}
	public String getIds_device_poa() {
		return ids_device_poa;
	}
	public void setIds_device_poa(String ids_device_poa) {
		this.ids_device_poa = ids_device_poa;
	}
	public String getIds_device_rpoa() {
		return ids_device_rpoa;
	}
	public void setIds_device_rpoa(String ids_device_rpoa) {
		this.ids_device_rpoa = ids_device_rpoa;
	}
	public String getIds_device_panel_temp() {
		return ids_device_panel_temp;
	}
	public void setIds_device_panel_temp(String ids_device_panel_temp) {
		this.ids_device_panel_temp = ids_device_panel_temp;
	}
	public String getIds_device_ambient_temp() {
		return ids_device_ambient_temp;
	}
	public void setIds_device_ambient_temp(String ids_device_ambient_temp) {
		this.ids_device_ambient_temp = ids_device_ambient_temp;
	}
	public int getLevel_sent_time() {
		return level_sent_time;
	}
	public void setLevel_sent_time(int level_sent_time) {
		this.level_sent_time = level_sent_time;
	}
	public List getWeatherPOA() {
		return weatherPOA;
	}
	public void setWeatherPOA(List weatherPOA) {
		this.weatherPOA = weatherPOA;
	}
	public List getSensorAmbientTemp() {
		return sensorAmbientTemp;
	}
	public void setSensorAmbientTemp(List sensorAmbientTemp) {
		this.sensorAmbientTemp = sensorAmbientTemp;
	}
	public List getWeatherRPOA() {
		return weatherRPOA;
	}
	public void setWeatherRPOA(List weatherRPOA) {
		this.weatherRPOA = weatherRPOA;
	}
	public int getReverse_poa() {
		return reverse_poa;
	}
	public void setReverse_poa(int reverse_poa) {
		this.reverse_poa = reverse_poa;
	}
	public double getMin_irradiance_limit() {
		return min_irradiance_limit;
	}
	public void setMin_irradiance_limit(double min_irradiance_limit) {
		this.min_irradiance_limit = min_irradiance_limit;
	}
	public double getClip() {
		return clip;
	}
	public void setClip(double clip) {
		this.clip = clip;
	}
	public double getBifaciality_factor() {
		return bifaciality_factor;
	}
	public void setBifaciality_factor(double bifaciality_factor) {
		this.bifaciality_factor = bifaciality_factor;
	}
	public double getPv_module_temperature_coeff() {
		return pv_module_temperature_coeff;
	}
	public void setPv_module_temperature_coeff(double pv_module_temperature_coeff) {
		this.pv_module_temperature_coeff = pv_module_temperature_coeff;
	}
	public double getGlobal_solar_irradiance_at_stc() {
		return global_solar_irradiance_at_stc;
	}
	public void setGlobal_solar_irradiance_at_stc(double global_solar_irradiance_at_stc) {
		this.global_solar_irradiance_at_stc = global_solar_irradiance_at_stc;
	}
	public double getStc_temperature() {
		return stc_temperature;
	}
	public void setStc_temperature(double stc_temperature) {
		this.stc_temperature = stc_temperature;
	}
	public double getInverter_efficiency() {
		return inverter_efficiency;
	}
	public void setInverter_efficiency(double inverter_efficiency) {
		this.inverter_efficiency = inverter_efficiency;
	}
	public double getT_avg() {
		return t_avg;
	}
	public void setT_avg(double t_avg) {
		this.t_avg = t_avg;
	}
	public double getSystem_loss() {
		return system_loss;
	}
	public void setSystem_loss(double system_loss) {
		this.system_loss = system_loss;
	}
	public double getPv_model() {
		return pv_model;
	}
	public void setPv_model(double pv_model) {
		this.pv_model = pv_model;
	}
	public double getDc_capacity() {
		return dc_capacity;
	}
	public void setDc_capacity(double dc_capacity) {
		this.dc_capacity = dc_capacity;
	}
	public double getAc_capacity() {
		return ac_capacity;
	}
	public void setAc_capacity(double ac_capacity) {
		this.ac_capacity = ac_capacity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_device() {
		return id_device;
	}
	public void setId_device(int id_device) {
		this.id_device = id_device;
	}
	public int getId_site() {
		return id_site;
	}
	public void setId_site(int id_site) {
		this.id_site = id_site;
	}
	public double getNvmActivePower() {
		return nvmActivePower;
	}
	public void setNvmActivePower(double nvmActivePower) {
		this.nvmActivePower = nvmActivePower;
	}
	public double getNvmActiveEnergy() {
		return nvmActiveEnergy;
	}
	public void setNvmActiveEnergy(double nvmActiveEnergy) {
		this.nvmActiveEnergy = nvmActiveEnergy;
	}
	public int getId_device_type() {
		return id_device_type;
	}
	public void setId_device_type(int id_device_type) {
		this.id_device_type = id_device_type;
	}
	public int getId_device_group() {
		return id_device_group;
	}
	public void setId_device_group(int id_device_group) {
		this.id_device_group = id_device_group;
	}
	public String getDatatablename() {
		return datatablename;
	}
	public void setDatatablename(String datatablename) {
		this.datatablename = datatablename;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public int getId_time_zone() {
		return id_time_zone;
	}
	public void setId_time_zone(int id_time_zone) {
		this.id_time_zone = id_time_zone;
	}
	public int getTimezone_datalogger() {
		return timezone_datalogger;
	}
	public void setTimezone_datalogger(int timezone_datalogger) {
		this.timezone_datalogger = timezone_datalogger;
	}
	public String getTime_zone_value() {
		return time_zone_value;
	}
	public void setTime_zone_value(String time_zone_value) {
		this.time_zone_value = time_zone_value;
	}
	public String getDisplay_timezone() {
		return display_timezone;
	}
	public void setDisplay_timezone(String display_timezone) {
		this.display_timezone = display_timezone;
	}
	public String getVirtual_device_type() {
		return virtual_device_type;
	}
	public void setVirtual_device_type(String virtual_device_type) {
		this.virtual_device_type = virtual_device_type;
	}
	public int getData_send_time() {
		return data_send_time;
	}
	public void setData_send_time(int data_send_time) {
		this.data_send_time = data_send_time;
	}
	public List getDevices() {
		return devices;
	}
	public void setDevices(List devices) {
		this.devices = devices;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public List getWeathers() {
		return weathers;
	}
	public void setWeathers(List weathers) {
		this.weathers = weathers;
	}
	public double getNvm_temperature() {
		return nvm_temperature;
	}
	public void setNvm_temperature(double nvm_temperature) {
		this.nvm_temperature = nvm_temperature;
	}
	public double getNvm_irradiance() {
		return nvm_irradiance;
	}
	public void setNvm_irradiance(double nvm_irradiance) {
		this.nvm_irradiance = nvm_irradiance;
	}
	public String getTable_data_virtual() {
		return table_data_virtual;
	}
	public void setTable_data_virtual(String table_data_virtual) {
		this.table_data_virtual = table_data_virtual;
	}
	
	
	
}
