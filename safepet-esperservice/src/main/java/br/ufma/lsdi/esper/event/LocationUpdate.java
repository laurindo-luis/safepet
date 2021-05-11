package br.ufma.lsdi.esper.event;

public class LocationUpdate {
    private Integer idPet;
    private Double latitude;
    private Double longitude;

    public LocationUpdate(Integer idPet, Double latitude, Double longitude) {
        this.idPet = idPet;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static String getNameEvent() {
        return "LocationUpdate";
    }

    public Integer getIdPet() {
        return idPet;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
