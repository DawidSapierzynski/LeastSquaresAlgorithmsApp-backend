package pl.leastsquaresalgorithms.approximationpropertiesservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ApproximationPropertiesDTO {
    private BigInteger id;
    private BigInteger userId;
    private BigInteger dataSeriesFileId;
    private int degree;
    private Timestamp dateCreate;
    private boolean deleted;
}
