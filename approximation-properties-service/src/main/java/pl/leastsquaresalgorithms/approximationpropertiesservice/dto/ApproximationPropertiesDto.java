package pl.leastsquaresalgorithms.approximationpropertiesservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ApproximationPropertiesDto {
    private BigInteger id;
    private BigInteger userId;
    private DataSeriesFileDto dataSeriesFile;
    private int degree;
    private Timestamp dateCreate;
    private boolean deleted;
}
