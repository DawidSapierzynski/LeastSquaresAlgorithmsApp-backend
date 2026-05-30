package pl.leastsquaresalgorithms.dataseries.model;

import jakarta.persistence.*;
import lombok.*;
import pl.least_squares_algorithms.core.PointXY;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "data_series_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DataSeriesFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_series_file_id")
    private Long dataSeriesFileId;
    @Column
    private String name;
    @Column(name = "hash_name")
    private String hashName;
    @Column(name = "date_sent")
    private Timestamp dateSent;
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "size")
    private Integer size;
    @Column(name = "variance")
    private Double variance;
    @Column(name = "standard_deviation")
    private Double standardDeviation;
    @Transient
    private List<PointXY> points;

    public synchronized List<PointXY> getPoints() {
        return points;
    }

    public synchronized void setPoints(List<PointXY> points) {
        this.points = points;
    }
}
