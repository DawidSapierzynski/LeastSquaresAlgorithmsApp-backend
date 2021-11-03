package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class DataSeriesFileDTO {
    private BigInteger id;
    private BigInteger userId;
    private String name;
    private String hashName;
    private Timestamp dateSent;
    private int size;
    private double variance;
    private double standardDeviation;
    private boolean deleted;
    private List<PointXY> points;
}
