package es.unican.cibel.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@SuppressLint("ParcelCreator")
@Entity
public class Tipo implements Parcelable {

    @SerializedName("id")
    @NonNull
    @Id
    private Long idTipo;

    private String nombre;

    @Generated(hash = 1687190094)
    public Tipo(@NonNull Long idTipo, String nombre) {
        this.idTipo = idTipo;
        this.nombre = nombre;
    }

    @Generated(hash = 1352377350)
    public Tipo() {
    }

    public Long getIdTipo() {
        return this.idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Tipo{" +
                "idTipo=" + idTipo +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nombre);
    }
}
