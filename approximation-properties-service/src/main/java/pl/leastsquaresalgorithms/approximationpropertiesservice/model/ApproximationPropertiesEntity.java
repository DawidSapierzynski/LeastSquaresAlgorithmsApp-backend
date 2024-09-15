package pl.leastsquaresalgorithms.approximationpropertiesservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "approximation_properties")
@EqualsAndHashCode
public class ApproximationPropertiesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approximation_properties_id")
    private BigInteger approximationPropertiesId;

    @Basic
    @Column(name = "degree_approximation")
    private Integer degreeApproximation;

    @Basic
    @Column(name = "date_create")
    private Timestamp dateCreate;

    @Basic
    @Column(name = "is_deleted")
    private Byte deleted = (byte) 0;

    @Basic
    @Column(name = "data_series_file_id")
    private BigInteger dataSeriesFileId;

    @Basic
    @Column(name = "user_id")
    private BigInteger userId;
}
