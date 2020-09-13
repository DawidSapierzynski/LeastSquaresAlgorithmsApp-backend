package pl.edu.wat.wcy.isi.app.model.entityModels;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "approximation_properties", schema = "aaa")
public class ApproximationPropertiesEntity {
    private Long approximationPropertiesId;
    private Integer degreeApproximation;
    private Timestamp dateCreate;
    private Byte deleted = (byte) 0;
    private DataSeriesFileEntity dataSeriesFile;
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approximation_properties_id")
    public Long getApproximationPropertiesId() {
        return approximationPropertiesId;
    }

    public void setApproximationPropertiesId(Long approximationPropertiesId) {
        this.approximationPropertiesId = approximationPropertiesId;
    }


    @Basic
    @Column(name = "degree_approximation")
    public Integer getDegreeApproximation() {
        return degreeApproximation;
    }

    public void setDegreeApproximation(Integer precisionApproximation) {
        this.degreeApproximation = precisionApproximation;
    }

    @Basic
    @Column(name = "date_create")
    public Timestamp getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Timestamp dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Basic
    @Column(name = "is_deleted")
    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApproximationPropertiesEntity that = (ApproximationPropertiesEntity) o;
        return Objects.equals(approximationPropertiesId, that.approximationPropertiesId) &&
                Objects.equals(degreeApproximation, that.degreeApproximation) &&
                Objects.equals(dateCreate, that.dateCreate) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(dataSeriesFile, that.dataSeriesFile) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approximationPropertiesId, degreeApproximation, dateCreate, deleted, dataSeriesFile, user);
    }

    @ManyToOne
    @JoinColumn(name = "data_series_file_id", referencedColumnName = "data_series_file_id")
    public DataSeriesFileEntity getDataSeriesFile() {
        return dataSeriesFile;
    }

    public void setDataSeriesFile(DataSeriesFileEntity dataSeriesFileByDataSeriesFileId) {
        this.dataSeriesFile = dataSeriesFileByDataSeriesFileId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userByUserId) {
        this.user = userByUserId;
    }

}
