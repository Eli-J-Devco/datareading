/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.nwm.api.DBManagers.DB;
import com.nwm.api.entities.ModelAcuvimIIREntity;
import com.nwm.api.utils.Lib;

public class ModelAcuvimIIRService extends DB {
	
	/**
	 * @description set data ModelAcuvimIIR
	 * @author Duy.Phan
	 * @since 2024-05-03
	 * @param data
	 */
	
	public ModelAcuvimIIREntity setModelAcuvimIIR(String line, double offset_data_old) {
		try {
			List<String> words = Lists.newArrayList(Splitter.on(',').split(line));
			if (words.size() > 0) {
				ModelAcuvimIIREntity dataModelAcuvimIIR = new ModelAcuvimIIREntity();
				
				Double power = Double.parseDouble(!Lib.isBlank(words.get(21)) ? words.get(21) : "0.001");
				Double energy = Double.parseDouble(!Lib.isBlank(words.get(86)) ? words.get(86) : "0.001");
				if(energy > 0) { energy = energy + offset_data_old; }
				
				dataModelAcuvimIIR.setTime(words.get(0).replace("'", ""));
				dataModelAcuvimIIR.setError(Integer.parseInt(!Lib.isBlank(words.get(1)) ? words.get(1) : "0"));
				dataModelAcuvimIIR.setLow_alarm(Integer.parseInt(!Lib.isBlank(words.get(2)) ? words.get(2) : "0"));
				dataModelAcuvimIIR.setHigh_alarm(Integer.parseInt(!Lib.isBlank(words.get(3)) ? words.get(3) : "0"));
				
				dataModelAcuvimIIR.setFrequency(Double.parseDouble(!Lib.isBlank(words.get(4)) ? words.get(4) : "0.001"));
				dataModelAcuvimIIR.setPhasevoltageV1(Double.parseDouble(!Lib.isBlank(words.get(5)) ? words.get(5) : "0.001"));
				dataModelAcuvimIIR.setPhasevoltageV2(Double.parseDouble(!Lib.isBlank(words.get(6)) ? words.get(6) : "0.001"));
				dataModelAcuvimIIR.setPhasevoltageV3(Double.parseDouble(!Lib.isBlank(words.get(7)) ? words.get(7) : "0.001"));
				dataModelAcuvimIIR.setAveragevoltageVavg(Double.parseDouble(!Lib.isBlank(words.get(8)) ? words.get(8) : "0.001"));
				dataModelAcuvimIIR.setLinevoltageV12(Double.parseDouble(!Lib.isBlank(words.get(9)) ? words.get(9) : "0.001"));
				dataModelAcuvimIIR.setLinevoltageV23(Double.parseDouble(!Lib.isBlank(words.get(10)) ? words.get(10) : "0.001"));
				dataModelAcuvimIIR.setLinevoltageV31(Double.parseDouble(!Lib.isBlank(words.get(11)) ? words.get(11) : "0.001"));
				dataModelAcuvimIIR.setAveragelinevoltageVLavg(Double.parseDouble(!Lib.isBlank(words.get(12)) ? words.get(12) : "0.001"));
				dataModelAcuvimIIR.setPhaselinecurrentI1(Double.parseDouble(!Lib.isBlank(words.get(13)) ? words.get(13) : "0.001"));
				dataModelAcuvimIIR.setPhaselinecurrentI2(Double.parseDouble(!Lib.isBlank(words.get(14)) ? words.get(14) : "0.001"));
				dataModelAcuvimIIR.setPhaselinecurrentI3(Double.parseDouble(!Lib.isBlank(words.get(15)) ? words.get(15) : "0.001"));
				dataModelAcuvimIIR.setAveragecurrentIavg(Double.parseDouble(!Lib.isBlank(words.get(16)) ? words.get(16) : "0.001"));
				dataModelAcuvimIIR.setNeutrallinecurrentIn(Double.parseDouble(!Lib.isBlank(words.get(16)) ? words.get(16) : "0.001"));
				dataModelAcuvimIIR.setPhaseApowerPa(Double.parseDouble(!Lib.isBlank(words.get(18)) ? words.get(18) : "0.001"));
				dataModelAcuvimIIR.setPhaseBpowerPb(Double.parseDouble(!Lib.isBlank(words.get(19)) ? words.get(19) : "0.001"));
				dataModelAcuvimIIR.setPhaseCpowerPc(Double.parseDouble(!Lib.isBlank(words.get(20)) ? words.get(20) : "0.001"));
				dataModelAcuvimIIR.setSystempowerPsum(power);
				dataModelAcuvimIIR.setPhaseAreactivepowerQa(Double.parseDouble(!Lib.isBlank(words.get(22)) ? words.get(22) : "0.001"));
				dataModelAcuvimIIR.setPhaseBreactivepowerQb(Double.parseDouble(!Lib.isBlank(words.get(23)) ? words.get(23) : "0.001"));
				dataModelAcuvimIIR.setPhaseCreactivepowerQc(Double.parseDouble(!Lib.isBlank(words.get(24)) ? words.get(24) : "0.001"));
				dataModelAcuvimIIR.setSystemreactivepowerQsum(Double.parseDouble(!Lib.isBlank(words.get(25)) ? words.get(25) : "0.001"));
				dataModelAcuvimIIR.setPhaseAapparentpowerSa(Double.parseDouble(!Lib.isBlank(words.get(26)) ? words.get(26) : "0.001"));
				dataModelAcuvimIIR.setPhaseBapparentpowerSb(Double.parseDouble(!Lib.isBlank(words.get(27)) ? words.get(27) : "0.001"));
				dataModelAcuvimIIR.setPhaseCapparentpowerSc(Double.parseDouble(!Lib.isBlank(words.get(28)) ? words.get(28) : "0.001"));
				dataModelAcuvimIIR.setSystemapparentpowerSsum(Double.parseDouble(!Lib.isBlank(words.get(29)) ? words.get(29) : "0.001"));
				dataModelAcuvimIIR.setPhaseApowerfactorPFa(Double.parseDouble(!Lib.isBlank(words.get(30)) ? words.get(30) : "0.001"));
				dataModelAcuvimIIR.setPhaseBpowerfactorPFb(Double.parseDouble(!Lib.isBlank(words.get(31)) ? words.get(31) : "0.001"));
				dataModelAcuvimIIR.setPhaseCpowerfactorPFc(Double.parseDouble(!Lib.isBlank(words.get(32)) ? words.get(32) : "0.001"));
				dataModelAcuvimIIR.setSystempowerfactorPFsum(Double.parseDouble(!Lib.isBlank(words.get(33)) ? words.get(33) : "0.001"));
				dataModelAcuvimIIR.setVoltageunbalancefactor(Double.parseDouble(!Lib.isBlank(words.get(34)) ? words.get(34) : "0.001"));
				dataModelAcuvimIIR.setCurrentunbalancefactor(Double.parseDouble(!Lib.isBlank(words.get(35)) ? words.get(35) : "0.001"));
				dataModelAcuvimIIR.setLoadcharacteristicLCR(Double.parseDouble(!Lib.isBlank(words.get(36)) ? words.get(36) : "0.001"));
				dataModelAcuvimIIR.setPowerdemand(Double.parseDouble(!Lib.isBlank(words.get(37)) ? words.get(37) : "0.001"));
				dataModelAcuvimIIR.setReactivepowerdemand(Double.parseDouble(!Lib.isBlank(words.get(38)) ? words.get(38) : "0.001"));
				dataModelAcuvimIIR.setApparentpowerdemand(Double.parseDouble(!Lib.isBlank(words.get(39)) ? words.get(39) : "0.001"));
				dataModelAcuvimIIR.setV1Maximum(Double.parseDouble(!Lib.isBlank(words.get(40)) ? words.get(40) : "0.001"));
				dataModelAcuvimIIR.setV2Maximum(Double.parseDouble(!Lib.isBlank(words.get(41)) ? words.get(41) : "0.001"));
				dataModelAcuvimIIR.setV3Maximum(Double.parseDouble(!Lib.isBlank(words.get(42)) ? words.get(42) : "0.001"));
				dataModelAcuvimIIR.setV12Maximum(Double.parseDouble(!Lib.isBlank(words.get(43)) ? words.get(43) : "0.001"));
				dataModelAcuvimIIR.setV23Maximum(Double.parseDouble(!Lib.isBlank(words.get(44)) ? words.get(44) : "0.001"));
				dataModelAcuvimIIR.setV31Maximum(Double.parseDouble(!Lib.isBlank(words.get(45)) ? words.get(45) : "0.001"));
				dataModelAcuvimIIR.setI1Maximum(Double.parseDouble(!Lib.isBlank(words.get(46)) ? words.get(46) : "0.001"));
				dataModelAcuvimIIR.setI2Maximum(Double.parseDouble(!Lib.isBlank(words.get(47)) ? words.get(47) : "0.001"));
				dataModelAcuvimIIR.setI3Maximum(Double.parseDouble(!Lib.isBlank(words.get(48)) ? words.get(48) : "0.001"));
				dataModelAcuvimIIR.setSystemPowerMaximum(Double.parseDouble(!Lib.isBlank(words.get(49)) ? words.get(49) : "0.001"));
				dataModelAcuvimIIR.setSystemReactivePowerMaximum(Double.parseDouble(!Lib.isBlank(words.get(50)) ? words.get(50) : "0.001"));
				dataModelAcuvimIIR.setSystemApparentPowerMaximum(Double.parseDouble(!Lib.isBlank(words.get(51)) ? words.get(51) : "0.001"));
				dataModelAcuvimIIR.setPowerFactorMaximum(Double.parseDouble(!Lib.isBlank(words.get(52)) ? words.get(52) : "0.001"));
				dataModelAcuvimIIR.setFrequencyMaximum(Double.parseDouble(!Lib.isBlank(words.get(53)) ? words.get(53) : "0.001"));
				dataModelAcuvimIIR.setPowerDemandMaximum(Double.parseDouble(!Lib.isBlank(words.get(54)) ? words.get(54) : "0.001"));
				dataModelAcuvimIIR.setReactivePowerDemandMaximum(Double.parseDouble(!Lib.isBlank(words.get(55)) ? words.get(55) : "0.001"));
				dataModelAcuvimIIR.setApparentPowerDemandMaximum(Double.parseDouble(!Lib.isBlank(words.get(56)) ? words.get(56) : "0.001"));				
				dataModelAcuvimIIR.setVoltageUnbalanceFactorMaximum(Double.parseDouble(!Lib.isBlank(words.get(57)) ? words.get(57) : "0.001"));
				dataModelAcuvimIIR.setCurrentUnbalanceFactormaximum(Double.parseDouble(!Lib.isBlank(words.get(58)) ? words.get(58) : "0.001"));
				dataModelAcuvimIIR.setV1V12THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(59)) ? words.get(59) : "0.001"));
				dataModelAcuvimIIR.setV2V31THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(60)) ? words.get(60) : "0.001"));
				dataModelAcuvimIIR.setV3V23THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(61)) ? words.get(61) : "0.001"));
				dataModelAcuvimIIR.setI1THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(62)) ? words.get(62) : "0.001"));
				dataModelAcuvimIIR.setI2THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(63)) ? words.get(63) : "0.001"));
				dataModelAcuvimIIR.setI3THDMaximum(Double.parseDouble(!Lib.isBlank(words.get(64)) ? words.get(64) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleV2toV1(Double.parseDouble(!Lib.isBlank(words.get(65)) ? words.get(65) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleV3toV1(Double.parseDouble(!Lib.isBlank(words.get(66)) ? words.get(66) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleI1toV1(Double.parseDouble(!Lib.isBlank(words.get(67)) ? words.get(67) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleI2toV1(Double.parseDouble(!Lib.isBlank(words.get(68)) ? words.get(68) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleI3toV1(Double.parseDouble(!Lib.isBlank(words.get(69)) ? words.get(69) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleV23toV12(Double.parseDouble(!Lib.isBlank(words.get(70)) ? words.get(70) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleofI1toV12(Double.parseDouble(!Lib.isBlank(words.get(71)) ? words.get(71) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleofI2toV12(Double.parseDouble(!Lib.isBlank(words.get(72)) ? words.get(72) : "0.001"));
				dataModelAcuvimIIR.setPhaseAngleofI3toV12(Double.parseDouble(!Lib.isBlank(words.get(73)) ? words.get(73) : "0.001"));
				dataModelAcuvimIIR.setTHDV1ofV1V12(Double.parseDouble(!Lib.isBlank(words.get(74)) ? words.get(74) : "0.001"));
				dataModelAcuvimIIR.setTHDV1ofV2V31(Double.parseDouble(!Lib.isBlank(words.get(75)) ? words.get(75) : "0.001"));
				dataModelAcuvimIIR.setTHDV1ofV3V23(Double.parseDouble(!Lib.isBlank(words.get(76)) ? words.get(76) : "0.001"));
				dataModelAcuvimIIR.setAverageTHDofV(Double.parseDouble(!Lib.isBlank(words.get(77)) ? words.get(77) : "0.001"));
				dataModelAcuvimIIR.setTHDI1(Double.parseDouble(!Lib.isBlank(words.get(78)) ? words.get(78) : "0.001"));
				dataModelAcuvimIIR.setTHDI2(Double.parseDouble(!Lib.isBlank(words.get(79)) ? words.get(79) : "0.001"));
				dataModelAcuvimIIR.setTHDI3(Double.parseDouble(!Lib.isBlank(words.get(80)) ? words.get(80) : "0.001"));
				dataModelAcuvimIIR.setAverageTHDofI(Double.parseDouble(!Lib.isBlank(words.get(81)) ? words.get(81) : "0.001"));
				dataModelAcuvimIIR.setEnergyIMP(Double.parseDouble(!Lib.isBlank(words.get(82)) ? words.get(82) : "0.001"));
				dataModelAcuvimIIR.setEnergyEXP(Double.parseDouble(!Lib.isBlank(words.get(83)) ? words.get(83) : "0.001"));
				dataModelAcuvimIIR.setReactiveEnergyIMP(Double.parseDouble(!Lib.isBlank(words.get(84)) ? words.get(84) : "0.001"));
				dataModelAcuvimIIR.setReactiveEnergyEXP(Double.parseDouble(!Lib.isBlank(words.get(85)) ? words.get(85) : "0.001"));
				dataModelAcuvimIIR.setEnergyTotal(energy);		
				dataModelAcuvimIIR.setEnergyNet(Double.parseDouble(!Lib.isBlank(words.get(87)) ? words.get(87) : "0.001"));
				dataModelAcuvimIIR.setReactiveEnergyTotal(Double.parseDouble(!Lib.isBlank(words.get(88)) ? words.get(88) : "0.001"));
				dataModelAcuvimIIR.setReactiveEnergyNet(Double.parseDouble(!Lib.isBlank(words.get(89)) ? words.get(89) : "0.001"));
				dataModelAcuvimIIR.setApparentEnergy(Double.parseDouble(!Lib.isBlank(words.get(90)) ? words.get(90) : "0.001"));
				
				// set custom field nvmActivePower and nvmActiveEnergy
				dataModelAcuvimIIR.setNvmActivePower(power);
				dataModelAcuvimIIR.setNvmActiveEnergy(energy);
				
				return dataModelAcuvimIIR;
				
			} else {
				return new ModelAcuvimIIREntity();
			}
			
			
		} catch (Exception ex) {
			log.error("insert", ex);
			return new ModelAcuvimIIREntity();
		}
	}
	
	/**
	 * @description insert data from datalogger to model_acuvim_IIR
	 * @author Duy.Phan
	 * @since 2024-05-03
	 * @param data from datalogger
	 */
	
	public boolean insertModelAcuvimIIR(ModelAcuvimIIREntity obj) {
		try {
			ModelAcuvimIIREntity dataObj = (ModelAcuvimIIREntity) queryForObject("ModelAcuvimIIR.getLastRow", obj);
			double measuredProduction = 0;
			if(dataObj != null && dataObj.getId_device() > 0 && dataObj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() != 0.001 ) {
				measuredProduction = obj.getNvmActiveEnergy() - dataObj.getNvmActiveEnergy();				 
			}
			
			if(obj.getNvmActiveEnergy() == 0.001 || obj.getNvmActiveEnergy() < 0) {
				 obj.setNvmActiveEnergy(dataObj.getNvmActiveEnergy());
				 obj.setEnergyTotal(dataObj.getNvmActiveEnergy());
			 }
			
			obj.setMeasuredProduction(measuredProduction);
			 
			Object insertId = insert("ModelAcuvimIIR.insertModelAcuvimIIR", obj);
	        if(insertId == null ) {
	        	return false;
	        }
	        return true;
		} catch (Exception ex) {
			log.error("insert", ex);
			return false;
		}

	}

}
