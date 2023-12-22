package pl.edu.wat.wcy.isi.app.model.entityModels;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "data_series_file")
@EqualsAndHashCode
public class DataSeriesFileEntity {
    private BigInteger dataSeriesFileId;
    private String name;
    private String hashName;
    private Timestamp dateSent;
    private Byte deleted = (byte) 0;
    private UserEntity user;
    private Integer size;
    private Double variance;
    private Double standardDeviation;
    private Collection<ApproximationPropertiesEntity> approximationProperties = new HashSet<>();
    private List<PointXY> points;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_series_file_id")
    public BigInteger getDataSeriesFileId() {
        return dataSeriesFileId;
    }

    public void setDataSeriesFileId(BigInteger dataSeriesFileId) {
        this.dataSeriesFileId = dataSeriesFileId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "hash_name")
    public String getHashName() {
        return hashName;
    }

    public void setHashName(String hashName) {
        this.hashName = hashName;
    }

    @Basic
    @Column(name = "date_sent")
    public Timestamp getDateSent() {
        return dateSent;
    }

    public void setDateSent(Timestamp dateSent) {
        this.dateSent = dateSent;
    }

    @Basic
    @Column(name = "is_deleted")
    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    @Basic
    @Column(name = "size")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Basic
    @Column(name = "variance")
    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    @Basic
    @Column(name = "standard_deviation")
    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userByUserId) {
        this.user = userByUserId;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dataSeriesFile")
    public Collection<ApproximationPropertiesEntity> getApproximationProperties() {
        return approximationProperties;
    }

    public void setApproximationProperties(Collection<ApproximationPropertiesEntity> approximationPropertiesByDataSeriesFileId) {
        this.approximationProperties = approximationPropertiesByDataSeriesFileId;
    }

    @Transient
    public synchronized List<PointXY> getPoints() {
        return points;
    }

    public synchronized void setPoints(List<PointXY> points) {
        this.points = points;
    }
}
