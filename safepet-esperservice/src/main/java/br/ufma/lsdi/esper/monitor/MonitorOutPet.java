package br.ufma.lsdi.esper.monitor;

import br.ufma.lsdi.util.Coordinate;

public class MonitorOutPet implements Monitor {

    final private Integer idPet;
    final private Double radiusInMeters;
    final private Coordinate coordinate;

    public MonitorOutPet(Integer idPet, Double radiusInMeters, Coordinate coordinate) {
        this.idPet = idPet;
        this.radiusInMeters = radiusInMeters;
        this.coordinate = coordinate;
    }

    public String getRuleEpl() {
        return String.format("expression distance alias for {(2 * 6371 * " +
                        "Math.asin(Math.sqrt(Math.pow(Math.sin(( " +
                        "Math.toRadians(%s) -  " +
                        "Math.toRadians(LocationUpdate.latitude)) / 2), 2) + " +
                        "Math.cos(Math.toRadians(LocationUpdate.latitude)) * " +
                        "Math.cos(Math.toRadians(%s)) * " +
                        "Math.pow(Math.sin((Math.toRadians(%s) - " +
                        "Math.toRadians(LocationUpdate.longitude)) / 2), 2)))) * 1000}" +
                        "select *, distance from LocationUpdate(LocationUpdate.idPet = %d) having distance > %s",
                coordinate.getLatitude(), coordinate.getLatitude(), coordinate.getLongitude(),
                idPet, radiusInMeters);
    }

    /**
     Executar quando um evento complexo é gerado
     */
    public void update() {

    }

    public String getRuleName() {
        return "epl-location-out";
    }

    public String getRuleId() {
        return String.format("epl-location-out-pet-%d", idPet);
    }
}
