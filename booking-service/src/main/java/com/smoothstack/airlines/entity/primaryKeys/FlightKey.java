package com.smoothstack.airlines.entity.primaryKeys;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class FlightKey implements Serializable {

	private static final long serialVersionUID = -4974417780265156080L;

	private Integer flightId;

	private Timestamp departTime;

	private Integer departCityId;

	private Integer arriveCityId;
}
