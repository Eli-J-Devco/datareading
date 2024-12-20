/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

public class ModelGasMeterEntity {
	private String time;
	private int id_device;
	private int error;
	private int low_alarm;
	private int high_alarm;
	private double ReadingValue;
	private double nvmActivePower;
	private double nvmActiveEnergy;
	private double MeasuredProduction;
	private String datatablename;
	private String view_tablename;
	private String job_tablename;
	
	

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

	public double getMeasuredProduction() {
		return MeasuredProduction;
	}

	public void setMeasuredProduction(double measuredProduction) {
		MeasuredProduction = measuredProduction;
	}

	public String getDatatablename() {
		return datatablename;
	}

	public void setDatatablename(String datatablename) {
		this.datatablename = datatablename;
	}

	public String getView_tablename() {
		return view_tablename;
	}

	public void setView_tablename(String view_tablename) {
		this.view_tablename = view_tablename;
	}

	public String getJob_tablename() {
		return job_tablename;
	}

	public void setJob_tablename(String job_tablename) {
		this.job_tablename = job_tablename;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId_device() {
		return id_device;
	}

	public void setId_device(int id_device) {
		this.id_device = id_device;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public int getLow_alarm() {
		return low_alarm;
	}

	public void setLow_alarm(int low_alarm) {
		this.low_alarm = low_alarm;
	}

	public int getHigh_alarm() {
		return high_alarm;
	}

	public void setHigh_alarm(int high_alarm) {
		this.high_alarm = high_alarm;
	}

	public double getReadingValue() {
		return ReadingValue;
	}

	public void setReadingValue(double readingValue) {
		ReadingValue = readingValue;
	}
	

}
