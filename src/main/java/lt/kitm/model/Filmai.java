package lt.kitm.model;

public class Filmai {
    private int id;
    private double reitingas;
    private String pavadinimas;
    private String aprasas;
    private int kategorijosId;

    public Filmai() {
    }

    public Filmai(double reitingas, String pavadinimas, String aprasas, int kategorijosId) {
        this.reitingas = reitingas;
        this.pavadinimas = pavadinimas;
        this.aprasas = aprasas;
        this.kategorijosId = kategorijosId;
    }

    public Filmai(int id, double reitingas, String pavadinimas, String aprasas) {
        this.id = id;
        this.reitingas = reitingas;
        this.pavadinimas = pavadinimas;
        this.aprasas = aprasas;
    }

    public Filmai(double reitingas, String pavadinimas, String aprasas) {
        this.reitingas = reitingas;
        this.pavadinimas = pavadinimas;
        this.aprasas = aprasas;

    }

    public Filmai(int id, double reitingas, String pavadinimas, String aprasas, int kategorijosId) {
        this.id = id;
        this.reitingas = reitingas;
        this.pavadinimas = pavadinimas;
        this.aprasas = aprasas;
        this.kategorijosId = kategorijosId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getReitingas() {
        return reitingas;
    }

    public void setReitingas(double reitingas) {
        this.reitingas = reitingas;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getAprasas() {
        return aprasas;
    }

    public void setAprasas(String aprasas) {
        this.aprasas = aprasas;
    }

    public int getKategorijosId() {
        return kategorijosId;
    }

    public void setKategorijosId(int kategorijosId) {
        this.kategorijosId = kategorijosId;
    }
}
