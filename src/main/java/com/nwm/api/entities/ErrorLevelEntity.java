/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorLevelEntity{
	
	private int id;
	private String name;
	private String description;
	private int status;
	private int is_delete;
	private String created_date;
	private String created_by;
	private String updated_date;
	private String updated_by;
	private String icon_alert;
	private String text;
	private int active;
	private int limit;
	private int offset;
	private int totalRecord;
	private String order_by;
	private String sort_column;
	private String keyword;
	private int screen_mode;
	private int id_icon;
	private String color;
	private String title_trans;
	
	
	
	
	public String getTitle_trans() {
		return title_trans;
	}
	public void setTitle_trans(String title_trans) {
		this.title_trans = title_trans;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the is_delete
	 */
	public int getIs_delete() {
		return is_delete;
	}
	/**
	 * @param is_delete the is_delete to set
	 */
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	/**
	 * @return the created_date
	 */
	public String getCreated_date() {
		return created_date;
	}
	/**
	 * @param created_date the created_date to set
	 */
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	/**
	 * @return the created_by
	 */
	public String getCreated_by() {
		return created_by;
	}
	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	/**
	 * @return the updated_date
	 */
	public String getUpdated_date() {
		return updated_date;
	}
	/**
	 * @param updated_date the updated_date to set
	 */
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	/**
	 * @return the updated_by
	 */
	public String getUpdated_by() {
		return updated_by;
	}
	/**
	 * @param updated_by the updated_by to set
	 */
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	/**
	 * @return the icon_alert
	 */
	public String getIcon_alert() {
		return icon_alert;
	}
	/**
	 * @param icon_alert the icon_alert to set
	 */
	public void setIcon_alert(String icon_alert) {
		this.icon_alert = icon_alert;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the active
	 */
	public int getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(int active) {
		this.active = active;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the order_by
	 */
	public String getOrder_by() {
		return order_by;
	}
	/**
	 * @param order_by the order_by to set
	 */
	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}
	/**
	 * @return the sort_column
	 */
	public String getSort_column() {
		return sort_column;
	}
	/**
	 * @param sort_column the sort_column to set
	 */
	public void setSort_column(String sort_column) {
		this.sort_column = sort_column;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the screen_mode
	 */
	public int getScreen_mode() {
		return screen_mode;
	}
	/**
	 * @param screen_mode the screen_mode to set
	 */
	public void setScreen_mode(int screen_mode) {
		this.screen_mode = screen_mode;
	}
	/**
	 * @return the id_icon
	 */
	public int getId_icon() {
		return id_icon;
	}
	/**
	 * @param id_icon the id_icon to set
	 */
	public void setId_icon(int id_icon) {
		this.id_icon = id_icon;
	}
	
	
}
