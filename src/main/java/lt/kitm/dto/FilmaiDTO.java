package lt.kitm.dto;

public class FilmaiDTO {
    private int id;
    private double reitingas;
    private String pavadinimas;
    private String aprasas;
    private int kategorijosId;
    private String kategorijosPavadinimas;

    public FilmaiDTO() {
    }

    public FilmaiDTO(int id, double reitingas, String pavadinimas, String aprasas, int kategorijosId, String kategorijosPavadinimas) {
        this.id = id;
        this.reitingas = reitingas;
        this.pavadinimas = pavadinimas;
        this.aprasas = aprasas;
        this.kategorijosId = kategorijosId;
        this.kategorijosPavadinimas = kategorijosPavadinimas;
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

    public String getKategorijosPavadinimas() {
        return kategorijosPavadinimas;
    }

    public void setKategorijosPavadinimas(String kategorijosPavadinimas) {
        this.kategorijosPavadinimas = kategorijosPavadinimas;
    }

    @Override
    public String toString() {
        return "FilmaiDTO{" +
                "id=" + id +
                ", isbn='" + reitingas + '\'' +
                ", pavadinimas='" + pavadinimas + '\'' +
                ", santrauka='" + aprasas + '\'' +
                ", kategorijosId=" + kategorijosId +
                ", kategorijosPavadinimas='" + kategorijosPavadinimas + '\'' +
                '}';
    }
}
