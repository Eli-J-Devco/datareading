/********************************************************
* Copyright 2020-2021 NEXT WAVE ENERGY MONITORING INC.
* All rights reserved.
* 
*********************************************************/
package com.nwm.api.services;
import com.maxmind.geoip.Location;

public class GeoLocation {
	
	private String countryCode;
    private String countryName;
    private String postalCode;
    private String city;
    private String region;
    private int areaCode;
    private int dmaCode;
    private int metroCode;

    private float latitude;
    private float longitude;

    public GeoLocation(String countryCode, String countryName, String postalCode, String city, String region,
                       int areaCode, int dmaCode, int metroCode, float latitude, float longitude) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.areaCode = areaCode;
        this.dmaCode = dmaCode;
        this.metroCode = metroCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public int getDmaCode() {
        return dmaCode;
    }

    public int getMetroCode() {
        return metroCode;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public static GeoLocation map(Location loc){
        return new GeoLocation(loc.countryCode, loc.countryName, loc.postalCode, loc.city, loc.region,
                loc.area_code, loc.dma_code, loc.metro_code, loc.latitude, loc.longitude);
    }

    @Override
//    public String toString() {
//        return "GeoLocation{" +
//                "countryCode='" + countryCode + '\'' +
//                ", countryName='" + countryName + '\'' +
//                ", postalCode='" + postalCode + '\'' +
//                ", city='" + city + '\'' +
//                ", region='" + region + '\'' +
//                ", areaCode=" + areaCode +
//                ", dmaCode=" + dmaCode +
//                ", metroCode=" + metroCode +
//                ", latitude=" + latitude +
//                ", longitude=" + longitude +
//                '}';
//    }
    public String toString() {
        return city + ", " + countryName;
    }
	
}
