package br.ufma.lsdi.safepetmobile.domain;

public class Pet {
    private Long id;
    private String name;
    private Double radiusInMeters;
    private Integer type;
    private Integer idColeira;
    private Long tutorId;
    private boolean monitored;

    public Pet(Long id, String name, Double radiusInMeters, Integer type, Integer idColeira,
               Long tutorId, boolean monitored) {
        this.id = id;
        this.name = name;
        this.radiusInMeters = radiusInMeters;
        this.type = type;
        this.tutorId = tutorId;
        this.idColeira = idColeira;
        this.monitored = monitored;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRadiusInMeters() {
        return radiusInMeters;
    }

    public void setRadiusInMeters(Double radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Integer getIdColeira() {
        return idColeira;
    }

    public void setIdColeira(Integer idColeira) {
        this.idColeira = idColeira;
    }

    public boolean isMonitored() {
        return monitored;
    }

    public void setMonitored(boolean monitored) {
        this.monitored = monitored;
    }
}
