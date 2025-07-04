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
import com.nwm.api.entities.ModelJanitzaUmg604proEntity;
import com.nwm.api.utils.Lib;

public class ModelJanitzaUmg604proService extends DB {
	/**
	 * @description set data 
	 * @author long.pham
	 * @since 2023-01-16
	 * @param data
	 */
	
	public ModelJanitzaUmg604proEntity setModelJanitzaUmg604pro(String line) {
		try {
			List<String> words = Lists.newArrayList(Splitter.on(',').split(line));
			if (words.size() > 0) {
				ModelJanitzaUmg604proEntity dataModelJanit = new ModelJanitzaUmg604proEntity();
				
				Double power = Double.parseDouble(!Lib.isBlank(words.get(17)) ? words.get(17) : "0.001");
				Double energy = Double.parseDouble(!Lib.isBlank(words.get(31)) ? words.get(31) : "0.001");				
				
				dataModelJanit.setTime(words.get(0).replace("'", ""));
				dataModelJanit.setError(Integer.parseInt(!Lib.isBlank(words.get(1)) ? words.get(1) : "0"));
				dataModelJanit.setLow_alarm(Integer.parseInt(!Lib.isBlank(words.get(2)) ? words.get(2) : "0"));
				dataModelJanit.setHigh_alarm(Integer.parseInt(!Lib.isBlank(words.get(3)) ? words.get(3) : "0"));
				
				dataModelJanit.setPhaseAVoltage(Double.parseDouble(!Lib.isBlank(words.get(4)) ? words.get(4) : "0.001"));
				dataModelJanit.setPhaseBVoltage(Double.parseDouble(!Lib.isBlank(words.get(5)) ? words.get(5) : "0.001"));
				dataModelJanit.setPhaseCVoltage(Double.parseDouble(!Lib.isBlank(words.get(6)) ? words.get(6) : "0.001"));
				dataModelJanit.setABVoltage(Double.parseDouble(!Lib.isBlank(words.get(7)) ? words.get(7) : "0.001"));
				dataModelJanit.setBCVoltage(Double.parseDouble(!Lib.isBlank(words.get(8)) ? words.get(8) : "0.001"));
				dataModelJanit.setCAVoltage(Double.parseDouble(!Lib.isBlank(words.get(9)) ? words.get(9) : "0.001"));
				dataModelJanit.setPhaseACurrent(Double.parseDouble(!Lib.isBlank(words.get(10)) ? words.get(10) : "0.001"));
				dataModelJanit.setPhaseBCurrent(Double.parseDouble(!Lib.isBlank(words.get(11)) ? words.get(11) : "0.001"));
				
				dataModelJanit.setPhaseCCurrent(Double.parseDouble(!Lib.isBlank(words.get(12)) ? words.get(12) : "0.001"));
				
				dataModelJanit.setTotalCurrent(Double.parseDouble(!Lib.isBlank(words.get(13)) ? words.get(13) : "0.001"));
				dataModelJanit.setPhaseAPower(Double.parseDouble(!Lib.isBlank(words.get(14)) ? words.get(14) : "0.001"));
				dataModelJanit.setPhaseBPower(Double.parseDouble(!Lib.isBlank(words.get(15)) ? words.get(15) : "0.001"));
				dataModelJanit.setPhaseCPower(Double.parseDouble(!Lib.isBlank(words.get(16)) ? words.get(16) : "0.001"));
				dataModelJanit.setTotalPower(power);
				dataModelJanit.setPhaseAApparentPower(Double.parseDouble(!Lib.isBlank(words.get(18)) ? words.get(18) : "0.001"));
				dataModelJanit.setPhaseBApparentPower(Double.parseDouble(!Lib.isBlank(words.get(19)) ? words.get(19) : "0.001"));
				dataModelJanit.setPhaseCApparentPower(Double.parseDouble(!Lib.isBlank(words.get(20)) ? words.get(20) : "0.001"));
				dataModelJanit.setTotalApparentPower(Double.parseDouble(!Lib.isBlank(words.get(21)) ? words.get(21) : "0.001"));
				dataModelJanit.setPhaseAReactivePower(Double.parseDouble(!Lib.isBlank(words.get(22)) ? words.get(22) : "0.001"));
				
				dataModelJanit.setPhaseBReactivePower(Double.parseDouble(!Lib.isBlank(words.get(23)) ? words.get(23) : "0.001"));
				dataModelJanit.setPhaseCReactivePower(Double.parseDouble(!Lib.isBlank(words.get(24)) ? words.get(24) : "0.001"));
				dataModelJanit.setTotalReactivePower(Double.parseDouble(!Lib.isBlank(words.get(25)) ? words.get(25) : "0.001"));
				dataModelJanit.setPhaseAPowerFactor(Double.parseDouble(!Lib.isBlank(words.get(26)) ? words.get(26) : "0.001"));
				dataModelJanit.setPhaseBPowerFactor(Double.parseDouble(!Lib.isBlank(words.get(27)) ? words.get(27) : "0.001"));
				dataModelJanit.setPhaseCPowerFactor(Double.parseDouble(!Lib.isBlank(words.get(28)) ? words.get(28) : "0.001"));
				dataModelJanit.setPowerFactor(Double.parseDouble(!Lib.isBlank(words.get(29)) ? words.get(29) : "0.001"));
				dataModelJanit.setFrequency(Double.parseDouble(!Lib.isBlank(words.get(30)) ? words.get(30) : "0.001"));
				dataModelJanit.setTotalForwardActiveEnergy(energy);
				dataModelJanit.setTotalReverseActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(32)) ? words.get(32) : "0.001"));
				
				dataModelJanit.setTotalForwardReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(33)) ? words.get(33) : "0.001"));
				dataModelJanit.setTotalReverseReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(34)) ? words.get(34) : "0.001"));
				dataModelJanit.setPhaseAForwardActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(35)) ? words.get(35) : "0.001"));
				
				
				dataModelJanit.setPhaseBForwardActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(36)) ? words.get(36) : "0.001"));
				dataModelJanit.setPhaseCForwardActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(37)) ? words.get(37) : "0.001"));
				dataModelJanit.setPhaseAReverseActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(38)) ? words.get(38) : "0.001"));
				dataModelJanit.setPhaseBReverseActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(39)) ? words.get(39) : "0.001"));
				dataModelJanit.setPhaseCReverseActiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(40)) ? words.get(40) : "0.001"));
				dataModelJanit.setPhaseAForwardReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(41)) ? words.get(41) : "0.001"));
				
				dataModelJanit.setPhaseBForwardReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(42)) ? words.get(42) : "0.001"));
				dataModelJanit.setPhaseCForwardReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(43)) ? words.get(43) : "0.001"));
				dataModelJanit.setPhaseAReverseReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(44)) ? words.get(44) : "0.001"));
				dataModelJanit.setPhaseBReverseReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(45)) ? words.get(45) : "0.001"));
				dataModelJanit.setPhaseCReverseReactiveEnergy(Double.parseDouble(!Lib.isBlank(words.get(46)) ? words.get(46) : "0.001"));
				
				// set custom field nvmActivePower and nvmActiveEnergy
				dataModelJanit.setNvmActivePower(power);
				dataModelJanit.setNvmActiveEnergy(energy);
				
				return dataModelJanit;
				
			} else {
				return new ModelJanitzaUmg604proEntity();
			}
			
			
		} catch (Exception ex) {
			log.error("insert", ex);
			return new ModelJanitzaUmg604proEntity();
		}
	}


	/**
	 * @description insert data from datalogger to model_meter_ion_8600
	 * @author long.pham
	 * @since 2023-01-16
	 * @param data from datalogger
	 */
	
	public boolean insertModelJanitzaUmg604pro(ModelJanitzaUmg604proEntity obj) {
		try {
			if(obj.getOffset_data_old() !=0) {
				Double energy = obj.getNvmActiveEnergy();
				energy = energy + obj.getOffset_data_old();
				obj.setNvmActiveEnergy(energy);
				obj.setTotalForwardActiveEnergy(energy);
			}
			
			ModelJanitzaUmg604proEntity dataObj = (ModelJanitzaUmg604proEntity) queryForObject("ModelJanitzaUmg604pro.getLastRow", obj);
			// filter data 
			if(dataObj != null && ( obj.getError() > 0 || obj.getNvmActiveEnergy() < dataObj.getNvmActiveEnergy() || obj.getNvmActiveEnergy() == 0.001 || obj.getNvmActiveEnergy() < 0) ) {
				obj.setNvmActiveEnergy(dataObj.getNvmActiveEnergy());
				obj.setTotalForwardActiveEnergy(dataObj.getNvmActiveEnergy());
			}
						
			 double measuredProduction = 0;
			 if(dataObj != null && dataObj.getId_device() > 0 && dataObj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() > 0 && obj.getNvmActiveEnergy() != 0.001 ) {
				 measuredProduction = obj.getNvmActiveEnergy() - dataObj.getNvmActiveEnergy(); 
			 }
			 

			 obj.setMeasuredProduction(measuredProduction);
			 
			 Object insertId = insert("ModelJanitzaUmg604pro.insertModelJanitzaUmg604pro", obj);
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
