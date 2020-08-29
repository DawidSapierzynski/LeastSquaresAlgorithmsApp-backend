package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class DataSeriesFileDTO {
    private long id;
    private long userId;
    private String name;
    private String hashName;
    private Timestamp dateSent;
    private int size;
    private double errorPolynomial;
    private double errorTrigonometric;
    private boolean periodicity;
    private double variance;
    private double standardDeviation;
    private boolean deleted;
    private List<PointXY> points;
    private List<PointXY> artefacts;
}
