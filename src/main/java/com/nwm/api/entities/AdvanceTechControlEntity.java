/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;


public class AdvanceTechControlEntity {
	private int id_site;
	private int id_device;	
	private String advance_tech_pass;
	private String advance_tech_host;
	private String advance_tech_status;
	private String hash_id;
	private int id_employee;
	private String device_type;
	private String status_type;
	private String advance_tech_field_restart;
	private String advance_tech_field_stop;
	private String advance_tech_field_start;
	
	private String advance_tech_value_restart;
	private String advance_tech_value_stop;
	private String advance_tech_value_start;
	private String serial_number;
	
	
	
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public String getAdvance_tech_value_restart() {
		return advance_tech_value_restart;
	}
	public void setAdvance_tech_value_restart(String advance_tech_value_restart) {
		this.advance_tech_value_restart = advance_tech_value_restart;
	}
	public String getAdvance_tech_value_stop() {
		return advance_tech_value_stop;
	}
	public void setAdvance_tech_value_stop(String advance_tech_value_stop) {
		this.advance_tech_value_stop = advance_tech_value_stop;
	}
	public String getAdvance_tech_value_start() {
		return advance_tech_value_start;
	}
	public void setAdvance_tech_value_start(String advance_tech_value_start) {
		this.advance_tech_value_start = advance_tech_value_start;
	}
	public String getAdvance_tech_field_restart() {
		return advance_tech_field_restart;
	}
	public void setAdvance_tech_field_restart(String advance_tech_field_restart) {
		this.advance_tech_field_restart = advance_tech_field_restart;
	}
	public String getAdvance_tech_field_stop() {
		return advance_tech_field_stop;
	}
	public void setAdvance_tech_field_stop(String advance_tech_field_stop) {
		this.advance_tech_field_stop = advance_tech_field_stop;
	}
	public String getAdvance_tech_field_start() {
		return advance_tech_field_start;
	}
	public void setAdvance_tech_field_start(String advance_tech_field_start) {
		this.advance_tech_field_start = advance_tech_field_start;
	}
	public int getId_site() {
		return id_site;
	}
	public void setId_site(int id_site) {
		this.id_site = id_site;
	}
	public int getId_device() {
		return id_device;
	}
	public void setId_device(int id_device) {
		this.id_device = id_device;
	}
	public String getAdvance_tech_pass() {
		return advance_tech_pass;
	}
	public void setAdvance_tech_pass(String advance_tech_pass) {
		this.advance_tech_pass = advance_tech_pass;
	}
	public String getAdvance_tech_host() {
		return advance_tech_host;
	}
	public void setAdvance_tech_host(String advance_tech_host) {
		this.advance_tech_host = advance_tech_host;
	}
	public String getAdvance_tech_status() {
		return advance_tech_status;
	}
	public void setAdvance_tech_status(String advance_tech_status) {
		this.advance_tech_status = advance_tech_status;
	}
	public String getHash_id() {
		return hash_id;
	}
	public void setHash_id(String hash_id) {
		this.hash_id = hash_id;
	}
	public int getId_employee() {
		return id_employee;
	}
	public void setId_employee(int id_employee) {
		this.id_employee = id_employee;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getStatus_type() {
		return status_type;
	}
	public void setStatus_type(String status_type) {
		this.status_type = status_type;
	}
	
	
	
	
}
