package pl.least_squares_algorithms.data_series_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.least_squares_algorithms.core.PointXY;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class DataSeriesFileDto {
    private Long id;
    private Long userId;
    private String name;
    private String hashName;
    private Timestamp dateSent;
    private int size;
    private double variance;
    private double standardDeviation;
    private boolean deleted;
    private List<PointXY> points;
}
