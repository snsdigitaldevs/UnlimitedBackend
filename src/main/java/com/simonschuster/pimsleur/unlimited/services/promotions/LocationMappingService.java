package com.simonschuster.pimsleur.unlimited.services.promotions;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.simonschuster.pimsleur.unlimited.data.dto.price.LocationInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Objects;

@Service
public class LocationMappingService {
    private static DatabaseReader dbReader;
    private static final Logger logger = LoggerFactory.getLogger(LocationMappingService.class);

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    static {
        InputStream stream = LocationMappingService.class.getResourceAsStream("/data/GeoLite2-City.mmdb");
        try {
            dbReader = new DatabaseReader.Builder(stream).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocationInfoDTO getLocation(HttpServletRequest request) {
        String ip = getIpAddress(request);
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            Country country = response.getCountry();
            return new LocationInfoDTO(country.getName(), ip, country.getIsoCode() );
        } catch (IOException | GeoIp2Exception e) {
            logger.error("Error occur when query ip from GeoIp.");
        }
        return new LocationInfoDTO(ip);
    }

}
