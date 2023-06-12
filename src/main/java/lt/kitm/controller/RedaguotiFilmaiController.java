package lt.kitm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lt.kitm.dto.FilmaiDTO;
import lt.kitm.model.Kategorija;
import lt.kitm.model.Filmai;
import lt.kitm.model.FilmaiDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RedaguotiFilmaiController {
    @FXML
    private TextField reitingas;
    @FXML
    private TextField pavadinimas;
    @FXML
    private TextArea aprasas;
    @FXML
    private ComboBox<String> kategorija;
    @FXML
    private Label zinute;
    private FilmaiDTO filmai;
    private ArrayList<Kategorija> kategorijos;
    private boolean atnaujinta = false;

    public void uzkrautiDuomenis(FilmaiDTO filmai, ArrayList<Kategorija> kategorijos) {
        this.filmai = filmai;
        // Sudeda kategorijas į ComboBox
        this.kategorijos = kategorijos;
        kategorija.getItems().addAll(
                kategorijos.stream()
                        .map(Kategorija::getPavadinimas)
                        .collect(Collectors.toList()
                ));
        this.reitingas.setText(String.valueOf(filmai.getReitingas()));
        this.pavadinimas.setText(filmai.getPavadinimas());
        this.aprasas.setText(filmai.getAprasas());
        this.kategorija.setValue(filmai.getKategorijosPavadinimas());
    }

    public boolean isAtnaujinta() {
        return this.atnaujinta;
    }

    public void onActionAtsaukti(ActionEvent actionEvent) {
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void onActionAtnaujinti(ActionEvent actionEvent) {
        //TODO: reikia patikrinti ar laukeliai tušti
        try {
            Filmai knyga = new Filmai(this.filmai.getId(), Double.parseDouble(this.reitingas.getText()), this.pavadinimas.getText(), this.aprasas.getText());
            if (!this.kategorija.getSelectionModel().isEmpty()) {
                int pasirinktaKategorija = kategorijos.stream()
                        .filter(k -> k.getPavadinimas().equals(this.kategorija.getValue()))
                        .findFirst().get().getId();
                knyga.setKategorijosId(pasirinktaKategorija);
            }
            FilmaiDAO.update(knyga);
            this.atnaujinta = true;
            this.onActionAtsaukti(actionEvent);
        } catch (SQLException e) {
            //TODO: žinutė jei klaida
            e.printStackTrace();
        }
    }
}
