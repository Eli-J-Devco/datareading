/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.controllers;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwm.api.entities.DeviceEntity;
import com.nwm.api.entities.ScadaChartingEntity;
import com.nwm.api.services.ScadaChartingService;
import com.nwm.api.services.SitesAnalyticsService;
import com.nwm.api.utils.Constants;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("scada/charting")
public class ScadaChartingController extends BaseController {
	
	/**
	 * @description get site detail
	 * @author Hung.Bui
	 * @since 2024-05-03
	 * @param obj { hash_id_site }
	 * @return data (status, message, array, total_row)
	 */
	@PostMapping("/site-detail")
	public Object getSiteDetail(@RequestBody ScadaChartingEntity obj) {
		try {
			ScadaChartingService service = new ScadaChartingService();
			ScadaChartingEntity data = service.getSiteDetail(obj);
			if (data != null) {
				return this.jsonResult(true, Constants.GET_SUCCESS_MSG, data, 1);
			} else {
				return this.jsonResult(false, Constants.GET_ERROR_MSG, null, 0);
			}
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}

	/**
	 * @description get devices list by site
	 * @author Hung.Bui
	 * @since 2024-05-03
	 * @param obj { hash_id_site }
	 * @return data (status, message, array, total_row)
	 */
	@PostMapping("/list-device-by-site")
	public Object getListDeviceBySite(@RequestBody DeviceEntity obj) {
		try {
			SitesAnalyticsService service = new SitesAnalyticsService();
			List data = service.getListDeviceBySite(obj);
			return this.jsonResult(true, Constants.GET_SUCCESS_MSG, data, data.size());
		} catch (Exception e) {
			log.error(e);
			return this.jsonResult(false, Constants.GET_ERROR_MSG, e, 0);
		}
	}
	
}
