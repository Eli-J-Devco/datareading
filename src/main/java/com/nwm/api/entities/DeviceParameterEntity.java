/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

import java.util.List;

public class DeviceParameterEntity{
	
	private int id;
	private int id_device_group;
	private String name;
	private String description;
	private String unit;
	private int is_filter;
	private int status;
	private int is_delete;
	private String created_date;
	private String created_by;
	private String updated_date;
	private String updated_by;
	private String text;
	private String slug;
	private int id_device; 
	private String register;
	private String hide;
	private String standard_alert_message;
	private String address;
	private String value;
	private List id_devices;
	private String value_chart_tool;
	private int is_checked;
	private int menu_order;
	private int is_calculation;
	private int is_common;
	private String standard_name;
	private int limit;
	private int offset;
	private int totalRecord;
	private int screen_mode;
	private String keyword;
	private String order_by;
	private String sort_column;
	private int id_categorize_data;
	private String scale;
	private List deviceType;
	private String filterBy;
	private int writable;
	private int rounding_decimals;
	private int is_user_defined;
	private Integer id_generic_parameter;
	private boolean is_active_power;
	private boolean is_energy;
	private boolean is_irradiance;
	private boolean is_temperature;
	private boolean is_panel_temperature;
	private String title_trans;
	
	
	
	
	public String getTitle_trans() {
		return title_trans;
	}
	public void setTitle_trans(String title_trans) {
		this.title_trans = title_trans;
	}
	public boolean isIs_active_power() {
		return is_active_power;
	}
	public boolean isIs_energy() {
		return is_energy;
	}
	public boolean isIs_irradiance() {
		return is_irradiance;
	}
	public boolean isIs_temperature() {
		return is_temperature;
	}
	public boolean isIs_panel_temperature() {
		return is_panel_temperature;
	}
	public int getRounding_decimals() {
		return rounding_decimals;
	}
	public void setRounding_decimals(int rounding_decimals) {
		this.rounding_decimals = rounding_decimals;
	}
	public String getFilterBy() {
		return filterBy;
	}
	public void setFilterBy(String filterBy) {
		this.filterBy = filterBy;
	}
	public List getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(List deviceType) {
		this.deviceType = deviceType;
	}
	public int getMenu_order() {
		return menu_order;
	}
	public void setMenu_order(int menu_order) {
		this.menu_order = menu_order;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_device_group() {
		return id_device_group;
	}
	public void setId_device_group(int id_device_group) {
		this.id_device_group = id_device_group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getIs_filter() {
		return is_filter;
	}
	public void setIs_filter(int is_filter) {
		this.is_filter = is_filter;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public int getId_device() {
		return id_device;
	}
	public void setId_device(int id_device) {
		this.id_device = id_device;
	}
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	public String getHide() {
		return hide;
	}
	public void setHide(String hide) {
		this.hide = hide;
	}
	public String getStandard_alert_message() {
		return standard_alert_message;
	}
	public void setStandard_alert_message(String standard_alert_message) {
		this.standard_alert_message = standard_alert_message;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List getId_devices() {
		return id_devices;
	}
	public void setId_devices(List id_devices) {
		this.id_devices = id_devices;
	}
	public String getValue_chart_tool() {
		return value_chart_tool;
	}
	public void setValue_chart_tool(String value_chart_tool) {
		this.value_chart_tool = value_chart_tool;
	}
	public int getIs_checked() {
		return is_checked;
	}
	public void setIs_checked(int is_checked) {
		this.is_checked = is_checked;
	}
	public int getIs_calculation() {
		return is_calculation;
	}
	public void setIs_calculation(int is_calculation) {
		this.is_calculation = is_calculation;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOrder_by() {
		return order_by;
	}
	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}
	public String getSort_column() {
		return sort_column;
	}
	public void setSort_column(String sort_column) {
		this.sort_column = sort_column;
	}
	public String getStandard_name() {
		return standard_name;
	}
	public void setStandard_name(String standard_name) {
		this.standard_name = standard_name;
	}
	public int getScreen_mode() {
		return screen_mode;
	}
	public void setScreen_mode(int screen_mode) {
		this.screen_mode = screen_mode;
	}
	public int getIs_common() {
		return is_common;
	}
	public void setIs_common(int is_common) {
		this.is_common = is_common;
	}
	public int getId_categorize_data() {
		return id_categorize_data;
	}
	public void setId_categorize_data(int id_categorize_data) {
		this.id_categorize_data = id_categorize_data;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public int getWritable() {
		return writable;
	}
	public void setWritable(int writable) {
		this.writable = writable;
	}
	public int getIs_user_defined() {
		return is_user_defined;
	}
	public void setIs_user_defined(int is_user_defined) {
		this.is_user_defined = is_user_defined;
	}
	public Integer getId_generic_parameter() {
		return id_generic_parameter;
	}
	public void setId_generic_parameter(Integer id_generic_parameter) {
		this.id_generic_parameter = id_generic_parameter;
	}
	public boolean is_active_power() {
		return is_active_power;
	}
	public void setIs_active_power(boolean is_active_power) {
		this.is_active_power = is_active_power;
	}
	public boolean is_energy() {
		return is_energy;
	}
	public void setIs_energy(boolean is_energy) {
		this.is_energy = is_energy;
	}
	public boolean is_irradiance() {
		return is_irradiance;
	}
	public void setIs_irradiance(boolean is_irradiance) {
		this.is_irradiance = is_irradiance;
	}
	public boolean is_temperature() {
		return is_temperature;
	}
	public void setIs_temperature(boolean is_temperature) {
		this.is_temperature = is_temperature;
	}
	public boolean is_panel_temperature() {
		return is_panel_temperature;
	}
	public void setIs_panel_temperature(boolean is_panel_temperature) {
		this.is_panel_temperature = is_panel_temperature;
	}
	

}
