package com.ccrawler.ctrip.vo;

public class CtripArriveVO {

	private String flightNo;
	
	private String fromAirport;
	
	private String arriveAirport;
	
	private String planFly;
	
	private String planArrive;
	
	private String estimateFly;
	
	private String estimateArrive;

	private String crawlerTime;

	public String getCrawlerTime() {
		return crawlerTime;
	}

	public void setCrawlerTime(String crawlerTime) {
		this.crawlerTime = crawlerTime;
	}

	private String actualFly;

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getFromAirport() {
		return fromAirport;
	}

	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}

	public String getArriveAirport() {
		return arriveAirport;
	}

	public void setArriveAirport(String arriveAirport) {
		this.arriveAirport = arriveAirport;
	}

	public String getPlanFly() {
		return planFly;
	}

	public void setPlanFly(String planFly) {
		this.planFly = planFly;
	}

	public String getPlanArrive() {
		return planArrive;
	}

	public void setPlanArrive(String planArrive) {
		this.planArrive = planArrive;
	}

	public String getActualFly() {
		return actualFly;
	}

	public String getEstimateFly() {
		return estimateFly;
	}

	public void setEstimateFly(String estimateFly) {
		this.estimateFly = estimateFly;
	}

	public String getEstimateArrive() {
		return estimateArrive;
	}

	public void setEstimateArrive(String estimateArrive) {
		this.estimateArrive = estimateArrive;
	}

	public void setActualFly(String actualFly) {
		this.actualFly = actualFly;
	}

	public String getActualArrive() {
		return actualArrive;
	}

	public void setActualArrive(String actualArrive) {
		this.actualArrive = actualArrive;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String actualArrive;

	private String status;

}
