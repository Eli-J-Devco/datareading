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
import com.nwm.api.entities.ModelPowerLogicPM8000LoadMeterEntity;
import com.nwm.api.utils.Lib;

public class ModelPowerLogicPM8000LoadMeterService extends DB {
	
	/**
	 * @description set data  ModelPowerLogicPM8000LoadMeter
	 * @author Hung.Bui
	 * @since 2024-11-05
	 * @param data
	 */
	
	public ModelPowerLogicPM8000LoadMeterEntity setModelPowerLogicPM8000LoadMeter(String line, double offset_data_old) {
		try {
			List<String> words = Lists.newArrayList(Splitter.on(',').split(line));
			if (words.size() > 0) {
				ModelPowerLogicPM8000LoadMeterEntity data = new ModelPowerLogicPM8000LoadMeterEntity();
				
				Double power = Double.parseDouble(!Lib.isBlank(words.get(47)) ? words.get(47) : "0.001");
				Double energy = Double.parseDouble(!Lib.isBlank(words.get(4)) ? words.get(4) : "0.001");
				if(energy < 0 ) { energy = energy * -1; } 
				if(offset_data_old > 0 && energy > 0 ) { energy = energy + offset_data_old; }
				
				data.setTime(words.get(0).replace("'", ""));
				data.setError(Integer.parseInt(!Lib.isBlank(words.get(1)) ? words.get(1) : "0"));
				data.setLow_alarm(Integer.parseInt(!Lib.isBlank(words.get(2)) ? words.get(2) : "0"));
				data.setHigh_alarm(Integer.parseInt(!Lib.isBlank(words.get(3)) ? words.get(3) : "0"));
				
				data.setActiveEnergyDelivered(Double.parseDouble(!Lib.isBlank(words.get(4)) ? words.get(4) : "0.001"));
				data.setActiveEnergyReceived(Double.parseDouble(!Lib.isBlank(words.get(5)) ? words.get(5) : "0.001"));
				data.setActiveEnergyDeliveredPlusReceived(Double.parseDouble(!Lib.isBlank(words.get(6)) ? words.get(6) : "0.001"));
				data.setActiveEnergyDeliveredMinusReceived(Double.parseDouble(!Lib.isBlank(words.get(7)) ? words.get(7) : "0.001"));
				data.setReactiveEnergyDelivered(Double.parseDouble(!Lib.isBlank(words.get(8)) ? words.get(8) : "0.001"));
				data.setReactiveEnergyReceived(Double.parseDouble(!Lib.isBlank(words.get(9)) ? words.get(9) : "0.001"));
				data.setReactiveEnergyDeliveredPlusReceived(Double.parseDouble(!Lib.isBlank(words.get(10)) ? words.get(10) : "0.001"));
				data.setReactiveEnergyDeliveredMinusReceived(Double.parseDouble(!Lib.isBlank(words.get(11)) ? words.get(11) : "0.001"));
				data.setApparentEnergyDelivered(Double.parseDouble(!Lib.isBlank(words.get(12)) ? words.get(12) : "0.001"));
				data.setApparentEnergyReceived(Double.parseDouble(!Lib.isBlank(words.get(13)) ? words.get(13) : "0.001"));
				data.setApparentEnergyDeliveredPlusReceived(Double.parseDouble(!Lib.isBlank(words.get(14)) ? words.get(14) : "0.001"));
				data.setApparentEnergyDeliveredMinusReceived(Double.parseDouble(!Lib.isBlank(words.get(15)) ? words.get(15) : "0.001"));
				data.setActiveEnergyinQuadrantI(Double.parseDouble(!Lib.isBlank(words.get(16)) ? words.get(16) : "0.001"));
				data.setActiveEnergyinQuadrantII(Double.parseDouble(!Lib.isBlank(words.get(17)) ? words.get(17) : "0.001"));
				data.setActiveEnergyinQuadrantIII(Double.parseDouble(!Lib.isBlank(words.get(18)) ? words.get(18) : "0.001"));
				data.setActiveEnergyinQuadrantIV(Double.parseDouble(!Lib.isBlank(words.get(19)) ? words.get(19) : "0.001"));
				data.setReactiveEnergyinQuadrantI(Double.parseDouble(!Lib.isBlank(words.get(20)) ? words.get(20) : "0.001"));
				data.setReactiveEnergyinQuadrantII(Double.parseDouble(!Lib.isBlank(words.get(21)) ? words.get(21) : "0.001"));
				data.setReactiveEnergyinQuadrantIII(Double.parseDouble(!Lib.isBlank(words.get(22)) ? words.get(22) : "0.001"));
				data.setReactiveEnergyinQuadrantIV(Double.parseDouble(!Lib.isBlank(words.get(23)) ? words.get(23) : "0.001"));
				data.setApparentEnergyinQuadrantI(Double.parseDouble(!Lib.isBlank(words.get(24)) ? words.get(24) : "0.001"));
				data.setApparentEnergyinQuadrantII(Double.parseDouble(!Lib.isBlank(words.get(25)) ? words.get(25) : "0.001"));
				data.setApparentEnergyinQuadrantIII(Double.parseDouble(!Lib.isBlank(words.get(26)) ? words.get(26) : "0.001"));
				data.setApparentEnergyinQuadrantIV(Double.parseDouble(!Lib.isBlank(words.get(27)) ? words.get(27) : "0.001"));
				data.setCurrentPhaseA(Double.parseDouble(!Lib.isBlank(words.get(28)) ? words.get(28) : "0.001"));
				data.setCurrentPhaseB(Double.parseDouble(!Lib.isBlank(words.get(29)) ? words.get(29) : "0.001"));
				data.setCurrentPhaseC(Double.parseDouble(!Lib.isBlank(words.get(30)) ? words.get(30) : "0.001"));
				data.setCurrentNeutralLine(Double.parseDouble(!Lib.isBlank(words.get(31)) ? words.get(31) : "0.001"));
				data.setCurrentGroundLine(Double.parseDouble(!Lib.isBlank(words.get(32)) ? words.get(32) : "0.001"));
				data.setCurrentAverage(Double.parseDouble(!Lib.isBlank(words.get(33)) ? words.get(33) : "0.001"));
				data.setCurrentUnbalanceWorst(Double.parseDouble(!Lib.isBlank(words.get(34)) ? words.get(34) : "0.001"));
				data.setVoltagePhaseAB(Double.parseDouble(!Lib.isBlank(words.get(35)) ? words.get(35) : "0.001"));
				data.setVoltagePhaseBC(Double.parseDouble(!Lib.isBlank(words.get(36)) ? words.get(36) : "0.001"));
				data.setVoltagePhaseCA(Double.parseDouble(!Lib.isBlank(words.get(37)) ? words.get(37) : "0.001"));
				data.setVoltageLLAverage(Double.parseDouble(!Lib.isBlank(words.get(38)) ? words.get(38) : "0.001"));
				data.setVoltageAN(Double.parseDouble(!Lib.isBlank(words.get(39)) ? words.get(39) : "0.001"));
				data.setVoltageBN(Double.parseDouble(!Lib.isBlank(words.get(40)) ? words.get(40) : "0.001"));
				data.setVoltageCN(Double.parseDouble(!Lib.isBlank(words.get(41)) ? words.get(41) : "0.001"));
				data.setVoltageLNAverage(Double.parseDouble(!Lib.isBlank(words.get(42)) ? words.get(42) : "0.001"));
				data.setVoltageUnbalanceLNWorst(Double.parseDouble(!Lib.isBlank(words.get(43)) ? words.get(43) : "0.001"));
				data.setActivePowerPhaseA(Double.parseDouble(!Lib.isBlank(words.get(44)) ? words.get(44) : "0.001"));
				data.setActivePowerPhaseB(Double.parseDouble(!Lib.isBlank(words.get(45)) ? words.get(45) : "0.001"));
				data.setActivePowerPhaseC(Double.parseDouble(!Lib.isBlank(words.get(46)) ? words.get(46) : "0.001"));
				data.setActivePowerTotal(Double.parseDouble(!Lib.isBlank(words.get(47)) ? words.get(47) : "0.001"));
				data.setReactivePowerPhaseA(Double.parseDouble(!Lib.isBlank(words.get(48)) ? words.get(48) : "0.001"));
				data.setReactivePowerPhaseB(Double.parseDouble(!Lib.isBlank(words.get(49)) ? words.get(49) : "0.001"));
				data.setReactivePowerPhaseC(Double.parseDouble(!Lib.isBlank(words.get(50)) ? words.get(50) : "0.001"));
				data.setReactivePowerTotal(Double.parseDouble(!Lib.isBlank(words.get(51)) ? words.get(51) : "0.001"));
				data.setApparentPowerPhaseA(Double.parseDouble(!Lib.isBlank(words.get(52)) ? words.get(52) : "0.001"));
				data.setApparentPowerPhaseB(Double.parseDouble(!Lib.isBlank(words.get(53)) ? words.get(53) : "0.001"));
				data.setApparentPowerPhaseC(Double.parseDouble(!Lib.isBlank(words.get(54)) ? words.get(54) : "0.001"));
				data.setApparentPowerTotal(Double.parseDouble(!Lib.isBlank(words.get(55)) ? words.get(55) : "0.001"));
				data.setFrequency(Double.parseDouble(!Lib.isBlank(words.get(56)) ? words.get(56) : "0.001"));
				data.setPowerFactorPhaseA(Double.parseDouble(!Lib.isBlank(words.get(57)) ? words.get(57) : "0.001"));
				data.setPowerFactorPhaseB(Double.parseDouble(!Lib.isBlank(words.get(58)) ? words.get(58) : "0.001"));
				data.setPowerFactorPhaseC(Double.parseDouble(!Lib.isBlank(words.get(59)) ? words.get(59) : "0.001"));
				data.setPowerFactorTotal(Double.parseDouble(!Lib.isBlank(words.get(60)) ? words.get(60) : "0.001"));
				
				// set custom field nvmActivePower and nvmActiveEnergy
				data.setNvmActivePower(power);
				data.setNvmActiveEnergy(energy);
				
				return data;
				
			} else {
				return new ModelPowerLogicPM8000LoadMeterEntity();
			}
			
			
		} catch (Exception ex) {
			return new ModelPowerLogicPM8000LoadMeterEntity();
		}
	}

	/**
	 * @description insert data from datalogger to model_power_logic_pm8000_load_meter
	 * @author Hung.Bui
	 * @since 2024-11-05
	 * @param data from datalogger
	 */
	
	public boolean insertModelPowerLogicPM8000LoadMeter(ModelPowerLogicPM8000LoadMeterEntity obj) {
		try {
			ModelPowerLogicPM8000LoadMeterEntity dataObj = (ModelPowerLogicPM8000LoadMeterEntity) queryForObject("ModelPowerLogicPM8000LoadMeter.getLastRow", obj);
			 double measuredProduction = 0;
			 if(dataObj != null && dataObj.getId_device() > 0 && dataObj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() != 0.001 ) {
				 measuredProduction = obj.getNvmActiveEnergy() - dataObj.getNvmActiveEnergy();		 
			 }
			 
			 if(obj.getNvmActiveEnergy() == 0.001 || obj.getNvmActiveEnergy() < 0) {
				 obj.setNvmActiveEnergy(dataObj.getNvmActiveEnergy());
				 obj.setActiveEnergyDelivered(dataObj.getNvmActiveEnergy());
			 }

			 obj.setMeasuredProduction(measuredProduction);
			 
			 Object insertId = insert("ModelPowerLogicPM8000LoadMeter.insertModelPowerLogicPM8000LoadMeter", obj);
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