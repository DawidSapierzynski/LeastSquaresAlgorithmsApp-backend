package pl.least_squares_algorithms.approximation_properties_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "approximation_properties")
@EqualsAndHashCode
public class ApproximationPropertiesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approximation_properties_id")
    private Long approximationPropertiesId;

    @Basic
    @Column(name = "degree_approximation")
    private Integer degreeApproximation;

    @Basic
    @Column(name = "date_create")
    private Timestamp dateCreate;

    @Basic
    @Column(name = "is_deleted")
    private Boolean deleted = Boolean.FALSE;

    @Basic
    @Column(name = "data_series_file_id")
    private Long dataSeriesFileId;

    @Basic
    @Column(name = "user_id")
    private Long userId;
}
