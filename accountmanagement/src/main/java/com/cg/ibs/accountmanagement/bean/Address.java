package com.cg.ibs.accountmanagement.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
public class Address {

	@Id
	private long applicantId;

	@Column(name = "PERMANENT_HOUSE_NUMBER", nullable = false, length = 6)
	private String permanentHouseNumber;

	@Column(name = "PERMANENT_STREET_NAME", nullable = false, length = 15)
	private String permanentStreetName;

	@Column(name = "PERMANENT_AREA", nullable = false, length = 15)
	private String permanentArea;

	@Column(name = "PERMANENT_LANDMARK", nullable = true, length = 15)
	private String permanentLandmark;

	@Column(name = "PERMANENT_CITY", nullable = false, length = 15)
	private String permanentCity;

	@Column(name = "PERMANENT_STATE", nullable = false, length = 15)
	private String permanentState;

	@Column(name = "PERMANENT_COUNTRY", nullable = false, length = 15)
	private String permanentCountry;

	@Column(name = "PERMANENT_PINCODE", nullable = false, length = 6)
	private String permanentPincode;

	@Column(name = "CURRENT_HOUSE_NUMBER", nullable = false, length = 6)
	private String currentHouseNumber;

	@Column(name = "CURRENT_STREET_NAME", nullable = false, length = 15)
	private String currentStreetName;

	@Column(name = "CURRENT_AREA", nullable = false, length = 15)
	private String currentArea;

	@Column(name = "CURRENT_LANDMARK", nullable = true, length = 15)
	private String currentLandmark;

	@Column(name = "CURRENT_CITY", nullable = false, length = 15)
	private String currentCity;

	@Column(name = "CURRENT_STATE", nullable = false, length = 15)
	private String currentState;

	@Column(name = "CURRENT_COUNTRY", nullable = false, length = 15)
	private String currentCountry;

	@Column(name = "CURRENT_PINCODE", nullable = false, length = 6)
	private String currentPincode;

	public long getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(long applicantId) {
		this.applicantId = applicantId;
	}

	public String getPermanentHouseNumber() {
		return permanentHouseNumber;
	}

	public void setPermanentHouseNumber(String permanentHouseNumber) {
		this.permanentHouseNumber = permanentHouseNumber;
	}

	public String getPermanentStreetName() {
		return permanentStreetName;
	}

	public void setPermanentStreetName(String permanentStreetName) {
		this.permanentStreetName = permanentStreetName;
	}

	public String getPermanentArea() {
		return permanentArea;
	}

	public void setPermanentArea(String permanentArea) {
		this.permanentArea = permanentArea;
	}

	public String getPermanentLandmark() {
		return permanentLandmark;
	}

	public void setPermanentLandmark(String permanentLandmark) {
		this.permanentLandmark = permanentLandmark;
	}

	public String getPermanentCity() {
		return permanentCity;
	}

	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}

	public String getPermanentState() {
		return permanentState;
	}

	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}

	public String getPermanentCountry() {
		return permanentCountry;
	}

	public void setPermanentCountry(String permanentCountry) {
		this.permanentCountry = permanentCountry;
	}

	public String getPermanentPincode() {
		return permanentPincode;
	}

	public void setPermanentPincode(String permanentPincode) {
		this.permanentPincode = permanentPincode;
	}

	public String getCurrentHouseNumber() {
		return currentHouseNumber;
	}

	public void setCurrentHouseNumber(String currentHouseNumber) {
		this.currentHouseNumber = currentHouseNumber;
	}

	public String getCurrentStreetName() {
		return currentStreetName;
	}

	public void setCurrentStreetName(String currentStreetName) {
		this.currentStreetName = currentStreetName;
	}

	public String getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(String currentArea) {
		this.currentArea = currentArea;
	}

	public String getCurrentLandmark() {
		return currentLandmark;
	}

	public void setCurrentLandmark(String currentLandmark) {
		this.currentLandmark = currentLandmark;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getCurrentCountry() {
		return currentCountry;
	}

	public void setCurrentCountry(String currentCountry) {
		this.currentCountry = currentCountry;
	}

	public String getCurrentPincode() {
		return currentPincode;
	}

	public void setCurrentPincode(String currentPincode) {
		this.currentPincode = currentPincode;
	}

	public Address() {
		super();
	}

	@Override
	public String toString() {
		return "ApplicantId:" + applicantId + "\nPermanent Address:\n\tHouse Number: " + permanentHouseNumber
				+ "\n\tStreet Name=: " + permanentStreetName + "\n\tArea: " + permanentArea + "\n\tLandmark: "
				+ permanentLandmark + "\n\tCity: " + permanentCity + "\n\tState: " + permanentState + "\n\tCountry: "
				+ permanentCountry + "\n\tPincode: " + permanentPincode + "\nCurrent Address:\n\tHouse Number:"
				+ currentHouseNumber + "\n\tStreet Name: " + currentStreetName + "\n\tArea: " + currentArea
				+ "\n\tLandmark: " + currentLandmark + "\n\tCity: " + currentCity + "\n\tState: " + currentState
				+ "\n\tCountry: " + currentCountry + "\n\tPincode: " + currentPincode;
	}

}