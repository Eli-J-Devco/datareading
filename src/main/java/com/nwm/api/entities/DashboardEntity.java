/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

import java.util.List;

public class DashboardEntity extends SortEntity {
	private int id;
	private int id_device;
	private int id_error;
	private String start_date;
	private String end_date;
	private String asset;
	private Double capacity;
	private int status;
	private String created_date;
	private String created_by;
	private String updated_date;
	private String updated_by;
	private int is_delete;
	private int id_customer;
	private int id_site;
	private int limit;
	private int offset;
	private int totalRecord;
	private String offset_timezone;
	private String localization_format;
	private String format_sql_short;
	private String format_sql_long;
	private String format_sql_string_short;
	private String format_sql_string_long;
	private String current_time;
	private List id_sites;
	private int priority;
	private int id_error_level;
	private List id_levels;
	private List id_types; 
	private List id_status;
	private String site_name;
	private String date_from;
	private String date_to;
	private String hash_id;
	private String level;
	private String view_history;
	private String customer_type;
	private int is_technical;
	private int screen_mode;
	private int alert_acknowledged;
	private int disable_notification;
	private int resolved;
	private String user_history;
	private String times_ago;
	private String times_ago_unit;
	private int highPriority;
	private int lowPriority;
	private int totalError;
	private double generationNow;
	private int totalSite;
	private double ratedDCCapacity;
	private String domain;
private String domain_role;
	
	
	public String getDomain_role() {
		return domain_role;
	}
	public void setDomain_role(String domain_role) {
		this.domain_role = domain_role;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
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
	public int getId_error() {
		return id_error;
	}
	public void setId_error(int id_error) {
		this.id_error = id_error;
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
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public Double getCapacity() {
		return capacity;
	}
	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public int getId_customer() {
		return id_customer;
	}
	public void setId_customer(int id_customer) {
		this.id_customer = id_customer;
	}
	public int getId_site() {
		return id_site;
	}
	public void setId_site(int id_site) {
		this.id_site = id_site;
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
	public String getOffset_timezone() {
		return offset_timezone;
	}
	public void setOffset_timezone(String offset_timezone) {
		this.offset_timezone = offset_timezone;
	}
	public String getLocalization_format() {
		return localization_format;
	}
	public void setLocalization_format(String localization_format) {
		this.localization_format = localization_format;
	}
	public String getFormat_sql_short() {
		return format_sql_short;
	}
	public void setFormat_sql_short(String format_sql_short) {
		this.format_sql_short = format_sql_short;
	}
	public String getFormat_sql_long() {
		return format_sql_long;
	}
	public void setFormat_sql_long(String format_sql_long) {
		this.format_sql_long = format_sql_long;
	}
	public String getFormat_sql_string_short() {
		return format_sql_string_short;
	}
	public void setFormat_sql_string_short(String format_sql_string_short) {
		this.format_sql_string_short = format_sql_string_short;
	}
	public String getFormat_sql_string_long() {
		return format_sql_string_long;
	}
	public void setFormat_sql_string_long(String format_sql_string_long) {
		this.format_sql_string_long = format_sql_string_long;
	}
	public String getCurrent_time() {
		return current_time;
	}
	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}
	public List getId_sites() {
		return id_sites;
	}
	public void setId_sites(List id_sites) {
		this.id_sites = id_sites;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getId_error_level() {
		return id_error_level;
	}
	public void setId_error_level(int id_error_level) {
		this.id_error_level = id_error_level;
	}
	public List getId_levels() {
		return id_levels;
	}
	public void setId_levels(List id_levels) {
		this.id_levels = id_levels;
	}
	public List getId_types() {
		return id_types;
	}
	public void setId_types(List id_types) {
		this.id_types = id_types;
	}
	public List getId_status() {
		return id_status;
	}
	public void setId_status(List id_status) {
		this.id_status = id_status;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public String getDate_from() {
		return date_from;
	}
	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}
	public String getDate_to() {
		return date_to;
	}
	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}
	public String getHash_id() {
		return hash_id;
	}
	public void setHash_id(String hash_id) {
		this.hash_id = hash_id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getView_history() {
		return view_history;
	}
	public void setView_history(String view_history) {
		this.view_history = view_history;
	}
	public String getCustomer_type() {
		return customer_type;
	}
	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	public int getIs_technical() {
		return is_technical;
	}
	public void setIs_technical(int is_technical) {
		this.is_technical = is_technical;
	}
	public int getScreen_mode() {
		return screen_mode;
	}
	public void setScreen_mode(int screen_mode) {
		this.screen_mode = screen_mode;
	}
	public int getAlert_acknowledged() {
		return alert_acknowledged;
	}
	public void setAlert_acknowledged(int alert_acknowledged) {
		this.alert_acknowledged = alert_acknowledged;
	}
	public int getDisable_notification() {
		return disable_notification;
	}
	public void setDisable_notification(int disable_notification) {
		this.disable_notification = disable_notification;
	}
	public int getResolved() {
		return resolved;
	}
	public void setResolved(int resolved) {
		this.resolved = resolved;
	}
	public String getUser_history() {
		return user_history;
	}
	public void setUser_history(String user_history) {
		this.user_history = user_history;
	}
	public String getTimes_ago() {
		return times_ago;
	}
	public void setTimes_ago(String times_ago) {
		this.times_ago = times_ago;
	}
	public String getTimes_ago_unit() {
		return times_ago_unit;
	}
	public void setTimes_ago_unit(String times_ago_unit) {
		this.times_ago_unit = times_ago_unit;
	}
	public int getHighPriority() {
		return highPriority;
	}
	public void setHighPriority(int highPriority) {
		this.highPriority = highPriority;
	}
	public int getLowPriority() {
		return lowPriority;
	}
	public void setLowPriority(int lowPriority) {
		this.lowPriority = lowPriority;
	}
	public int getTotalError() {
		return totalError;
	}
	public void setTotalError(int totalError) {
		this.totalError = totalError;
	}
	public double getGenerationNow() {
		return generationNow;
	}
	public void setGenerationNow(double generationNow) {
		this.generationNow = generationNow;
	}
	public int getTotalSite() {
		return totalSite;
	}
	public void setTotalSite(int totalSite) {
		this.totalSite = totalSite;
	}
	public double getRatedDCCapacity() {
		return ratedDCCapacity;
	}
	public void setRatedDCCapacity(double ratedDCCapacity) {
		this.ratedDCCapacity = ratedDCCapacity;
	}
	
	
	
}	
