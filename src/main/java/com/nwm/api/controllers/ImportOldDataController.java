/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.controllers;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import javax.validation.Valid;
import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.nwm.api.entities.FileImportDataOldEntity;
import com.nwm.api.entities.ImportOldDataEntity;
import com.nwm.api.entities.SiteEntity;
import com.nwm.api.services.BatchJobService;
import com.nwm.api.services.ImportOldDataService;
import com.nwm.api.utils.Constants;
import com.nwm.api.utils.Lib;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/import-old-data")
public class ImportOldDataController extends BaseController {

//	Integer count = 0;
	/**
	 * @description Get all site by id_employee
	 * @author long.pham
	 * @since 2022-12-21
	 * @return data 
	 */
	@PostMapping("/get-list-all-site-by-employee")
	public Object getDropdownList(@RequestBody ImportOldDataEntity obj) {
		try {
			ImportOldDataService service = new ImportOldDataService();
			List data = service.getAllSiteByEmployeeId(obj);
			return this.jsonResult(true, Constants.GET_SUCCESS_MSG, data, data.size());
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}
	
	
	
	
	/**
	 * @description Get list site by id_customer
	 * @author long.pham
	 * @since 2020-10-09
	 * @param id_customer
	 * @return data (status, message, array, total_row
	 */
	@PostMapping("/get-list-device-by-site-id")
	public Object getList(@RequestBody ImportOldDataEntity obj) {
		try {
			if (obj.getLimit() == 0) {
				obj.setLimit(Constants.MAXRECORD);
			}
			ImportOldDataService service = new ImportOldDataService();
			List data = service.getAllDeviceBySiteId(obj);
			return this.jsonResult(true, Constants.GET_SUCCESS_MSG, data, data.size());
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}
	

	
	/**
	 * @description Get list file import
	 * @author long.pham
	 * @since 2023-08-03
	 * @param id_customer
	 * @return data (status, message, array, total_row
	 */
	@PostMapping("/get-list-file-import")
	public Object getListFileImport(@RequestBody FileImportDataOldEntity obj) {
		try {
			if (obj.getLimit() == 0) {
				obj.setLimit(Constants.MAXRECORD);
			}
			ImportOldDataService service = new ImportOldDataService();
			List data = service.getListFileImport(obj);
			int totalRecord = service.getTotalRecord(obj);
			
			return this.jsonResult(true, Constants.GET_SUCCESS_MSG, data, totalRecord);
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}
	
	/**
	 * @description save error level
	 * @author long.pham
	 * @since 2021-02-26
	 * @param  screen_mode = 0:add, 1:edit
	 */

	@PostMapping("/save")
	public Object save(@Valid @RequestBody ImportOldDataEntity obj) {
		try {
			ImportOldDataService service = new ImportOldDataService();
			ImportOldDataEntity data = service.insertImportOldData(obj);
			if (data != null) {
				if (data.getRow() > 0) {
					return this.jsonResult(false, "Error date format at row " + data.getRow(), null, 0);
				}
				return this.jsonResult(true, Constants.SAVE_SUCCESS_MSG, data, 1);
			} else {
				return this.jsonResult(false, Constants.SAVE_ERROR_MSG, null, 0);
			}
		} catch (Exception e) {
			// log error
			return this.jsonResult(false, Constants.SAVE_ERROR_MSG, e, 0);
		}
	}
	
	
	/**
	 * @description save error level
	 * @author long.pham
	 * @since 2023-05-31
	 * @param screen_mode = 0:add, 1:edit
	 */

	@PostMapping("/save-upload")
	public Object saveUpload(@Valid @RequestBody ImportOldDataEntity obj) {
		FileImportDataOldEntity itemRow = new FileImportDataOldEntity();
		try {
			ImportOldDataService service = new ImportOldDataService();
			String fileName = "";
			String saveDir = "";

			if (!Lib.isBlank(obj.getFile_upload())) {
				saveDir = uploadRootPath() + "/" + Lib.getReourcePropValue(Constants.appConfigFileName,
						Constants.uploadFilePathConfigKeyOlddata);
				String newFileName = obj.getFilename().toString(); 
				newFileName = newFileName.substring(0, newFileName.indexOf('.'));
				fileName = newFileName + "-"+ randomAlphabetic(16);
				String saveFileName = Lib.uploadFromBase64(obj.getFile_upload(), fileName, saveDir);
				String fileUrl = saveDir + "/" + saveFileName;
				if(fileUrl == null){ return this.jsonResult(true, "Upload file error", null, 1); }
				// Insert file to database
				FileImportDataOldEntity item = new FileImportDataOldEntity();
				item.setId_device(obj.getId_device());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		        // set UTC time zone by using SimpleDateFormat class  
		        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
		        item.setTime_upload(sdf.format(new Date()));
		        item.setFilename(saveFileName);
		        item.setId_employee(obj.getId_employee());
		        item.setStatus(1);
		        FileImportDataOldEntity result = service.insertFileImportDataOld(item);
		        itemRow.setId(result.getId());
		        
		        String domainCronJob = Lib.getReourcePropValue(Constants.appConfigFileName, Constants.domainCronJob);
				String privateKey = Lib.getReourcePropValue(Constants.appConfigFileName, Constants.privateKey);
		
				String url = domainCronJob + "/api-server/import-old-data/run-event-upload-old-data?token=" + privateKey + "&id="+ result.getId();
				String command = "curl -X GET " + url;
				Runtime.getRuntime().exec(command);
				return this.jsonResult(true, "Upload file import old data successfully", null, 1);
			} else {
				return this.jsonResult(false, Constants.SAVE_ERROR_MSG, null, 0);
			}

		} 
		
		catch (Exception e) {
			// log error
			return this.jsonResult(false, Constants.SAVE_ERROR_MSG, e, 0);
		}
	}
	
	
	
	/**
	 * @description run event upload old data 
	 * @author long.pham
	 * @since 2023-08-03
	 * @return {}
	 */
	@GetMapping("/run-event-upload-old-data")
	@ResponseBody
	public Object renderDataVirtualDevice(@RequestParam Map<String, Object> params) {
		try {
			String privateKey = Lib.getReourcePropValue(Constants.appConfigFileName, Constants.privateKey);
			String token = (String) params.get("token");
			if(token == null || token == "" || !token.equals(privateKey)) {
				return this.jsonResult(false, Constants.GET_ERROR_MSG, null, 0);
			}
			
			String id = (String) params.get("id");
			int idFile = 0;
			
			if(id != null && Integer.parseInt(id) > 0 ) {
				idFile = Integer.parseInt(id);
			}
			
			FileImportDataOldEntity itemFile = new FileImportDataOldEntity();
			itemFile.setId(idFile);
			
			ImportOldDataService service = new ImportOldDataService();
			FileImportDataOldEntity dataFile = service.getDetailFileUploadDataOld(itemFile);
			ImportOldDataEntity obj = new ImportOldDataEntity();
			
			HashSet<String> setDate = new HashSet<String> ();
			
			if( dataFile.getId() >= 0 ) {
				obj.setId_site(dataFile.getId_site());
				obj.setTable_name(dataFile.getDatatablename());
				obj.setDevice_group_table(dataFile.getDevice_group_table());
				
				String saveDir = uploadRootPath() + "/" + Lib.getReourcePropValue(Constants.appConfigFileName, Constants.uploadFilePathConfigKeyOlddata);
				String fileUrl = saveDir + "/" + dataFile.getFilename();
				try (InputStream is = new FileInputStream(fileUrl); ReadableWorkbook wb = new ReadableWorkbook(is)) {
					StopWatch watch = new StopWatch();
					watch.start();
	
					wb.getSheets().forEach(sheet -> {
						try (Stream<Row> rows = sheet.openStream()) {
							long rowTotal = sheet.openStream().mapToLong(e -> 1L).sum();
							rows.skip(1).forEach(r -> {
								if(r.getRowNum() == 2) { itemFile.setStart_date(r.getCellText(1).toString()); }
								
								List<Object> result = new ArrayList<Object>();
								HashMap<String, String> rowItem = new HashMap<String, String>();
								if (Lib.isDateValid(r.getCellText(1).toString())) {
									rowItem.put("time", r.getCellText(1).toString());
									rowItem.put("local_time", r.getCellText(1).toString());
									rowItem.put("datatablename", dataFile.getDatatablename());
									String time = r.getCellText(1).toString();
									
									if (time == null || !time.matches("((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]:([0-5][0-9]|[6][0])$")) {
										setDate.clear();
							    	}

									String[] parts = time.split(" ");
									String start = parts[0];
									setDate.add(start);
									
									switch (dataFile.getDevice_group_table()) {
									
									case "model_ae_refusol":
										rowItem.put("id_device", !r.getCellText(2).toString().equals("") ? r.getCellText(2).toString() : "0");
										rowItem.put("error", !r.getCellText(3).toString().equals("") ? r.getCellText(3).toString() : "0");
										rowItem.put("low_alarm", !r.getCellText(4).toString().equals("") ? r.getCellText(4).toString() : "0");
										rowItem.put("high_alarm", !r.getCellText(5).toString().equals("") ? r.getCellText(5).toString() : "0");
										rowItem.put("ACPower", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("ACVoltageAverageRMS", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("ACVoltage1RMS", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ACVoltage2RMS", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("ACVoltage3RMS", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("ACCurrentSum", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("ACCurrent1", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("ACCurrent2", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("ACCurrent3", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("ACFrequency1", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("ACFrequency2", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("ACFrequency3", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("DCPower", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("DCVoltage", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("DCCurrent", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("HeatSink", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("Interior", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("Irradiation", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("Panel", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("DailyYield", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("TotalYield", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("OperatingHours", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("Status", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("ErrorMessageCode", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										
										break;
									
									case "model_sunny_central_class9775_inverter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("Fault", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("OperatingState", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("Status", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("EventNumber", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("LifekWhTotal", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("PVCurrent", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("PVVoltage", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("PVPower", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("ACPower", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("ACVoltageAB", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("ACVoltageBC", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("ACVoltageCA", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("ACCurrent", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("ACCurrentL1", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("ACCurrentL2", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("ACCurrentL3", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("ACFrequency", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										
										rowItem.put("ReactivePower", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("ApparentPower", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("PowerFactor", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("ReactivePowerMode", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("PowerFactorFeedback", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("OperatingMode", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("ACPowerLimit", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("ACVoltage", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("HeatSinkTemp", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("InteriorTemperature", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("ExternalTemperature", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
	
										break;
										
									case "model_satcon_powergate_225_inverter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("Fault1", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("Fault2", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("Fault3", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("Fault4", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("GridStatus", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("Status6", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("Status7", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("PCSState", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("DCInputPower", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("DC_Link_Volts", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("DCInputVoltage", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("DCInputCurrent", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("OutputKVAR", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("OutputKW", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("OutputKVA", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("Line_Volts_A_TEST", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("Line_Volts_B_TEST", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										
										rowItem.put("Line_Volts_C_TEST", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("Line_Amps_A_TEST", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("Line_Amps_B_TEST", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("Line_Amps_C_TEST", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("NeutralCurrent", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("StopCode", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("KWHlow", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("KWH", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("PowerFactor", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("LineFreq", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("OutputPowerLimit", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										
										break;
									case "model_meter_ion_8600":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("VlnA", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("VlnB", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("VlnC", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("VlnAve", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("VllAb", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("VllAc", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("VllCa", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("IA", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("IB", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("IC", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("IAve", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("VUnbal", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("IUnbal", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("Freq", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("I4", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("kWA", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("kWB", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										
										rowItem.put("kWC", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("kWTot", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("kVARA", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("kVARB", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("kVARC", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("kVARTot", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("KVAA", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("KVAB", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("KVAC", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("KVATot", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("PFSignA", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("PFSignB", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("PFSignC", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("PFSignTot", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("VIIAveMx", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("IAveMx", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("kWTotMx", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("kVARTotMx", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("kVATotMx", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("FreqMx", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("VIIAveMn", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										
										rowItem.put("IAveMn", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("FreqMn", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("kWSdDelRec", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("kVASdDelRec", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("kVARSdDelRec", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("kWSdMxDR", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("kVASdMxDR", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("kVARSdMxDR", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("PhaseRev", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("kWhDel", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("kWhRec", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("kWhDelRec", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("kWhDel_Rec", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("kVARhDel", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("kVARhRec", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("kVARhDelRec", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("kVARhDel_Rec", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										rowItem.put("kVAhDelRec", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString() : "0.001");
										rowItem.put("V1THDMx", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString() : "0.001");
										rowItem.put("V2THDMx", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString() : "0.001");
										rowItem.put("V3THDMx", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString() : "0.001");
										rowItem.put("I1THDMx", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString() : "0.001");
	
										break;
									
									case "model_meter_ion_8600v1":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("Frequency", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("VAN", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("VlnC", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("VlnAve", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("VllAb", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("VllAc", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("VllCa", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("IA", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("IB", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("IC", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("IAve", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("VUnbal", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("IUnbal", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("Freq", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("I4", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("kWA", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("kWB", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										
										rowItem.put("kWC", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("kWTot", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("kVARA", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("kVARB", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("kVARC", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("kVARTot", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("KVAA", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("KVAB", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("KVAC", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("KVATot", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("PFSignA", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("PFSignB", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("PFSignC", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("PFSignTot", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("VIIAveMx", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("IAveMx", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("kWTotMx", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("kVARTotMx", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("kVATotMx", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("FreqMx", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("VIIAveMn", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										
										rowItem.put("IAveMn", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("FreqMn", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("kWSdDelRec", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("kVASdDelRec", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("kVARSdDelRec", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("kWSdMxDR", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("kVASdMxDR", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("kVARSdMxDR", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("PhaseRev", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("kWhDel", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("kWhRec", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("kWhDelRec", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("kWhDel_Rec", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("kVARhDel", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("kVARhRec", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("kVARhDelRec", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("kVARhDel_Rec", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										rowItem.put("kVAhDelRec", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString() : "0.001");
										rowItem.put("V1THDMx", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString() : "0.001");
										rowItem.put("V2THDMx", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString() : "0.001");
										rowItem.put("V3THDMx", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString() : "0.001");
										rowItem.put("I1THDMx", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString() : "0.001");
	
										break;	
										
									case "model_hukseflux_sr30d1_deviceclass_v0":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("IrradianceTcs", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("IrradianceUs", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("SensorBodyTemperature", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("SensorElectricalResistance", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("ScalingFactorIrradiance", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("ScalingFactorTemperature", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("SensorSerialNumber", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("SensorSensitivity", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("SensorCalibrationDate", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("InternalHumidity", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("TiltAngle", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("TiltAngleaverage", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("FanSpeedRPM", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("HeaterCurrent", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("FanCurrent", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
	
										break;
									case "model_imtsolar_tmodul_class8006":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("ModuleTemperature", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
	
										break;
									case "model_xantrex_gt100_250_500":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("VAB", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("VBC", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("VCA", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("CurrentA", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("CurrentB", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("CurrentC", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("ReadPower", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("PVVoltage", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("PVCurrent", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("PVPower", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("GridFrequency", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("SystemState", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("GoalState", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("FaultCode", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("AccumulatedEnergy", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("RMatrixTemp", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("LMatrixTemp", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("IntakeAirTemperature", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
	
										break;
	
									case "model_tti_tracker":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("Mode", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("SubMode", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("MotorStatus", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("ReadAngle", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("SetAngle", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("OptimalAngle", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("WindSpeed", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("TTiTime", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("MotorFault", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("RemoteInterfaceFault", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("InclinometerFault", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("ModbusAddress", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("FirmwareVersion", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("Units", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("InclinometerOffset", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("MotorStopDelay", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("CoastAngle", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("MaxRotationWest", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("MaxRotationEast", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("SoftAngleLimitsEnabled", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("MotorMonitorSampleTime", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("MotorMonitorMinAngle", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("EnableMotorMonitor", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("DeadBand", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("NightTimeStowAltitude", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("NightTimeStowAngle", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("PoleSpacing", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("ModuleWidth", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("MotorPolarity", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("InclinometerPolarity", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("Latitude", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("Longitude", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("LoggingInterval", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("HelicalVarationAngle", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
										rowItem.put("DriveArmSlope", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString(): "0.001");
										rowItem.put("WindConstant", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString(): "0.001");
										rowItem.put("WindStowSpeed", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString(): "0.001");
										rowItem.put("WindStowTime", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString(): "0.001");
	
										break;
									case "model_solectria_sgi_226ivt":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("DCVoltage", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("ACPowerOutput", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("ACGridFrequency", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ACPowerStageCurrent", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("L1toL2ACVoltage", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("L2toL3ACVoltage", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("L1toL3ACVoltage", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("PhaseSequence", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("CumulativeACEnergy", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("CumulativeOngridHours", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("FanOntimeHours", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("ACContactorCycles", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("SlaveID", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("CriticalAlarms", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("InformativeAlarms", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
	
										break;
									case "model_pv_powered_35_50_260_500kw_inverter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										
										rowItem.put("InverterIDASCIICHAR0001", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR0203", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR0405", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR0607", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR0809", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR1011", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR1213", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("InverterIDASCIICHAR1415", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("FirmwareVersionASCIICHAR0001", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("FirmwareVersionASCIICHAR0203", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("FirmwareVersionASCIICHAR0405", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("FirmwareVersionASCIICHAR0607", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("MapVersion", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("InverterConfiguration", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR0001", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR0203", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR0405", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR0607", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR0809", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR1011", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR1213", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR1415", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR1617", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("InverterSerialASCIICHAR1819", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("VoltageAN", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("VoltageBN", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("VoltageCN", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("CurrentA", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("CurrentB", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("CurrentC", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("DCInputVoltage", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("DCInputCurrent", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("LineFrequency", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("OutputGeneration", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
										rowItem.put("TotalEnergyGeneration", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString(): "0.001");
										rowItem.put("PVInputVoltage", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString(): "0.001");
										rowItem.put("InputGenerationCalculated", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString(): "0.001");
										rowItem.put("InverterOperatingStatus", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString(): "0.001");
										rowItem.put("MainFault", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString(): "0.001");
										rowItem.put("DriveFault", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString(): "0.001");
										rowItem.put("VoltageFault", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString(): "0.001");
										rowItem.put("GridFault", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString(): "0.001");
										rowItem.put("TemperatureFault", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString(): "0.001");
										rowItem.put("SystemFault", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString(): "0.001");
										rowItem.put("SystemWarnings", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString(): "0.001");
										rowItem.put("PVMStatusCodes", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString(): "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString(): "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString(): "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString(): "0.001");
	
										break;
									case "model_lufft_class8020":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("RelativeHumidityActual", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("RelativeHumidityMin", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("RelativeHumidityMax", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("RelativeHumidityAvg", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("RelativeAirPressureActual", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("RelativeAirPressureMin", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("RelativeAirPressureMax", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("RelativeAirPressureAvg", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("WindDirectionActual", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("WindDirectionMin", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("WindDirectionMax", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("WindDirectionVct", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("WindDirectionFast", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("WindDirectionCompassCorrected", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("Compass", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("PrecipitationType", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("WindMeasurementQuality", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("IrradianceActual", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("IrradianceMin", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("IrradianceMax", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("IrradianceAvg", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("AirTemperatureActual", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("AirTemperatureMin", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("AirTemperatureMax", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("AirTemperatureAvg", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("DewPointActual", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("DewPointMin", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("DewPointMax", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("DewPointAvg", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("WindChillTemperature", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("HeatingTemperatureWind", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("HeatingTemperatureR2S", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("WindSpeedActual", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("WindSpeedMin", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
										rowItem.put("WindSpeedMax", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString(): "0.001");
										rowItem.put("WindSpeedAvg", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString(): "0.001");
										rowItem.put("WindSpeedVct", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString(): "0.001");
										rowItem.put("WindSpeedFast", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString(): "0.001");
										rowItem.put("PrecipitationQuantityAbsolute", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString(): "0.001");
										rowItem.put("PrecipitationQuantityDifferential", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString(): "0.001");
										rowItem.put("PrecipitationIntensity", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString(): "0.001");
										rowItem.put("AbsoluteHumidityActual", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString(): "0.001");
										rowItem.put("AbsoluteHumidityMin", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString(): "0.001");
										rowItem.put("AbsoluteHumidityMax", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString(): "0.001");
										rowItem.put("AbsoluteHumidityAvg", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString(): "0.001");
										rowItem.put("MixingRatioActual", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString(): "0.001");
										rowItem.put("MixingRatioMin", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString(): "0.001");
										rowItem.put("MixingRatioMax", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString(): "0.001");
										rowItem.put("MixingRatioAvg", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString(): "0.001");
										rowItem.put("AbsoluteAirPressureActual", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString(): "0.001");
										rowItem.put("AbsoluteAirPressureMin", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString(): "0.001");
										rowItem.put("AbsoluteAirPressureMax", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString(): "0.001");
										rowItem.put("AbsoluteAirPressureAvg", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString(): "0.001");
										rowItem.put("WindSpeedStandardDeviation", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString(): "0.001");
										rowItem.put("WindDirectionStandardDeviation", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString(): "0.001");
										rowItem.put("WetBulbTemperature", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString(): "0.001");
										rowItem.put("SpecificEnthalpy", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString(): "0.001");
										rowItem.put("AirDensityActual", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString(): "0.001");
										rowItem.put("LeafWetnessActual", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString(): "0.001");
										rowItem.put("LeafWetnessMin", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString(): "0.001");
										rowItem.put("LeafWetnessMax", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString(): "0.001");
										rowItem.put("LeafWetnessAvg", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString(): "0.001");
										rowItem.put("LeafWetnessState", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString(): "0.001");
										rowItem.put("ExternalTemperature", !r.getCellText(69).toString().equals("") ? r.getCellText(69).toString(): "0.001");
										rowItem.put("WindValueQualityFast", !r.getCellText(70).toString().equals("") ? r.getCellText(70).toString(): "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(71).toString().equals("") ? r.getCellText(71).toString(): "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(72).toString().equals("") ? r.getCellText(72).toString(): "0.001");
	
										break;
									case "model_lufft_ws501_umb_weather":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("RelativeHumidityActual", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("RelativeHumidityMin", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("RelativeHumidityMax", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("RelativeHumidityAvg", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("RelativeAirPressureActual", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("RelativeAirPressureMin", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("RelativeAirPressureMax", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("RelativeAirPressureAvg", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("WindDirectionActual", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("WindDirectionMin", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("WindDirectionMax", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("WindDirectionVct", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("WindDirectionFast",!r.getCellText(18).toString().equals("") ?  r.getCellText(18).toString(): "0.001");
										rowItem.put("WindDirectionCompassCorrected", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("Compass", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("WindMeasurementQuality", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("PrecipitationType", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("GlobalRadiation", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("GlobalRadiation2", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("GlobalRadiation3", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("GlobalRadiation4", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("AirTemperatureCActual", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("AirTemperatureCMin", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("AirTemperatureCMax", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("AirTemperatureCAvg", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("DewPointActual", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("DewPointMin", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("DewPointMax", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("DewPointAvg", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("WindChillTemperature", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("HeatingTemperatureWind", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("HeatingTemperatureR2S", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("WindSpeedActual", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("WindSpeedMin", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
										rowItem.put("WindSpeedMax", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString(): "0.001");
										rowItem.put("WindSpeedAvg", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString(): "0.001");
										rowItem.put("WindSpeedVct", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString(): "0.001");
										rowItem.put("WindSpeedFast", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString(): "0.001");
										rowItem.put("PrecipitationAbsolute", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString(): "0.001");
										rowItem.put("PrecipitationDifferential", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString(): "0.001");
										rowItem.put("PrecipitationIntensity", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString(): "0.001");
										rowItem.put("AirTemperatureFActual", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString(): "0.001");
										rowItem.put("AirTemperatureFMin", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString(): "0.001");
										rowItem.put("AirTemperatureFMax", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString(): "0.001");
										rowItem.put("AirTemperatureFAvg", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString(): "0.001");
										rowItem.put("ExternalTemperatureC", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString(): "0.001");
										rowItem.put("ExternalTemperatureF", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString(): "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString(): "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString(): "0.001");
										
										break;
									case "model_abb_trio_class6210":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("AuroraType", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("GridType", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("TransformerType", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("StatesByte0", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("StatesByte1", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("StatesByte2", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("StatesByte3", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("StatesByte4", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("TotalEnergy", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("GridVoltage", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("GridCurrent", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("GridPower", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("Frequency", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("Input1Power", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("Input1Voltage", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("Input1Current", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("Input2Power", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("Input2Voltage", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("Input2Current", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("InverterTemperature", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("BooseterTemperature", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("IslolationResistance", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
	
										break;
									case "model_elkor_production_meter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("ActivePowerTotal", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("ReactivePowerTotal", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("ApparentPowerTotal", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("VoltageAverage", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("VoltageLLAverage", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("CurrentAverage", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("SystemPowerFactor", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("SystemFrequency", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("VoltageAverageAngle", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("SystemQuadrant", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("VoltageA", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("VoltageB", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("VoltageC", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("VoltageAB", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("VoltageBC", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("VoltageAC", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("CurrentA", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("CurrentB", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("CurrentC", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("ActivePowerA", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("ActivePowerB", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("ActivePowerC", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("ReactivePowerA", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("ReactivePowerB", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("ReactivePowerC", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("ApparentPowerA", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("ApparentPowerB", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("ApparentPowerC", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("PowerFactorA", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("PowerFactorB", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("PowerFactorC", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("VoltageAngleAB", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("VoltageAngleBC", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("VoltageAngleCA", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
										rowItem.put("QuadrantA", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString(): "0.001");
										rowItem.put("QuadrantB", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString(): "0.001");
										rowItem.put("QuadrantC", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString(): "0.001");
										rowItem.put("SlidingWindowPower", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString(): "0.001");
										rowItem.put("NetTotalEnergy", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString(): "0.001");
										rowItem.put("TotalNetApparentEnergy", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString(): "0.001");
										rowItem.put("TotalImportEnergy", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString(): "0.001");
										rowItem.put("TotalExportEnergy", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString(): "0.001");
										rowItem.put("TotalImportApparentEnergy", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString(): "0.001");
										rowItem.put("TotalExportApparentEnergy", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString(): "0.001");
										rowItem.put("Q1TotalReactiveEnergy", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString(): "0.001");
										rowItem.put("Q2TotalReactiveEnergy", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString(): "0.001");
										rowItem.put("Q3TotalReactiveEnergy", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString(): "0.001");
										rowItem.put("Q4TotalReactiveEnergy", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString(): "0.001");
										rowItem.put("Q1Q2TotalInductiveReactiveEnergy", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString(): "0.001");
										rowItem.put("Q3Q4TotalCapacitiveReactiveEnergy", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString(): "0.001");
										rowItem.put("NetEnergyA", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString(): "0.001");
										rowItem.put("NetEnergyB", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString(): "0.001");
										rowItem.put("NetEnergyC", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString(): "0.001");
										rowItem.put("NetApparentEnergyA", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString(): "0.001");
										rowItem.put("NetApparentEnergyB", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString(): "0.001");
										rowItem.put("NetApparentEnergyC", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString(): "0.001");
										rowItem.put("ImportEnergyA", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString(): "0.001");
										rowItem.put("ImportEnergyB", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString(): "0.001");
										rowItem.put("ImportEnergyC", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString(): "0.001");
										rowItem.put("ExportEnergyA", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString(): "0.001");
										rowItem.put("ExportEnergyB", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString(): "0.001");
										rowItem.put("ExportEnergyC", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString(): "0.001");
										rowItem.put("ImportApparentEnergyA", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString(): "0.001");
										rowItem.put("ImportApparentEnergyB", !r.getCellText(69).toString().equals("") ? r.getCellText(69).toString(): "0.001");
										rowItem.put("ImportApparentEnergyC", !r.getCellText(70).toString().equals("") ? r.getCellText(70).toString(): "0.001");
										rowItem.put("ExportApparentEnergyA", !r.getCellText(71).toString().equals("") ? r.getCellText(71).toString(): "0.001");
										rowItem.put("ExportApparentEnergyB", !r.getCellText(72).toString().equals("") ? r.getCellText(72).toString(): "0.001");
										rowItem.put("ExportApparentEnergyC", !r.getCellText(73).toString().equals("") ? r.getCellText(73).toString(): "0.001");
										rowItem.put("Q1ReactiveEnergyA", !r.getCellText(74).toString().equals("") ? r.getCellText(74).toString(): "0.001");
										rowItem.put("Q1ReactiveEnergyB", !r.getCellText(75).toString().equals("") ? r.getCellText(75).toString(): "0.001");
										rowItem.put("Q1ReactiveEnergyC", !r.getCellText(76).toString().equals("") ? r.getCellText(76).toString(): "0.001");
										rowItem.put("Q2ReactiveEnergyA", !r.getCellText(77).toString().equals("") ? r.getCellText(77).toString(): "0.001");
										rowItem.put("Q2ReactiveEnergyB", !r.getCellText(78).toString().equals("") ? r.getCellText(78).toString(): "0.001");
										rowItem.put("Q2ReactiveEnergyC", !r.getCellText(79).toString().equals("") ? r.getCellText(79).toString(): "0.001");
	
										rowItem.put("Q3ReactiveEnergyA", !r.getCellText(80).toString().equals("") ? r.getCellText(80).toString(): "0.001");
										rowItem.put("Q3ReactiveEnergyB", !r.getCellText(81).toString().equals("") ? r.getCellText(81).toString(): "0.001");
										rowItem.put("Q3ReactiveEnergyC", !r.getCellText(82).toString().equals("") ? r.getCellText(82).toString(): "0.001");
										rowItem.put("Q4ReactiveEnergyA", !r.getCellText(83).toString().equals("") ? r.getCellText(83).toString(): "0.001");
										rowItem.put("Q4ReactiveEnergyB", !r.getCellText(84).toString().equals("") ? r.getCellText(84).toString(): "0.001");
										rowItem.put("Q4ReactiveEnergyC", !r.getCellText(85).toString().equals("") ? r.getCellText(85).toString(): "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(86).toString().equals("") ? r.getCellText(86).toString(): "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(87).toString().equals("") ? r.getCellText(87).toString(): "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(88).toString().equals("") ? r.getCellText(88).toString(): "0.001");
	
										break;
									case "model_w_kipp_zonen_rt1":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("DeviceType", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("DataModelVersion", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("OperationalMode", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("StatusFlags", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("SunPOATempComp", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("PanelTemperature", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("ExtPowerSensor", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("BatchNumber", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("SerialNumber", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("CalibrationDateYYMMDD", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
	
										break;
									case "model_elkor_wattson_pv_meter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("TotalEnergyConsumption", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString(): "0.001");
										rowItem.put("TotalRealPower", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString(): "0.001");
										rowItem.put("TotalReactivePower", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString(): "0.001");
										rowItem.put("TotalApparentPower", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString(): "0.001");
										rowItem.put("AverageVoltageLN", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString(): "0.001");
										rowItem.put("AverageVoltageLL", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString(): "0.001");
										rowItem.put("AverageCurrent", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString(): "0.001");
										rowItem.put("TotalSystemPowerFactor", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString(): "0.001");
										rowItem.put("Frequency", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString(): "0.001");
										rowItem.put("SlidingWindowRealPowerDemand", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString(): "0.001");
										rowItem.put("VoltageAN", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString(): "0.001");
										rowItem.put("VoltageBN", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString(): "0.001");
										rowItem.put("VoltageCN", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString(): "0.001");
										rowItem.put("VoltageAB", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString(): "0.001");
										rowItem.put("VoltageBC", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString(): "0.001");
										rowItem.put("VoltageAC", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString(): "0.001");
										rowItem.put("CurrentA", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString(): "0.001");
										rowItem.put("CurrentB", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString(): "0.001");
										rowItem.put("CurrentC", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString(): "0.001");
										rowItem.put("RealPowerA", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString(): "0.001");
										rowItem.put("RealPowerB", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString(): "0.001");
										rowItem.put("RealPowerC", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString(): "0.001");
										rowItem.put("ReactivePowerA", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString(): "0.001");
										rowItem.put("ReactivePowerB", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString(): "0.001");
										rowItem.put("ReactivePowerC", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString(): "0.001");
										rowItem.put("ApparentPowerA", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString(): "0.001");
										rowItem.put("ApparentPowerB", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString(): "0.001");
										rowItem.put("ApparentPowerC", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString(): "0.001");
										rowItem.put("PowerFactorA", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString(): "0.001");
										rowItem.put("PowerFactorB", !r.getCellText(45).toString().equals("") ? r.getCellText(35).toString(): "0.001");
										rowItem.put("PowerFactorC", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString(): "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(47).toString().equals("") ? r.getCellText(37).toString(): "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString(): "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString(): "0.001");
	
										break;
									case "model_satcon_pvs357_inverter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("Software_Identification_Number", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("Fault_Word1", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("Fault_Word2", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("Fault_Word3", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("Fault_Word4", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("Fault_Word5", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("Fault_Word6", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("Fault_Word7", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("Number_of_Faults", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("Program_Checksum", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("Parameter_Checksum", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("DC_Input_Volts", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("DC_Link_Volts", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("DC_Link_Amps", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("DC_Ground_Current", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("Line_Amps_A", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("Line_Amps_B", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("Line_Amps_C", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("Line_Amps_Average", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("Neutral_Current", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("Line_Volts_A", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("Line_Volts_B", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("Line_Volts_C", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("Line_Volts_Average", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("Line_Voltage_Unbalance", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("Line_Current_Unbalance", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("Input_kW", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("Output_kw", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("Output_kvar", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("Output_kva", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("Power_Factor", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("Ground_Impedance", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("String_Amps1", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("String_Amps2", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("String_Amps3", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("String_Amps4", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("String_Amps5", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("String_Amps6", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("String_Amps7", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("String_Amps8", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("String_Amps9", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("String_Amps10", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("String_Amps11", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("String_Amps12", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("String_Amps13", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("String_Amps14", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("String_Amps15", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("String_Amps16", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("String_Amps17", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("String_Amps18", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("String_Amps19", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("String_Amps20", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("String_Amps21", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("String_Amps22", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("String_Amps23", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										rowItem.put("String_Amps24", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString() : "0.001");
										rowItem.put("String_Amps25", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString() : "0.001");
										rowItem.put("String_Amps26", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString() : "0.001");
										rowItem.put("String_Amps27", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString() : "0.001");
										rowItem.put("String_Amps28", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString() : "0.001");
										rowItem.put("String_Amps29", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString() : "0.001");
										rowItem.put("String_Amps30", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString() : "0.001");
										rowItem.put("String_Amps31", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString() : "0.001");
										rowItem.put("String_Amps32", !r.getCellText(69).toString().equals("") ? r.getCellText(69).toString() : "0.001");
										rowItem.put("String_Amps_Average", !r.getCellText(70).toString().equals("") ? r.getCellText(70).toString() : "0.001");
										rowItem.put("String_kwh1", !r.getCellText(71).toString().equals("") ? r.getCellText(71).toString() : "0.001");
										rowItem.put("String_kwh2", !r.getCellText(72).toString().equals("") ? r.getCellText(72).toString() : "0.001");
										rowItem.put("String_kwh3", !r.getCellText(73).toString().equals("") ? r.getCellText(73).toString() : "0.001");
										rowItem.put("String_kwh4", !r.getCellText(74).toString().equals("") ? r.getCellText(74).toString() : "0.001");
										rowItem.put("String_kwh5", !r.getCellText(75).toString().equals("") ? r.getCellText(75).toString() : "0.001");
										rowItem.put("String_kwh6", !r.getCellText(76).toString().equals("") ? r.getCellText(76).toString() : "0.001");
										rowItem.put("String_kwh7", !r.getCellText(77).toString().equals("") ? r.getCellText(77).toString() : "0.001");
										rowItem.put("String_kwh8", !r.getCellText(78).toString().equals("") ? r.getCellText(78).toString() : "0.001");
										rowItem.put("String_kwh9", !r.getCellText(79).toString().equals("") ? r.getCellText(79).toString() : "0.001");
	
										rowItem.put("String_kwh10", !r.getCellText(80).toString().equals("") ? r.getCellText(80).toString() : "0.001");
										rowItem.put("String_kwh11", !r.getCellText(81).toString().equals("") ? r.getCellText(81).toString() : "0.001");
										rowItem.put("String_kwh12", !r.getCellText(82).toString().equals("") ? r.getCellText(82).toString() : "0.001");
										rowItem.put("String_kwh13", !r.getCellText(83).toString().equals("") ? r.getCellText(83).toString() : "0.001");
										rowItem.put("String_kwh14", !r.getCellText(84).toString().equals("") ? r.getCellText(84).toString() : "0.001");
										rowItem.put("String_kwh15", !r.getCellText(85).toString().equals("") ? r.getCellText(85).toString() : "0.001");
										rowItem.put("String_kwh16", !r.getCellText(86).toString().equals("") ? r.getCellText(86).toString() : "0.001");
										rowItem.put("String_kwh17", !r.getCellText(87).toString().equals("") ? r.getCellText(87).toString() : "0.001");
										rowItem.put("String_kwh18", !r.getCellText(88).toString().equals("") ? r.getCellText(88).toString() : "0.001");
										rowItem.put("String_kwh19", !r.getCellText(89).toString().equals("") ? r.getCellText(89).toString() : "0.001");
										rowItem.put("String_kwh20", !r.getCellText(90).toString().equals("") ? r.getCellText(90).toString() : "0.001");
										rowItem.put("String_kwh21", !r.getCellText(91).toString().equals("") ? r.getCellText(91).toString() : "0.001");
										rowItem.put("String_kwh22", !r.getCellText(92).toString().equals("") ? r.getCellText(92).toString() : "0.001");
										rowItem.put("String_kwh23", !r.getCellText(93).toString().equals("") ? r.getCellText(93).toString() : "0.001");
										rowItem.put("String_kwh24", !r.getCellText(94).toString().equals("") ? r.getCellText(94).toString() : "0.001");
										rowItem.put("String_kwh25", !r.getCellText(95).toString().equals("") ? r.getCellText(95).toString() : "0.001");
										rowItem.put("String_kwh26", !r.getCellText(96).toString().equals("") ? r.getCellText(96).toString() : "0.001");
										rowItem.put("String_kwh27", !r.getCellText(97).toString().equals("") ? r.getCellText(97).toString() : "0.001");
										rowItem.put("String_kwh28", !r.getCellText(98).toString().equals("") ? r.getCellText(98).toString() : "0.001");
										rowItem.put("String_kwh29", !r.getCellText(99).toString().equals("") ? r.getCellText(99).toString() : "0.001");
										rowItem.put("String_kwh30", !r.getCellText(100).toString().equals("") ? r.getCellText(100).toString() : "0.001");
										rowItem.put("String_kwh31", !r.getCellText(101).toString().equals("") ? r.getCellText(101).toString() : "0.001");
										rowItem.put("String_kwh32", !r.getCellText(102).toString().equals("") ? r.getCellText(102).toString() : "0.001");
										rowItem.put("String_kwh_Average", !r.getCellText(103).toString().equals("") ? r.getCellText(103).toString() : "0.001");
										rowItem.put("Total_kwh", !r.getCellText(104).toString().equals("") ? r.getCellText(104).toString() : "0.001");
										rowItem.put("Total_mwh", !r.getCellText(105).toString().equals("") ? r.getCellText(105).toString() : "0.001");
										rowItem.put("kwh_Today", !r.getCellText(106).toString().equals("") ? r.getCellText(106).toString() : "0.001");
										rowItem.put("kwh_Yesterday", !r.getCellText(107).toString().equals("") ? r.getCellText(107).toString() : "0.001");
										rowItem.put("Total_kwh7_days", !r.getCellText(108).toString().equals("") ? r.getCellText(108).toString() : "0.001");
										rowItem.put("Total_kwh30_days", !r.getCellText(109).toString().equals("") ? r.getCellText(109).toString() : "0.001");
										rowItem.put("Average_kwh7_days", !r.getCellText(110).toString().equals("") ? r.getCellText(110).toString() : "0.001");
										rowItem.put("Average_kwh30_Days", !r.getCellText(111).toString().equals("") ? r.getCellText(111).toString() : "0.001");
										rowItem.put("Average_Line_Frequency", !r.getCellText(112).toString().equals("") ? r.getCellText(112).toString() : "0.001");
										rowItem.put("Average_Line_Frequency_Error", !r.getCellText(113).toString().equals("") ? r.getCellText(113).toString() : "0.001");
										rowItem.put("FPGA_Identification_Number", !r.getCellText(114).toString().equals("") ? r.getCellText(114).toString() : "0.001");
										rowItem.put("DC_Input_Voltage_Timer", !r.getCellText(115).toString().equals("") ? r.getCellText(115).toString() : "0.001");
										rowItem.put("AC_Line_Voltage_Timer", !r.getCellText(116).toString().equals("") ? r.getCellText(116).toString() : "0.001");
										rowItem.put("Operating_State", !r.getCellText(117).toString().equals("") ? r.getCellText(117).toString() : "0.001");
										rowItem.put("Internal_Air_Temperature", !r.getCellText(118).toString().equals("") ? r.getCellText(118).toString() : "0.001");
										rowItem.put("Inverter_Air_Temperature", !r.getCellText(119).toString().equals("") ? r.getCellText(119).toString() : "0.001");
										rowItem.put("Heatsink_Temperature1", !r.getCellText(120).toString().equals("") ? r.getCellText(120).toString() : "0.001");
										rowItem.put("Heatsink_Temperature2", !r.getCellText(121).toString().equals("") ? r.getCellText(121).toString() : "0.001");
										rowItem.put("Heatsink_Temperature3", !r.getCellText(122).toString().equals("") ? r.getCellText(122).toString() : "0.001");
										rowItem.put("Heatsink_Temperature4", !r.getCellText(123).toString().equals("") ? r.getCellText(123).toString() : "0.001");
										rowItem.put("Heatsink_Temperature5", !r.getCellText(124).toString().equals("") ? r.getCellText(124).toString() : "0.001");
										rowItem.put("Heatsink_Temperature6", !r.getCellText(125).toString().equals("") ? r.getCellText(125).toString() : "0.001");
										rowItem.put("Heatsink_Maximum_Temparature1", !r.getCellText(126).toString().equals("") ? r.getCellText(126).toString() : "0.001");
										rowItem.put("Fan_Speed_Command1", !r.getCellText(127).toString().equals("") ? r.getCellText(127).toString() : "0.001");
										rowItem.put("Heatsink_Maximum_Temperature2", !r.getCellText(128).toString().equals("") ? r.getCellText(128).toString() : "0.001");
										rowItem.put("Fan_Speed_Command2", !r.getCellText(129).toString().equals("") ? r.getCellText(129).toString() : "0.001");
										rowItem.put("Number_of_Temperature_Feedbacks", !r.getCellText(130).toString().equals("") ? r.getCellText(130).toString() : "0.001");
										rowItem.put("Serial_number_word1", !r.getCellText(131).toString().equals("") ? r.getCellText(131).toString() : "0.001");
										rowItem.put("Serial_number_word2", !r.getCellText(132).toString().equals("") ? r.getCellText(132).toString() : "0.001");
										rowItem.put("Serial_number_word3", !r.getCellText(133).toString().equals("") ? r.getCellText(133).toString() : "0.001");
										rowItem.put("Serial_number_word4", !r.getCellText(134).toString().equals("") ? r.getCellText(134).toString() : "0.001");
										rowItem.put("Number_of_Strings", !r.getCellText(135).toString().equals("") ? r.getCellText(135).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(136).toString().equals("") ? r.getCellText(136).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(137).toString().equals("") ? r.getCellText(137).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(138).toString().equals("") ? r.getCellText(138).toString() : "0.001");
										
										break;
									case "model_veris_industries_e51c2_power_meter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("AccumulatedRealEnergyNet", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("RealEnergyQuadrants14Import", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("RealEnergyQuadrants23Export", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ReactiveEnergyQuadrant1", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("ReactiveEnergyQuadrant2", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("ReactiveEnergyQuadrant3", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("ReactiveEnergyQuadrant4", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("ApparentEnergyNet", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("ApparentEnergyQuadrants14", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("ApparentEnergyQuadrants23", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("TotalNetInstantaneousRealPower", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("TotalNetInstantaneousReactivePower", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("TotalNetInstantaneousApparentPower", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("TotalPowerFactor", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("VoltageLL3pAve", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("VoltageLN3pAve", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("Current3pAve", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("Frequency", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("TotalRealPowerPresentDemand", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("TotalReactivePowerPresentDemand", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("TotalApparentPowerPresentDemand", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("TotalRealPowerMaxDemandImport", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("TotalReactivePowerMaxDemandImport", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("TotalApparentPowerMaxDemandImport", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("TotalRealPowerMaxDemandExport", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("TotalReactivePowerMaxDemandExport", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("TotalApparentPowerMaxDemandExport", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseAImport", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseBImport", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseCImport", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseAExport", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseBExport", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("AccumulatedRealEnergyPhaseCExport", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("AccumulatedQ1ReactiveEnergyPhaseAImport",
												!r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("AccumulatedQ1ReactiveEnergyPhaseBImport",
												!r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("AccumulatedQ1ReactiveEnergyPhaseCImport",
												!r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("AccumulatedQ2ReactiveEnergyPhaseAImport",
												!r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("AccumulatedQ2ReactiveEnergyPhaseBImport",
												!r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("AccumulatedQ2ReactiveEnergyPhaseCImport",
												!r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("AccumulatedQ3ReactiveEnergyPhaseAExport",
												!r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("AccumulatedQ3ReactiveEnergyPhaseBExport",
												!r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("AccumulatedQ3ReactiveEnergyPhaseCExport",
												!r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("AccumulatedQ4ReactiveEnergyPhaseAExport",
												!r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("AccumulatedQ4ReactiveEnergyPhaseBExport",
												!r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("AccumulatedQ4ReactiveEnergyPhaseCExport",
												!r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseAImport",
												!r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseBImport",
												!r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseCImport",
												!r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseAExport",
												!r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseBExport",
												!r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("AccumulatedApparentEnergyPhaseCExport",
												!r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("RealPowerPhaseA", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("RealPowerPhaseB", !r.getCellText(68).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("RealPowerPhaseC", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("ReactivePowerPhaseA", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										rowItem.put("ReactivePowerPhaseB", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString() : "0.001");
										rowItem.put("ReactivePowerPhaseC", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString() : "0.001");
										rowItem.put("ApparentPowerPhaseA", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString() : "0.001");
										rowItem.put("ApparentPowerPhaseB", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString() : "0.001");
										rowItem.put("ApparentPowerPhaseC", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString() : "0.001");
										rowItem.put("PowerFactorPhaseA", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString() : "0.001");
										rowItem.put("PowerFactorPhaseB", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString() : "0.001");
										rowItem.put("PowerFactorPhaseC", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString() : "0.001");
										rowItem.put("VoltagePhaseAB", !r.getCellText(69).toString().equals("") ? r.getCellText(69).toString() : "0.001");
										rowItem.put("VoltagePhaseBC", !r.getCellText(70).toString().equals("") ? r.getCellText(70).toString() : "0.001");
										rowItem.put("VoltagePhaseAC", !r.getCellText(71).toString().equals("") ? r.getCellText(71).toString() : "0.001");
										rowItem.put("VoltagePhaseAN", !r.getCellText(72).toString().equals("") ? r.getCellText(72).toString() : "0.001");
										rowItem.put("VoltagePhaseBN", !r.getCellText(73).toString().equals("") ? r.getCellText(73).toString() : "0.001");
										rowItem.put("VoltagePhaseCN", !r.getCellText(74).toString().equals("") ? r.getCellText(74).toString() : "0.001");
										rowItem.put("CurrentPhaseA", !r.getCellText(75).toString().equals("") ? r.getCellText(75).toString() : "0.001");
										rowItem.put("CurrentPhaseB", !r.getCellText(76).toString().equals("") ? r.getCellText(76).toString() : "0.001");
										rowItem.put("CurrentPhaseC", !r.getCellText(77).toString().equals("") ? r.getCellText(77).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(78).toString().equals("") ? r.getCellText(78).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(79).toString().equals("") ? r.getCellText(79).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(80).toString().equals("") ? r.getCellText(80).toString() : "0.001");
	
										break;
									case "model_chint_solectria_inverter_class9725":
										rowItem.put("id_device", !r.getCellText(2).toString().equals("") ? r.getCellText(2).toString() : "0");
										rowItem.put("error", !r.getCellText(3).toString().equals("") ? r.getCellText(3).toString() : "0");
										rowItem.put("low_alarm", !r.getCellText(4).toString().equals("") ? r.getCellText(4).toString() : "0");
										rowItem.put("high_alarm", !r.getCellText(5).toString().equals("") ? r.getCellText(5).toString() : "0");
										rowItem.put("PowerOnOff", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("PActiveSet", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("PFactorSet", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("PReactiveSet", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("GridVMax", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("GridVmaxTripT", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("GridVMin", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("GridVminTripT", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("GridFMax", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("GridFMin", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("GridFTripT", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("ActivePower", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("PowerFactor", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("StartDelayTime", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("Risomin", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("PVStartVol", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("DCIMax", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("TambientMax", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("TmoduleMax", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("OffsetDiffMax", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("GridVolUnbalance", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("SoftPowerStep", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("TotalEnergyToEnergy", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("TotalEnergyToday", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("InverterEfficiency", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("PowerFactor1", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("MaxActivePowerToday", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("RunTimeToGrid", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("AC_ActivePower", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("AC_ApparentPower", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("GridVoltageUab", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("GridVoltageUbc", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("GridVoltageUca", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("GridA_PhaseCurrent", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("GridB_PhaseCurrent", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("GridC_PhaseCurrent", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("PV1_Voltage", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("PV1_Current", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("PV2_Voltage", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("PV2_Current", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("PV3_Voltage", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("PV3_Current", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("Grid_Frequency", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("ModuleTemp", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("InternalTemp", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("TransformerTemp", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("InverterModeCode", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("PermanentFaultCode", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("WarnCode", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("FaultCode0", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("FaultCode1", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("FaultCode2", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										
										break;
									case "model_rt1_class30000":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("device_type", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("data_model_version", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("operational_mode", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("status_flags", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("sensor1_data", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("panel_temperature", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("external_power_sensor", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("calibration_date", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("error_code", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("protocol_error", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("batch_number", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("serial_number", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("software_version", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("hardware_version", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("node_id", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
	
										break;
									case "model_advanced_energy_solaron":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("today_kwh", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("ytd_kwh_total", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("life_kwh_total", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ytd_kwh", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("life_kwh", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("last_15min_kwh", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("timestamp_15minutes", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("last_restart", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("uptime", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("ac_power", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("ac_frequency", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("pv_voltage", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("pv_current", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("common_mode", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("ambient_temperature", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("coolant_temperature", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("reactor_temperature", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("cabinet_temperature", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("bus_voltage", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("ground_current", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("reactive_power", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("active_faults1", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("active_faults2", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("active_faults3", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("status", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("warnings1", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("warnings2_reserved", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("warnings3_reserved", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("limits", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("year", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("month", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("day", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("hour", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("minutes", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("seconds", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("current_time", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										
	
										break;
									case "model_imtsolar_class8000":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("irradiance", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("tcell", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
	
										break;
									case "model_pvp_inverter":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("total_kwh_delivered", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("volts_a_l_n", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("volts_b_l_n", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("volts_c_l_n", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("current_a", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("current_b", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("current_c", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("dc_output_voltage", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("dc_output_current", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("line_frenquency", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("line_kw", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("inverter_operating_status", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("inverter_fault_word0", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("inverter_fault_word1", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("inverter_fault_word2", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("data_comm_status", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
	
										break;
									case "model_ivt_solaron_ext":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("today_kwh", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("ytd_kwh_total", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("life_kwh_total", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ytd_kwh", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("life_kwh", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("last_15min_kwh", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("timestamp_15minutes", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("last_restart", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("uptime", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("ac_power", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("ac_frequency", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("pv_voltage", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("pv_current", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("common_mode", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("ambient_temperature", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("coolant_temperature", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("reactor_temperature", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("cabinet_temperature", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("bus_voltage", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("ground_current", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("reactive_power", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("active_faults1", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("active_faults2", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("active_faults3", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("status", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("warnings1", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("warnings2_reserved", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("warnings3_reserved", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("limits", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("year", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("month", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("day", !r.getCellText(36).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("hour", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("minutes", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("seconds", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("current_time", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("ac_current", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("requset_set_ac_power_limit", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("request_set_instantaneous_reactive_power_set_point", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("autostart_status", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("set_read_reactive_power_mode", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("set_read_p_ac_limit", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("set_read_instantaneous_reactive_power_set_point", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("set_read_power_factor_set_point", !r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("ac_power_ramp_rate", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("reactive_power_ramp_rate", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("power_factor_ramp_rate", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										break;
	
									case "model_kippzonen_rt1_class8009":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										
										rowItem.put("device_type", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("data_model_version", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("operational_mode", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("status_flags", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("sensor1_data", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("panel_temperature", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("external_power_sensor", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("calibration_date", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("error_code", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("protocol_error", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("batch_number", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("serial_number", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("software_version", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("hardware_version", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("node_id", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
	
										break;
									case "model_shark100":
										rowItem.put("id_device", !r.getCellText(2).toString().equals("") ? r.getCellText(2).toString() : "0");
										rowItem.put("error", !r.getCellText(3).toString().equals("") ? r.getCellText(3).toString() : "0");
										rowItem.put("low_alarm", !r.getCellText(4).toString().equals("") ? r.getCellText(4).toString() : "0");
										rowItem.put("high_alarm", !r.getCellText(5).toString().equals("") ? r.getCellText(5).toString() : "0");
										rowItem.put("volts_a_n", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("volts_b_n", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("volts_c_n", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("volts_a_b", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("volts_b_c", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("volts_c_a", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("amps_a", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("amps_b", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("amps_c", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("watts_3ph_total", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("vars_3ph_total", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("vas_3ph_total", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("power_factor_3ph_total", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("frequency", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("neutral_current", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("w_hours_received", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("w_hours_delivered", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("w_hours_net", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("w_hours_total", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("var_hours_positive", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("var_hours_negative", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("var_hours_net", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("var_hours_total", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("va_hours_total", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("amps_a_average", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("amps_b_average", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("amps_c_average", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("positive_watts_3ph_average", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("positive_vars_3ph_average", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("negative_watts_3ph_average", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("negative_vars_3ph_average", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("vas_3ph_average", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("positive_pf_3ph_average", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("negative_pf_3ph_average", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("volts_a_n_min", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("volts_b_n_min", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("volts_c_n_min", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("volts_a_b_min", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("volts_b_c_min", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("volts_c_a_min", !r.getCellText(45).toString().equals("") ? r.getCellText(45).toString() : "0.001");
										rowItem.put("amps_a_min_avg_demand", !r.getCellText(46).toString().equals("") ? r.getCellText(46).toString() : "0.001");
										rowItem.put("amps_b_min_avg_demand", !r.getCellText(47).toString().equals("") ? r.getCellText(47).toString() : "0.001");
										rowItem.put("amps_c_min_avg_demand", !r.getCellText(48).toString().equals("") ? r.getCellText(48).toString() : "0.001");
										rowItem.put("positive_watts_3ph_min_avg_demand", r.getCellText(49).toString().equals("") ? r.getCellText(49).toString() : "0.001");
										rowItem.put("positive_vars_3ph_min_avg_demand", !r.getCellText(50).toString().equals("") ? r.getCellText(50).toString() : "0.001");
										rowItem.put("negative_watts_3ph_min_avg_demand", !r.getCellText(51).toString().equals("") ? r.getCellText(51).toString() : "0.001");
										rowItem.put("negative_vars_3ph_min_avg_demand", !r.getCellText(52).toString().equals("") ? r.getCellText(52).toString() : "0.001");
										rowItem.put("vas_3ph_min_avg_demand", !r.getCellText(53).toString().equals("") ? r.getCellText(53).toString() : "0.001");
										rowItem.put("positive_pf_3ph_min_avg_demand", !r.getCellText(54).toString().equals("") ? r.getCellText(54).toString() : "0.001");
										rowItem.put("negative_pf_3ph_min_avg_demand", !r.getCellText(55).toString().equals("") ? r.getCellText(55).toString() : "0.001");
										rowItem.put("frequency_min", !r.getCellText(56).toString().equals("") ? r.getCellText(56).toString() : "0.001");
										rowItem.put("volts_a_n_max", !r.getCellText(57).toString().equals("") ? r.getCellText(57).toString() : "0.001");
										rowItem.put("volts_b_n_max", !r.getCellText(58).toString().equals("") ? r.getCellText(58).toString() : "0.001");
										rowItem.put("volts_c_n_max", !r.getCellText(59).toString().equals("") ? r.getCellText(59).toString() : "0.001");
										rowItem.put("volts_a_b_max", !r.getCellText(60).toString().equals("") ? r.getCellText(60).toString() : "0.001");
										rowItem.put("volts_b_c_max", !r.getCellText(61).toString().equals("") ? r.getCellText(61).toString() : "0.001");
										rowItem.put("volts_c_a_max", !r.getCellText(62).toString().equals("") ? r.getCellText(62).toString() : "0.001");
										rowItem.put("amps_a_max_avg_demand", !r.getCellText(63).toString().equals("") ? r.getCellText(63).toString() : "0.001");
										rowItem.put("amps_b_max_avg_demand", !r.getCellText(64).toString().equals("") ? r.getCellText(64).toString() : "0.001");
										rowItem.put("amps_c_max_avg_demand", !r.getCellText(65).toString().equals("") ? r.getCellText(65).toString() : "0.001");
										rowItem.put("positive_watts_3ph_max_avg_demand", !r.getCellText(66).toString().equals("") ? r.getCellText(66).toString() : "0.001");
										rowItem.put("positive_vars_3ph_max_avg_demand", !r.getCellText(67).toString().equals("") ? r.getCellText(67).toString() : "0.001");
										rowItem.put("negative_watts_3ph_max_avg_demand", !r.getCellText(68).toString().equals("") ? r.getCellText(68).toString() : "0.001");
										rowItem.put("negative_vars_3ph_max_avg_demand", !r.getCellText(69).toString().equals("") ? r.getCellText(69).toString() : "0.001");
										rowItem.put("vas_3ph_max_avg_demand", !r.getCellText(70).toString().equals("") ? r.getCellText(70).toString() : "0.001");
										rowItem.put("positive_pf_3ph_max_avg_demand", !r.getCellText(71).toString().equals("") ? r.getCellText(71).toString() : "0.001");
										rowItem.put("negative_pf_3ph_max_avg_demand", !r.getCellText(72).toString().equals("") ? r.getCellText(72).toString() : "0.001");
										rowItem.put("frequency_max", !r.getCellText(73).toString().equals("") ? r.getCellText(73).toString() : "0.001");
										rowItem.put("volts_a_n_thd", !r.getCellText(74).toString().equals("") ? r.getCellText(74).toString() : "0.001");
										rowItem.put("volts_b_n_thd", !r.getCellText(75).toString().equals("") ? r.getCellText(75).toString() : "0.001");
										rowItem.put("volts_c_n_thd", !r.getCellText(76).toString().equals("") ? r.getCellText(76).toString() : "0.001");
										rowItem.put("amps_a_thd", !r.getCellText(77).toString().equals("") ? r.getCellText(77).toString() : "0.001");
										rowItem.put("amps_b_thd", !r.getCellText(78).toString().equals("") ? r.getCellText(78).toString() : "0.001");
										rowItem.put("amps_c_thd", !r.getCellText(79).toString().equals("") ? r.getCellText(79).toString() : "0.001");
										rowItem.put("phase_a_current_0th", !r.getCellText(80).toString().equals("") ? r.getCellText(80).toString() : "0.001");
										rowItem.put("phase_a_current_1st", !r.getCellText(81).toString().equals("") ? r.getCellText(81).toString() : "0.001");
										rowItem.put("phase_a_current_2nd", !r.getCellText(82).toString().equals("") ? r.getCellText(82).toString() : "0.001");
										rowItem.put("phase_a_current_3rd", !r.getCellText(83).toString().equals("") ? r.getCellText(83).toString() : "0.001");
										rowItem.put("phase_a_current_4th", !r.getCellText(84).toString().equals("") ? r.getCellText(84).toString() : "0.001");
										rowItem.put("phase_a_current_5th", !r.getCellText(85).toString().equals("") ? r.getCellText(85).toString() : "0.001");
										rowItem.put("phase_a_current_6th", !r.getCellText(86).toString().equals("") ? r.getCellText(86).toString() : "0.001");
										rowItem.put("phase_a_current_7th", !r.getCellText(87).toString().equals("") ? r.getCellText(87).toString() : "0.001");
										rowItem.put("phase_a_voltage_0th", !r.getCellText(88).toString().equals("") ? r.getCellText(88).toString() : "0.001");
										rowItem.put("phase_a_voltage_1st", !r.getCellText(89).toString().equals("") ? r.getCellText(89).toString() : "0.001");
										rowItem.put("phase_a_voltage_2nd", !r.getCellText(90).toString().equals("") ? r.getCellText(90).toString() : "0.001");
										rowItem.put("phase_a_voltage_3rd", !r.getCellText(91).toString().equals("") ? r.getCellText(91).toString() : "0.001");
										rowItem.put("phase_b_current_0th", !r.getCellText(92).toString().equals("") ? r.getCellText(92).toString() : "0.001");
										rowItem.put("phase_b_current_1st", !r.getCellText(93).toString().equals("") ? r.getCellText(93).toString() : "0.001");
										rowItem.put("phase_b_current_2nd", !r.getCellText(94).toString().equals("") ? r.getCellText(94).toString() : "0.001");
										rowItem.put("phase_b_current_3rd", !r.getCellText(95).toString().equals("") ? r.getCellText(95).toString() : "0.001");
										rowItem.put("phase_b_current_4th", !r.getCellText(96).toString().equals("") ? r.getCellText(96).toString() : "0.001");
										rowItem.put("phase_b_current_5th", !r.getCellText(97).toString().equals("") ? r.getCellText(97).toString() : "0.001");
										rowItem.put("phase_b_current_6th", !r.getCellText(98).toString().equals("") ? r.getCellText(98).toString() : "0.001");
										rowItem.put("phase_b_current_7th", !r.getCellText(99).toString().equals("") ? r.getCellText(99).toString() : "0.001");
										rowItem.put("phase_b_voltage_0th", !r.getCellText(100).toString().equals("") ? r.getCellText(100).toString() : "0.001");
										rowItem.put("phase_b_voltage_1st", !r.getCellText(101).toString().equals("") ? r.getCellText(101).toString() : "0.001");
										rowItem.put("phase_b_voltage_2nd", !r.getCellText(102).toString().equals("") ? r.getCellText(102).toString() : "0.001");
										rowItem.put("phase_b_voltage_3rd", !r.getCellText(103).toString().equals("") ? r.getCellText(103).toString() : "0.001");
										rowItem.put("phase_c_current_0th", !r.getCellText(104).toString().equals("") ? r.getCellText(104).toString() : "0.001");
										rowItem.put("phase_c_current_1st", !r.getCellText(105).toString().equals("") ? r.getCellText(105).toString() : "0.001");
										rowItem.put("phase_c_current_2nd", !r.getCellText(106).toString().equals("") ? r.getCellText(106).toString() : "0.001");
										rowItem.put("phase_c_current_3rd", !r.getCellText(107).toString().equals("") ? r.getCellText(107).toString() : "0.001");
										rowItem.put("phase_c_current_4th", !r.getCellText(108).toString().equals("") ? r.getCellText(108).toString() : "0.001");
										rowItem.put("phase_c_current_5th", !r.getCellText(109).toString().equals("") ? r.getCellText(109).toString() : "0.001");
										rowItem.put("phase_c_current_6th", !r.getCellText(110).toString().equals("") ? r.getCellText(110).toString() : "0.001");
										rowItem.put("phase_c_current_7th", !r.getCellText(111).toString().equals("") ? r.getCellText(111).toString() : "0.001");
										rowItem.put("phase_c_voltage_0th", !r.getCellText(112).toString().equals("") ? r.getCellText(112).toString() : "0.001");
										rowItem.put("phase_c_voltage_1st", !r.getCellText(113).toString().equals("") ? r.getCellText(113).toString() : "0.001");
										rowItem.put("phase_c_voltage_2nd", !r.getCellText(114).toString().equals("") ? r.getCellText(114).toString() : "0.001");
										rowItem.put("phase_c_voltage_3rd", !r.getCellText(115).toString().equals("") ? r.getCellText(115).toString() : "0.001");
										rowItem.put("angle_phase_a_current", !r.getCellText(116).toString().equals("") ? r.getCellText(116).toString() : "0.001");
										rowItem.put("angle_phase_b_current", !r.getCellText(117).toString().equals("") ? r.getCellText(117).toString() : "0.001");
										rowItem.put("angle_phase_c_current", !r.getCellText(118).toString().equals("") ? r.getCellText(118).toString() : "0.001");
										rowItem.put("angle_volts_a_b", !r.getCellText(119).toString().equals("") ? r.getCellText(119).toString() : "0.001");
										rowItem.put("angle_volts_b_c", !r.getCellText(120).toString().equals("") ? r.getCellText(120).toString() : "0.001");
										rowItem.put("angle_volts_c_a", !r.getCellText(121).toString().equals("") ? r.getCellText(121).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(122).toString().equals("") ? r.getCellText(122).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(123).toString().equals("") ? r.getCellText(123).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(124).toString().equals("") ? r.getCellText(124).toString() : "0.001");
										break;
										
										
									case "model_xantrex_inverter":
										rowItem.put("id_device", !r.getCellText(2).toString().equals("") ? r.getCellText(2).toString() : "0");
										rowItem.put("error", !r.getCellText(3).toString().equals("") ? r.getCellText(3).toString() : "0");
										rowItem.put("low_alarm", !r.getCellText(4).toString().equals("") ? r.getCellText(4).toString() : "0");
										rowItem.put("high_alarm", !r.getCellText(5).toString().equals("") ? r.getCellText(5).toString() : "0");
										rowItem.put("VAB", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("VBC", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("VCA", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("CurrentA", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("CurrentB", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("CurrentC", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("ReadPower", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("PVVoltage", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("PVCurrent", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("PVPower", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("GridFrequency", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("SystemState", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("GoalState", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("FaultCode", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("kWh", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										break;
										
									case "model_poa_temp":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("T_AMB", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("T_MOD", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
	
										break;
										
									case "model_eri_weather_icp_class8050":
										rowItem.put("id_device", r.getCellText(2).toString());
										rowItem.put("error", r.getCellText(3).toString());
										rowItem.put("low_alarm", r.getCellText(4).toString());
										rowItem.put("high_alarm", r.getCellText(5).toString());
										rowItem.put("panel_temp", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("ambient_temp", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("wind_speed", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("solar_irradiation", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("wind_direction", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("nvm_irradiance", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("nvm_temperature", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										
										break;
										
									case "model_elster_a1700":
										rowItem.put("id_device", !r.getCellText(2).toString().equals("") ? r.getCellText(2).toString() : "0");
										rowItem.put("error", !r.getCellText(3).toString().equals("") ? r.getCellText(3).toString() : "0");
										rowItem.put("low_alarm", !r.getCellText(4).toString().equals("") ? r.getCellText(4).toString() : "0");
										rowItem.put("high_alarm", !r.getCellText(5).toString().equals("") ? r.getCellText(5).toString() : "0");
										rowItem.put("PhaseAVoltage", !r.getCellText(6).toString().equals("") ? r.getCellText(6).toString() : "0.001");
										rowItem.put("PhaseBVoltage", !r.getCellText(7).toString().equals("") ? r.getCellText(7).toString() : "0.001");
										rowItem.put("PhaseCVoltage", !r.getCellText(8).toString().equals("") ? r.getCellText(8).toString() : "0.001");
										rowItem.put("ABLineVoltage", !r.getCellText(9).toString().equals("") ? r.getCellText(9).toString() : "0.001");
										rowItem.put("BCLineVoltage", !r.getCellText(10).toString().equals("") ? r.getCellText(10).toString() : "0.001");
										rowItem.put("CALineVoltage", !r.getCellText(11).toString().equals("") ? r.getCellText(11).toString() : "0.001");
										rowItem.put("PhaseACurrent", !r.getCellText(12).toString().equals("") ? r.getCellText(12).toString() : "0.001");
										rowItem.put("PhaseBCurrent", !r.getCellText(13).toString().equals("") ? r.getCellText(13).toString() : "0.001");
										rowItem.put("PhaseCCurrent", !r.getCellText(14).toString().equals("") ? r.getCellText(14).toString() : "0.001");
										rowItem.put("PhaseAActivePower", !r.getCellText(15).toString().equals("") ? r.getCellText(15).toString() : "0.001");
										rowItem.put("PhaseBActivePower", !r.getCellText(16).toString().equals("") ? r.getCellText(16).toString() : "0.001");
										rowItem.put("PhaseCActivePower", !r.getCellText(17).toString().equals("") ? r.getCellText(17).toString() : "0.001");
										rowItem.put("TotalActivePower", !r.getCellText(18).toString().equals("") ? r.getCellText(18).toString() : "0.001");
										rowItem.put("TotalReactivePower", !r.getCellText(19).toString().equals("") ? r.getCellText(19).toString() : "0.001");
										rowItem.put("TotalApparentPower", !r.getCellText(20).toString().equals("") ? r.getCellText(20).toString() : "0.001");
										rowItem.put("TotalPowerFactor", !r.getCellText(21).toString().equals("") ? r.getCellText(21).toString() : "0.001");
										rowItem.put("GridFrequency", !r.getCellText(22).toString().equals("") ? r.getCellText(22).toString() : "0.001");
										rowItem.put("TotalForwardActiveEnergy", !r.getCellText(23).toString().equals("") ? r.getCellText(23).toString() : "0.001");
										rowItem.put("TotalReverseActiveEnergy", !r.getCellText(24).toString().equals("") ? r.getCellText(24).toString() : "0.001");
										rowItem.put("TotalForwardReactiveEnergy", !r.getCellText(25).toString().equals("") ? r.getCellText(25).toString() : "0.001");
										rowItem.put("TotalReverseReactiveEnergy", !r.getCellText(26).toString().equals("") ? r.getCellText(26).toString() : "0.001");
										rowItem.put("PhaseAForwardActivePower", !r.getCellText(27).toString().equals("") ? r.getCellText(27).toString() : "0.001");
										rowItem.put("PhaseBForwardActivePower", !r.getCellText(28).toString().equals("") ? r.getCellText(28).toString() : "0.001");
										rowItem.put("PhaseCForwardActivePower", !r.getCellText(29).toString().equals("") ? r.getCellText(29).toString() : "0.001");
										rowItem.put("PhaseAReverseActivePower", !r.getCellText(30).toString().equals("") ? r.getCellText(30).toString() : "0.001");
										rowItem.put("PhaseBReverseActivePower", !r.getCellText(31).toString().equals("") ? r.getCellText(31).toString() : "0.001");
										rowItem.put("PhaseCReverseActivePower", !r.getCellText(32).toString().equals("") ? r.getCellText(32).toString() : "0.001");
										rowItem.put("PhaseAForwardReactivePower", !r.getCellText(33).toString().equals("") ? r.getCellText(33).toString() : "0.001");
										rowItem.put("PhaseBForwardReactivePower", !r.getCellText(34).toString().equals("") ? r.getCellText(34).toString() : "0.001");
										rowItem.put("PhaseCForwardReactivePower", !r.getCellText(35).toString().equals("") ? r.getCellText(35).toString() : "0.001");
										rowItem.put("PhaseAReverseReactivePower", !r.getCellText(36).toString().equals("") ? r.getCellText(36).toString() : "0.001");
										rowItem.put("PhaseBReverseReactivePower", !r.getCellText(37).toString().equals("") ? r.getCellText(37).toString() : "0.001");
										rowItem.put("PhaseCReverseReactivePower", !r.getCellText(38).toString().equals("") ? r.getCellText(38).toString() : "0.001");
										rowItem.put("FlatForwardActiveEnergy", !r.getCellText(39).toString().equals("") ? r.getCellText(39).toString() : "0.001");
										rowItem.put("PeakForwardActiveEnergy", !r.getCellText(40).toString().equals("") ? r.getCellText(40).toString() : "0.001");
										rowItem.put("ValleyForwardActiveEnergy", !r.getCellText(41).toString().equals("") ? r.getCellText(41).toString() : "0.001");
										rowItem.put("FlatReverseActiveEnergy", !r.getCellText(42).toString().equals("") ? r.getCellText(42).toString() : "0.001");
										rowItem.put("PeakReverseActiveEnergy", !r.getCellText(43).toString().equals("") ? r.getCellText(43).toString() : "0.001");
										rowItem.put("ValleyReverseActiveEnergy", !r.getCellText(44).toString().equals("") ? r.getCellText(44).toString() : "0.001");
										rowItem.put("nvmActivePower", !r.getCellText(122).toString().equals("") ? r.getCellText(122).toString() : "0.001");
										rowItem.put("nvmActiveEnergy", !r.getCellText(123).toString().equals("") ? r.getCellText(123).toString() : "0.001");
										rowItem.put("MeasuredProduction", !r.getCellText(124).toString().equals("") ? r.getCellText(124).toString() : "0.001");
										break;
									}
									
									
									
	
									result.add(rowItem);
									obj.setDataList(result);
									ImportOldDataEntity insert = service.insertImportOldData(obj);
									
									// update file data 
									FileImportDataOldEntity updateFileImportRow = new FileImportDataOldEntity();
									updateFileImportRow.setId(itemFile.getId());
									updateFileImportRow.setStatus(2);
									updateFileImportRow.setTotal_row((int) rowTotal);
									updateFileImportRow.setTotal_complete_row(r.getRowNum());
									updateFileImportRow.setTotal_error_row(0);
									service.updateFileReportDataRow(updateFileImportRow);
								}
	
							});
	
						} catch (Exception e) {
							e.printStackTrace();
						}  finally {
							FileImportDataOldEntity getRowComplete = service.getDetailFileUploadDataOld(itemFile);
							
							obj.setId(dataFile.getId_device());
							obj.setId_device_type(dataFile.getId_device_type());
							
							for (String s : setDate) {	
								String[] year = s.split("-");
								obj.setYear(year[0]);
								obj.setStart_date(s + " 08:00:00");
								obj.setEnd_date(s + " 17:59:59");
								obj.setDatatablename(obj.getTable_name());
								obj.setId(dataFile.getId_device()); 
								
								service.insertSiteDataReport(obj);
								
							}
							// update file data 
							FileImportDataOldEntity updateRow = new FileImportDataOldEntity();
							updateRow.setId(getRowComplete.getId());
							updateRow.setStatus(3);
							updateRow.setTotal_row(getRowComplete.getTotal_row());
							updateRow.setTotal_complete_row(getRowComplete.getTotal_complete_row());
							updateRow.setTotal_error_row( getRowComplete.getTotal_row() - getRowComplete.getTotal_complete_row() );
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
							updateRow.setTime_complete(sdf.format(new Date()));
							service.updateFileReportDataRow(updateRow);
						}
						watch.stop();
					});
					
					// Run event update data virtual table
					String domainCronJob = Lib.getReourcePropValue(Constants.appConfigFileName, Constants.domainCronJob);
					DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date currentDate = new Date();
					Date date1 = null;
					Date date2 = null;
					String startDate = itemFile.getStart_date();
					int total_day = 2;
					if (startDate != null) {
						String endDate = simpleDateFormat.format(currentDate);
						date1 = simpleDateFormat.parse(startDate);
						date2 = simpleDateFormat.parse(endDate);
						long getDiff = date2.getTime() - date1.getTime();
						long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
						total_day = Integer.parseInt(String.valueOf(getDaysDiff));
					}
			
					String url = domainCronJob + "/api-server/virtual-device/render-data?token=" + privateKey + "&id_site="+ dataFile.getId_site() + "&total_day="+total_day;
					String command = "curl -X GET " + url;
					Runtime.getRuntime().exec(command);
					
					return this.jsonResult(true, "Import data successfully", null, 1);
				} catch (Exception e) {
					return this.jsonResult(false, Constants.SAVE_ERROR_MSG, e, 0);
				}
			}

			return this.jsonResult(true, Constants.GET_SUCCESS_MSG, null, 0);
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}
	
	
	
	
	/**
	 * @description delete role
	 * @author long.pham
	 * @since 2020-12-30
	 * @param id
	 * @return data (status, message, array, total_row
	 */
	@PostMapping("/delete-data-old")
	public Object deleteDataOld(@Valid @RequestBody FileImportDataOldEntity obj) {
		ImportOldDataService service = new ImportOldDataService();
		try {
			int result = service.deleteDataOld(obj);
			if (result == 1) {
				BatchJobService serviceBathJob = new BatchJobService();
				SiteEntity siteItem = new SiteEntity();
				siteItem.setId(obj.getId_site());
				siteItem.setStart_date(obj.getStart_date());
				siteItem.setEnd_date(obj.getEnd_date());
				siteItem.setTime_zone_value(obj.getTimezone_value());
				
				serviceBathJob.insertDataGenerateReport(siteItem);
				return this.jsonResult(true, Constants.DELETE_SUCCESS_MSG, obj, 1);
			}
			return this.jsonResult(false, Constants.DELETE_ERROR_MSG, null, 0);
		} catch (Exception e) {
			return this.jsonResult(false, Constants.DELETE_ERROR_MSG, e, 0);
		}
	}
	
	
}
