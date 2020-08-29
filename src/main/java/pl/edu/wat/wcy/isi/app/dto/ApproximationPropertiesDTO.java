package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ApproximationPropertiesDTO {
    private long id;
    private long userId;
    private long dataSeriesFileId;
    private int degree;
    private DataSeriesFileDTO dataSeriesFileDTO;
    private Timestamp dateCreate;
    private boolean deleted;
}
