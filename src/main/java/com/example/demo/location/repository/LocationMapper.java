package com.example.demo.location.repository;

import com.example.demo.location.domain.Location;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LocationMapper {

    @Select("SELECT * FROM location WHERE latitude = #{latitude} AND longitude = #{longitude}")
    Location findByLatitudeAndLongitude(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

    @Insert("INSERT INTO location (latitude, longitude) VALUES (#{latitude}, #{longitude})")
    @Options(useGeneratedKeys = true, keyProperty = "locationId")
    void saveLocation(Location location);
}
