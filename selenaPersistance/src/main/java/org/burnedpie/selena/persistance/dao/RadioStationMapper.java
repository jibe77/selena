package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jibe on 15/08/16.
 */
public class RadioStationMapper implements org.springframework.jdbc.core.RowMapper<RadioStation> {


    public RadioStation mapRow(ResultSet resultSet, int i) throws SQLException {
        RadioStation radioStation = new RadioStation();
        radioStation.setId(resultSet.getInt("ID"));
        radioStation.setName(resultSet.getString("NAME"));
        radioStation.setUrl(resultSet.getString("URL"));
        radioStation.setChannel(resultSet.getInt("CHANNEL"));
        return radioStation;
    }
}
