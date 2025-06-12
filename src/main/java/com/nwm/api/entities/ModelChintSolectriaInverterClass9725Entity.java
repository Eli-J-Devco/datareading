/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;

public class ModelChintSolectriaInverterClass9725Entity extends ModelBaseEntity {
	private double PowerOnOff;
	private double PActiveSet;
	private double PFactorSet;
	private double PReactiveSet;
	private double GridVMax;
	private double GridVmaxTripT;
	private double GridVMin;
	private double GridVminTripT;
	private double GridFMax;
	private double GridFMin;
	private double GridFTripT;
	private double ActivePower;
	private double PowerFactor;
	private double StartDelayTime;
	private double Risomin;
	private double PVStartVol;
	private double DCIMax;
	private double TambientMax;
	private double TmoduleMax;
	private double OffsetDiffMax;
	private double GridVolUnbalance;
	private double SoftPowerStep;
	private double TotalEnergyToEnergy;
	private double TotalEnergyToday;
	private double InverterEfficiency;
	private double PowerFactor1;
	private double MaxActivePowerToday;
	private double RunTimeToGrid;
	private double AC_ActivePower;
	private double AC_ApparentPower;
	private double GridVoltageUab;
	private double GridVoltageUbc;
	private double GridVoltageUca;
	private double GridA_PhaseCurrent;
	private double GridB_PhaseCurrent;
	private double GridC_PhaseCurrent;
	private double PV1_Voltage;
	private double PV1_Current;
	private double PV2_Voltage;
	private double PV2_Current;
	private double PV3_Voltage;
	private double PV3_Current;
	private double Grid_Frequency;
	private double ModuleTemp;
	private double InternalTemp;
	private double TransformerTemp;
	private double InverterModeCode;
	private double PermanentFaultCode;
	private double WarnCode;
	private double FaultCode0;
	private double FaultCode1;
	private double FaultCode2;
	private String SerialNumber;
	
	private int totalPermanentFaultCode;
	private int totalWarnCode;
	private int totalFaultCode0;
	private int totalFaultCode1;
	private int totalFaultCode2;
	
	public int getTotalPermanentFaultCode() {
		return totalPermanentFaultCode;
	}
	public void setTotalPermanentFaultCode(int totalPermanentFaultCode) {
		this.totalPermanentFaultCode = totalPermanentFaultCode;
	}
	public int getTotalWarnCode() {
		return totalWarnCode;
	}
	public void setTotalWarnCode(int totalWarnCode) {
		this.totalWarnCode = totalWarnCode;
	}
	public int getTotalFaultCode0() {
		return totalFaultCode0;
	}
	public void setTotalFaultCode0(int totalFaultCode0) {
		this.totalFaultCode0 = totalFaultCode0;
	}
	public int getTotalFaultCode1() {
		return totalFaultCode1;
	}
	public void setTotalFaultCode1(int totalFaultCode1) {
		this.totalFaultCode1 = totalFaultCode1;
	}
	public int getTotalFaultCode2() {
		return totalFaultCode2;
	}
	public void setTotalFaultCode2(int totalFaultCode2) {
		this.totalFaultCode2 = totalFaultCode2;
	}
	/**
	 * @return the powerOnOff
	 */
	public double getPowerOnOff() {
		return PowerOnOff;
	}
	/**
	 * @param powerOnOff the powerOnOff to set
	 */
	public void setPowerOnOff(double powerOnOff) {
		PowerOnOff = powerOnOff;
	}
	/**
	 * @return the pActiveSet
	 */
	public double getPActiveSet() {
		return PActiveSet;
	}
	/**
	 * @param pActiveSet the pActiveSet to set
	 */
	public void setPActiveSet(double pActiveSet) {
		PActiveSet = pActiveSet;
	}
	/**
	 * @return the pFactorSet
	 */
	public double getPFactorSet() {
		return PFactorSet;
	}
	/**
	 * @param pFactorSet the pFactorSet to set
	 */
	public void setPFactorSet(double pFactorSet) {
		PFactorSet = pFactorSet;
	}
	/**
	 * @return the pReactiveSet
	 */
	public double getPReactiveSet() {
		return PReactiveSet;
	}
	/**
	 * @param pReactiveSet the pReactiveSet to set
	 */
	public void setPReactiveSet(double pReactiveSet) {
		PReactiveSet = pReactiveSet;
	}
	/**
	 * @return the gridVMax
	 */
	public double getGridVMax() {
		return GridVMax;
	}
	/**
	 * @param gridVMax the gridVMax to set
	 */
	public void setGridVMax(double gridVMax) {
		GridVMax = gridVMax;
	}
	/**
	 * @return the gridVmaxTripT
	 */
	public double getGridVmaxTripT() {
		return GridVmaxTripT;
	}
	/**
	 * @param gridVmaxTripT the gridVmaxTripT to set
	 */
	public void setGridVmaxTripT(double gridVmaxTripT) {
		GridVmaxTripT = gridVmaxTripT;
	}
	/**
	 * @return the gridVMin
	 */
	public double getGridVMin() {
		return GridVMin;
	}
	/**
	 * @param gridVMin the gridVMin to set
	 */
	public void setGridVMin(double gridVMin) {
		GridVMin = gridVMin;
	}
	/**
	 * @return the gridVminTripT
	 */
	public double getGridVminTripT() {
		return GridVminTripT;
	}
	/**
	 * @param gridVminTripT the gridVminTripT to set
	 */
	public void setGridVminTripT(double gridVminTripT) {
		GridVminTripT = gridVminTripT;
	}
	/**
	 * @return the gridFMax
	 */
	public double getGridFMax() {
		return GridFMax;
	}
	/**
	 * @param gridFMax the gridFMax to set
	 */
	public void setGridFMax(double gridFMax) {
		GridFMax = gridFMax;
	}
	/**
	 * @return the gridFMin
	 */
	public double getGridFMin() {
		return GridFMin;
	}
	/**
	 * @param gridFMin the gridFMin to set
	 */
	public void setGridFMin(double gridFMin) {
		GridFMin = gridFMin;
	}
	/**
	 * @return the gridFTripT
	 */
	public double getGridFTripT() {
		return GridFTripT;
	}
	/**
	 * @param gridFTripT the gridFTripT to set
	 */
	public void setGridFTripT(double gridFTripT) {
		GridFTripT = gridFTripT;
	}
	/**
	 * @return the activePower
	 */
	public double getActivePower() {
		return ActivePower;
	}
	/**
	 * @param activePower the activePower to set
	 */
	public void setActivePower(double activePower) {
		ActivePower = activePower;
	}
	/**
	 * @return the powerFactor
	 */
	public double getPowerFactor() {
		return PowerFactor;
	}
	/**
	 * @param powerFactor the powerFactor to set
	 */
	public void setPowerFactor(double powerFactor) {
		PowerFactor = powerFactor;
	}
	/**
	 * @return the startDelayTime
	 */
	public double getStartDelayTime() {
		return StartDelayTime;
	}
	/**
	 * @param startDelayTime the startDelayTime to set
	 */
	public void setStartDelayTime(double startDelayTime) {
		StartDelayTime = startDelayTime;
	}
	/**
	 * @return the risomin
	 */
	public double getRisomin() {
		return Risomin;
	}
	/**
	 * @param risomin the risomin to set
	 */
	public void setRisomin(double risomin) {
		Risomin = risomin;
	}
	/**
	 * @return the pVStartVol
	 */
	public double getPVStartVol() {
		return PVStartVol;
	}
	/**
	 * @param pVStartVol the pVStartVol to set
	 */
	public void setPVStartVol(double pVStartVol) {
		PVStartVol = pVStartVol;
	}
	/**
	 * @return the dCIMax
	 */
	public double getDCIMax() {
		return DCIMax;
	}
	/**
	 * @param dCIMax the dCIMax to set
	 */
	public void setDCIMax(double dCIMax) {
		DCIMax = dCIMax;
	}
	/**
	 * @return the tambientMax
	 */
	public double getTambientMax() {
		return TambientMax;
	}
	/**
	 * @param tambientMax the tambientMax to set
	 */
	public void setTambientMax(double tambientMax) {
		TambientMax = tambientMax;
	}
	/**
	 * @return the tmoduleMax
	 */
	public double getTmoduleMax() {
		return TmoduleMax;
	}
	/**
	 * @param tmoduleMax the tmoduleMax to set
	 */
	public void setTmoduleMax(double tmoduleMax) {
		TmoduleMax = tmoduleMax;
	}
	/**
	 * @return the offsetDiffMax
	 */
	public double getOffsetDiffMax() {
		return OffsetDiffMax;
	}
	/**
	 * @param offsetDiffMax the offsetDiffMax to set
	 */
	public void setOffsetDiffMax(double offsetDiffMax) {
		OffsetDiffMax = offsetDiffMax;
	}
	/**
	 * @return the gridVolUnbalance
	 */
	public double getGridVolUnbalance() {
		return GridVolUnbalance;
	}
	/**
	 * @param gridVolUnbalance the gridVolUnbalance to set
	 */
	public void setGridVolUnbalance(double gridVolUnbalance) {
		GridVolUnbalance = gridVolUnbalance;
	}
	/**
	 * @return the softPowerStep
	 */
	public double getSoftPowerStep() {
		return SoftPowerStep;
	}
	/**
	 * @param softPowerStep the softPowerStep to set
	 */
	public void setSoftPowerStep(double softPowerStep) {
		SoftPowerStep = softPowerStep;
	}
	/**
	 * @return the totalEnergyToEnergy
	 */
	public double getTotalEnergyToEnergy() {
		return TotalEnergyToEnergy;
	}
	/**
	 * @param totalEnergyToEnergy the totalEnergyToEnergy to set
	 */
	public void setTotalEnergyToEnergy(double totalEnergyToEnergy) {
		TotalEnergyToEnergy = totalEnergyToEnergy;
	}
	/**
	 * @return the totalEnergyToday
	 */
	public double getTotalEnergyToday() {
		return TotalEnergyToday;
	}
	/**
	 * @param totalEnergyToday the totalEnergyToday to set
	 */
	public void setTotalEnergyToday(double totalEnergyToday) {
		TotalEnergyToday = totalEnergyToday;
	}
	/**
	 * @return the inverterEfficiency
	 */
	public double getInverterEfficiency() {
		return InverterEfficiency;
	}
	/**
	 * @param inverterEfficiency the inverterEfficiency to set
	 */
	public void setInverterEfficiency(double inverterEfficiency) {
		InverterEfficiency = inverterEfficiency;
	}
	/**
	 * @return the powerFactor1
	 */
	public double getPowerFactor1() {
		return PowerFactor1;
	}
	/**
	 * @param powerFactor1 the powerFactor1 to set
	 */
	public void setPowerFactor1(double powerFactor1) {
		PowerFactor1 = powerFactor1;
	}
	/**
	 * @return the maxActivePowerToday
	 */
	public double getMaxActivePowerToday() {
		return MaxActivePowerToday;
	}
	/**
	 * @param maxActivePowerToday the maxActivePowerToday to set
	 */
	public void setMaxActivePowerToday(double maxActivePowerToday) {
		MaxActivePowerToday = maxActivePowerToday;
	}
	/**
	 * @return the runTimeToGrid
	 */
	public double getRunTimeToGrid() {
		return RunTimeToGrid;
	}
	/**
	 * @param runTimeToGrid the runTimeToGrid to set
	 */
	public void setRunTimeToGrid(double runTimeToGrid) {
		RunTimeToGrid = runTimeToGrid;
	}
	/**
	 * @return the aC_ActivePower
	 */
	public double getAC_ActivePower() {
		return AC_ActivePower;
	}
	/**
	 * @param aC_ActivePower the aC_ActivePower to set
	 */
	public void setAC_ActivePower(double aC_ActivePower) {
		AC_ActivePower = aC_ActivePower;
	}
	/**
	 * @return the aC_ApparentPower
	 */
	public double getAC_ApparentPower() {
		return AC_ApparentPower;
	}
	/**
	 * @param aC_ApparentPower the aC_ApparentPower to set
	 */
	public void setAC_ApparentPower(double aC_ApparentPower) {
		AC_ApparentPower = aC_ApparentPower;
	}
	/**
	 * @return the gridVoltageUab
	 */
	public double getGridVoltageUab() {
		return GridVoltageUab;
	}
	/**
	 * @param gridVoltageUab the gridVoltageUab to set
	 */
	public void setGridVoltageUab(double gridVoltageUab) {
		GridVoltageUab = gridVoltageUab;
	}
	/**
	 * @return the gridVoltageUbc
	 */
	public double getGridVoltageUbc() {
		return GridVoltageUbc;
	}
	/**
	 * @param gridVoltageUbc the gridVoltageUbc to set
	 */
	public void setGridVoltageUbc(double gridVoltageUbc) {
		GridVoltageUbc = gridVoltageUbc;
	}
	/**
	 * @return the gridVoltageUca
	 */
	public double getGridVoltageUca() {
		return GridVoltageUca;
	}
	/**
	 * @param gridVoltageUca the gridVoltageUca to set
	 */
	public void setGridVoltageUca(double gridVoltageUca) {
		GridVoltageUca = gridVoltageUca;
	}
	/**
	 * @return the gridA_PhaseCurrent
	 */
	public double getGridA_PhaseCurrent() {
		return GridA_PhaseCurrent;
	}
	/**
	 * @param gridA_PhaseCurrent the gridA_PhaseCurrent to set
	 */
	public void setGridA_PhaseCurrent(double gridA_PhaseCurrent) {
		GridA_PhaseCurrent = gridA_PhaseCurrent;
	}
	/**
	 * @return the gridB_PhaseCurrent
	 */
	public double getGridB_PhaseCurrent() {
		return GridB_PhaseCurrent;
	}
	/**
	 * @param gridB_PhaseCurrent the gridB_PhaseCurrent to set
	 */
	public void setGridB_PhaseCurrent(double gridB_PhaseCurrent) {
		GridB_PhaseCurrent = gridB_PhaseCurrent;
	}
	/**
	 * @return the gridC_PhaseCurrent
	 */
	public double getGridC_PhaseCurrent() {
		return GridC_PhaseCurrent;
	}
	/**
	 * @param gridC_PhaseCurrent the gridC_PhaseCurrent to set
	 */
	public void setGridC_PhaseCurrent(double gridC_PhaseCurrent) {
		GridC_PhaseCurrent = gridC_PhaseCurrent;
	}
	/**
	 * @return the pV1_Voltage
	 */
	public double getPV1_Voltage() {
		return PV1_Voltage;
	}
	/**
	 * @param pV1_Voltage the pV1_Voltage to set
	 */
	public void setPV1_Voltage(double pV1_Voltage) {
		PV1_Voltage = pV1_Voltage;
	}
	/**
	 * @return the pV1_Current
	 */
	public double getPV1_Current() {
		return PV1_Current;
	}
	/**
	 * @param pV1_Current the pV1_Current to set
	 */
	public void setPV1_Current(double pV1_Current) {
		PV1_Current = pV1_Current;
	}
	/**
	 * @return the pV2_Voltage
	 */
	public double getPV2_Voltage() {
		return PV2_Voltage;
	}
	/**
	 * @param pV2_Voltage the pV2_Voltage to set
	 */
	public void setPV2_Voltage(double pV2_Voltage) {
		PV2_Voltage = pV2_Voltage;
	}
	/**
	 * @return the pV2_Current
	 */
	public double getPV2_Current() {
		return PV2_Current;
	}
	/**
	 * @param pV2_Current the pV2_Current to set
	 */
	public void setPV2_Current(double pV2_Current) {
		PV2_Current = pV2_Current;
	}
	/**
	 * @return the pV3_Voltage
	 */
	public double getPV3_Voltage() {
		return PV3_Voltage;
	}
	/**
	 * @param pV3_Voltage the pV3_Voltage to set
	 */
	public void setPV3_Voltage(double pV3_Voltage) {
		PV3_Voltage = pV3_Voltage;
	}
	/**
	 * @return the pV3_Current
	 */
	public double getPV3_Current() {
		return PV3_Current;
	}
	/**
	 * @param pV3_Current the pV3_Current to set
	 */
	public void setPV3_Current(double pV3_Current) {
		PV3_Current = pV3_Current;
	}
	/**
	 * @return the grid_Frequency
	 */
	public double getGrid_Frequency() {
		return Grid_Frequency;
	}
	/**
	 * @param grid_Frequency the grid_Frequency to set
	 */
	public void setGrid_Frequency(double grid_Frequency) {
		Grid_Frequency = grid_Frequency;
	}
	/**
	 * @return the moduleTemp
	 */
	public double getModuleTemp() {
		return ModuleTemp;
	}
	/**
	 * @param moduleTemp the moduleTemp to set
	 */
	public void setModuleTemp(double moduleTemp) {
		ModuleTemp = moduleTemp;
	}
	/**
	 * @return the internalTemp
	 */
	public double getInternalTemp() {
		return InternalTemp;
	}
	/**
	 * @param internalTemp the internalTemp to set
	 */
	public void setInternalTemp(double internalTemp) {
		InternalTemp = internalTemp;
	}
	/**
	 * @return the transformerTemp
	 */
	public double getTransformerTemp() {
		return TransformerTemp;
	}
	/**
	 * @param transformerTemp the transformerTemp to set
	 */
	public void setTransformerTemp(double transformerTemp) {
		TransformerTemp = transformerTemp;
	}
	/**
	 * @return the inverterModeCode
	 */
	public double getInverterModeCode() {
		return InverterModeCode;
	}
	/**
	 * @param inverterModeCode the inverterModeCode to set
	 */
	public void setInverterModeCode(double inverterModeCode) {
		InverterModeCode = inverterModeCode;
	}
	/**
	 * @return the permanentFaultCode
	 */
	public double getPermanentFaultCode() {
		return PermanentFaultCode;
	}
	/**
	 * @param permanentFaultCode the permanentFaultCode to set
	 */
	public void setPermanentFaultCode(double permanentFaultCode) {
		PermanentFaultCode = permanentFaultCode;
	}
	/**
	 * @return the warnCode
	 */
	public double getWarnCode() {
		return WarnCode;
	}
	/**
	 * @param warnCode the warnCode to set
	 */
	public void setWarnCode(double warnCode) {
		WarnCode = warnCode;
	}
	/**
	 * @return the faultCode0
	 */
	public double getFaultCode0() {
		return FaultCode0;
	}
	/**
	 * @param faultCode0 the faultCode0 to set
	 */
	public void setFaultCode0(double faultCode0) {
		FaultCode0 = faultCode0;
	}
	/**
	 * @return the faultCode1
	 */
	public double getFaultCode1() {
		return FaultCode1;
	}
	/**
	 * @param faultCode1 the faultCode1 to set
	 */
	public void setFaultCode1(double faultCode1) {
		FaultCode1 = faultCode1;
	}
	/**
	 * @return the faultCode2
	 */
	public double getFaultCode2() {
		return FaultCode2;
	}
	/**
	 * @param faultCode2 the faultCode2 to set
	 */
	public void setFaultCode2(double faultCode2) {
		FaultCode2 = faultCode2;
	}
	public String getSerialNumber() {
		return SerialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}
	
	

}
