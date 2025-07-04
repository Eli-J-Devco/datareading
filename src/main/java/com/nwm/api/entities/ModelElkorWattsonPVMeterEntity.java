/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

public class ModelElkorWattsonPVMeterEntity extends ModelBaseEntity {
	private double TotalEnergyConsumption;
	private double TotalRealPower;
	private double TotalReactivePower;
	private double TotalApparentPower;
	private double AverageVoltageLN;
	private double AverageVoltageLL;
	private double AverageCurrent;
	private double TotalSystemPowerFactor;
	private double Frequency;
	private double SlidingWindowRealPowerDemand;
	private double VoltageAN;
	private double VoltageBN;
	private double VoltageCN;
	private double VoltageAB;
	private double VoltageBC;
	private double VoltageAC;
	private double CurrentA;
	private double CurrentB;
	private double CurrentC;
	private double RealPowerA;
	private double RealPowerB;
	private double RealPowerC;
	private double ReactivePowerA;
	private double ReactivePowerB;
	private double ReactivePowerC;
	private double ApparentPowerA;
	private double ApparentPowerB;
	private double ApparentPowerC;
	private double PowerFactorA;
	private double PowerFactorB;
	private double PowerFactorC;
	
	public double getTotalEnergyConsumption() {
		return TotalEnergyConsumption;
	}
	public void setTotalEnergyConsumption(double totalEnergyConsumption) {
		TotalEnergyConsumption = totalEnergyConsumption;
	}
	public double getTotalRealPower() {
		return TotalRealPower;
	}
	public void setTotalRealPower(double totalRealPower) {
		TotalRealPower = totalRealPower;
	}
	public double getTotalReactivePower() {
		return TotalReactivePower;
	}
	public void setTotalReactivePower(double totalReactivePower) {
		TotalReactivePower = totalReactivePower;
	}
	public double getTotalApparentPower() {
		return TotalApparentPower;
	}
	public void setTotalApparentPower(double totalApparentPower) {
		TotalApparentPower = totalApparentPower;
	}
	public double getAverageVoltageLN() {
		return AverageVoltageLN;
	}
	public void setAverageVoltageLN(double averageVoltageLN) {
		AverageVoltageLN = averageVoltageLN;
	}
	public double getAverageVoltageLL() {
		return AverageVoltageLL;
	}
	public void setAverageVoltageLL(double averageVoltageLL) {
		AverageVoltageLL = averageVoltageLL;
	}
	public double getAverageCurrent() {
		return AverageCurrent;
	}
	public void setAverageCurrent(double averageCurrent) {
		AverageCurrent = averageCurrent;
	}
	public double getTotalSystemPowerFactor() {
		return TotalSystemPowerFactor;
	}
	public void setTotalSystemPowerFactor(double totalSystemPowerFactor) {
		TotalSystemPowerFactor = totalSystemPowerFactor;
	}
	public double getFrequency() {
		return Frequency;
	}
	public void setFrequency(double frequency) {
		Frequency = frequency;
	}
	public double getSlidingWindowRealPowerDemand() {
		return SlidingWindowRealPowerDemand;
	}
	public void setSlidingWindowRealPowerDemand(double slidingWindowRealPowerDemand) {
		SlidingWindowRealPowerDemand = slidingWindowRealPowerDemand;
	}
	public double getVoltageAN() {
		return VoltageAN;
	}
	public void setVoltageAN(double voltageAN) {
		VoltageAN = voltageAN;
	}
	public double getVoltageBN() {
		return VoltageBN;
	}
	public void setVoltageBN(double voltageBN) {
		VoltageBN = voltageBN;
	}
	public double getVoltageCN() {
		return VoltageCN;
	}
	public void setVoltageCN(double voltageCN) {
		VoltageCN = voltageCN;
	}
	public double getVoltageAB() {
		return VoltageAB;
	}
	public void setVoltageAB(double voltageAB) {
		VoltageAB = voltageAB;
	}
	public double getVoltageBC() {
		return VoltageBC;
	}
	public void setVoltageBC(double voltageBC) {
		VoltageBC = voltageBC;
	}
	public double getVoltageAC() {
		return VoltageAC;
	}
	public void setVoltageAC(double voltageAC) {
		VoltageAC = voltageAC;
	}
	public double getCurrentA() {
		return CurrentA;
	}
	public void setCurrentA(double currentA) {
		CurrentA = currentA;
	}
	public double getCurrentB() {
		return CurrentB;
	}
	public void setCurrentB(double currentB) {
		CurrentB = currentB;
	}
	public double getCurrentC() {
		return CurrentC;
	}
	public void setCurrentC(double currentC) {
		CurrentC = currentC;
	}
	public double getRealPowerA() {
		return RealPowerA;
	}
	public void setRealPowerA(double realPowerA) {
		RealPowerA = realPowerA;
	}
	public double getRealPowerB() {
		return RealPowerB;
	}
	public void setRealPowerB(double realPowerB) {
		RealPowerB = realPowerB;
	}
	public double getRealPowerC() {
		return RealPowerC;
	}
	public void setRealPowerC(double realPowerC) {
		RealPowerC = realPowerC;
	}
	public double getReactivePowerA() {
		return ReactivePowerA;
	}
	public void setReactivePowerA(double reactivePowerA) {
		ReactivePowerA = reactivePowerA;
	}
	public double getReactivePowerB() {
		return ReactivePowerB;
	}
	public void setReactivePowerB(double reactivePowerB) {
		ReactivePowerB = reactivePowerB;
	}
	public double getReactivePowerC() {
		return ReactivePowerC;
	}
	public void setReactivePowerC(double reactivePowerC) {
		ReactivePowerC = reactivePowerC;
	}
	public double getApparentPowerA() {
		return ApparentPowerA;
	}
	public void setApparentPowerA(double apparentPowerA) {
		ApparentPowerA = apparentPowerA;
	}
	public double getApparentPowerB() {
		return ApparentPowerB;
	}
	public void setApparentPowerB(double apparentPowerB) {
		ApparentPowerB = apparentPowerB;
	}
	public double getApparentPowerC() {
		return ApparentPowerC;
	}
	public void setApparentPowerC(double apparentPowerC) {
		ApparentPowerC = apparentPowerC;
	}
	public double getPowerFactorA() {
		return PowerFactorA;
	}
	public void setPowerFactorA(double powerFactorA) {
		PowerFactorA = powerFactorA;
	}
	public double getPowerFactorB() {
		return PowerFactorB;
	}
	public void setPowerFactorB(double powerFactorB) {
		PowerFactorB = powerFactorB;
	}
	public double getPowerFactorC() {
		return PowerFactorC;
	}
	public void setPowerFactorC(double powerFactorC) {
		PowerFactorC = powerFactorC;
	}
	
	
}
