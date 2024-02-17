package es.unican.cibelapps.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Objects;

import es.unican.cibelapps.R;

@SuppressLint("ParcelCreator")
@Entity
public class Vulnerabilidad implements Parcelable {

    @Transient
    public static final String SEVERITY_L = "LOW";
    @Transient
    public static final String SEVERITY_M = "MEDIUM";
    @Transient
    public static final String SEVERITY_H = "HIGH";
    @Transient
    public static final String SEVERITY_C = "CRITICAL";
    @Transient
    public static final String IMPACT_N = "NONE";
    @Transient
    public static final String IMPACT_L = "LOW";
    @Transient
    public static final String IMPACT_P = "PARTIAL";
    @Transient
    public static final String IMPACT_H = "HIGH";
    @Transient
    public static final String IMPACT_C = "COMPLETE";

    @Id
    private String idCVE;
    private String descripcion;
    private String descripcion_en;
    private String confidentialityImpact;
    private String integrityImpact;
    private String availabilityImpact;
    private double baseScore;
    private String baseSeverity;
    private String versionEndExcluding;
    private String versionEndIncluding;
    private boolean afectaApp = true;

    @Generated(hash = 2101606825)
    public Vulnerabilidad(String idCVE, String descripcion, String descripcion_en, String confidentialityImpact,
            String integrityImpact, String availabilityImpact, double baseScore, String baseSeverity,
            String versionEndExcluding, String versionEndIncluding, boolean afectaApp) {
        this.idCVE = idCVE;
        this.descripcion = descripcion;
        this.descripcion_en = descripcion_en;
        this.confidentialityImpact = confidentialityImpact;
        this.integrityImpact = integrityImpact;
        this.availabilityImpact = availabilityImpact;
        this.baseScore = baseScore;
        this.baseSeverity = baseSeverity;
        this.versionEndExcluding = versionEndExcluding;
        this.versionEndIncluding = versionEndIncluding;
        this.afectaApp = afectaApp;
    }
    @Generated(hash = 1462448819)
    public Vulnerabilidad() {
    }

    public String getDescripcion() {
        return this.descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getConfidentialityImpact() {
        return this.confidentialityImpact;
    }
    public void setConfidentialityImpact(String confidentialityImpact) {
        this.confidentialityImpact = confidentialityImpact;
    }
    public String getIntegrityImpact() {
        return this.integrityImpact;
    }
    public void setIntegrityImpact(String integrityImpact) {
        this.integrityImpact = integrityImpact;
    }
    public String getAvailabilityImpact() {
        return this.availabilityImpact;
    }
    public void setAvailabilityImpact(String availabilityImpact) {
        this.availabilityImpact = availabilityImpact;
    }
    public double getBaseScore() {
        return this.baseScore;
    }
    public void setBaseScore(double baseScore) {
        this.baseScore = baseScore;
    }
    public String getBaseSeverity() {
        return this.baseSeverity;
    }
    public void setBaseSeverity(String baseSeverity) {
        this.baseSeverity = baseSeverity;
    }
    public String getIdCVE() {
        return this.idCVE;
    }
    public void setIdCVE(String idCVE) {
        this.idCVE = idCVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vulnerabilidad that = (Vulnerabilidad) o;
        return Objects.equals(idCVE, that.idCVE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCVE);
    }

    @Override
    public String toString() {
        return "Vulnerabilidad{" +
                "idCVE='" + idCVE + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", confidentialityImpact='" + confidentialityImpact + '\'' +
                ", integrityImpact='" + integrityImpact + '\'' +
                ", availabilityImpact='" + availabilityImpact + '\'' +
                ", baseScore=" + baseScore +
                ", baseSeverity='" + baseSeverity + '\'' +
                ", versionEndExcluding='" + versionEndExcluding + '\'' +
                ", versionEndIncluding='" + versionEndIncluding + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idCVE);
        parcel.writeString(descripcion);
        parcel.writeString(confidentialityImpact);
        parcel.writeString(integrityImpact);
        parcel.writeString(availabilityImpact);
        parcel.writeDouble(baseScore);
        parcel.writeString(baseSeverity);
    }

    public int getColorFromSeverity() {
        String baseSeverity = getBaseSeverity();
        int colorResId;
        switch (baseSeverity) {
            case Vulnerabilidad.SEVERITY_L:
                colorResId = R.color.lowV;
                break;
            case Vulnerabilidad.SEVERITY_M:
                colorResId = R.color.mediumV;
                break;
            case Vulnerabilidad.SEVERITY_H:
                colorResId = R.color.highV;
                break;
            case Vulnerabilidad.SEVERITY_C:
                colorResId = R.color.criticalV;
                break;
            default:
                colorResId = R.color.black; // Si el índice no está en el rango, se usa el color por defecto
                break;
        }
        return colorResId;
    }

    public int getColorFromImpact(String impact) {
        int colorResId;
        switch (impact) {
            case Vulnerabilidad.IMPACT_N:
                colorResId = R.color.noneI;
                break;
            case Vulnerabilidad.IMPACT_L:
                colorResId = R.color.lowV;
                break;
            case Vulnerabilidad.IMPACT_P:
                colorResId = R.color.mediumV;
                break;
            case Vulnerabilidad.IMPACT_H:
                colorResId = R.color.highV;
                break;
            case Vulnerabilidad.IMPACT_C:
                colorResId = R.color.criticalV;
                break;
            default:
                colorResId = R.color.black; // Si el índice no está en el rango, se usa el color por defecto
                break;
        }
        return colorResId;
    }

    public int mapImpact(String impact) {
        int result;
        switch (impact) {
            case Vulnerabilidad.IMPACT_N:
                result = 0;
                break;
            case Vulnerabilidad.IMPACT_L:
                result = 1;
                break;
            case Vulnerabilidad.IMPACT_P:
                result = 2;
                break;
            case Vulnerabilidad.IMPACT_H:
                result = 3;
                break;
            case Vulnerabilidad.IMPACT_C:
                result = 4;
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
    public String getVersionEndExcluding() {
        return this.versionEndExcluding;
    }
    public void setVersionEndExcluding(String versionEndExcluding) {
        this.versionEndExcluding = versionEndExcluding;
    }
    public String getVersionEndIncluding() {
        return this.versionEndIncluding;
    }
    public void setVersionEndIncluding(String versionEndIncluding) {
        this.versionEndIncluding = versionEndIncluding;
    }
    public boolean getAfectaApp() {
        return this.afectaApp;
    }
    public void setAfectaApp(boolean afectaApp) {
        this.afectaApp = afectaApp;
    }
    public String getDescripcion_en() {
        return this.descripcion_en;
    }
    public void setDescripcion_en(String descripcion_en) {
        this.descripcion_en = descripcion_en;
    }
}
