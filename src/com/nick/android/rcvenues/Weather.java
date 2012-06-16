package com.nick.android.rcvenues;

import java.io.Serializable;

/**
 * Weather describes a weather forecast for a single day.
 */
public class Weather implements Serializable {

   
	private static final long serialVersionUID = 1L;
	
	
	/**
     * Icon url
     */
    public String iconUrl = "";
    /**
     * Humidity
     */
    public String humidity = "0";
    /**
     * Temperature in Celsius
     */
    public String temperatureC = "0";
    /**
     * Temperature in Fahrenheit
     */
    public String temperatureF = "0";
    /**
     * Temperature in Celsius
     */
    public String minTempC = "0";
    /**
     * Temperature in Fahrenheit
     */
    public String minTempF = "0";
    /**
     * Temperature in Celsius
     */
    public String maxTempC = "0";
    /**
     * Temperature in Fahrenheit
     */
    public String maxTempF = "0";
    /**
     * Wind speed in kmph
     */
    public String windSpeedKmph = "0";
    /**
     * Wind speed in mph
     */
    public String windSpeedMph = "0";
    /**
     * Wind direction in degrees
     */
    public String windDirectionDegrees = "0";
    /**
     * Wind direction in compass points
     */
    public String windDirectionPoints = "N";
    /**
     * Weather description
     */
    public String description = "";
    
    public String precipitation = "";
}
