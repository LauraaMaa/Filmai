package lt.kitm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lt.kitm.model.Kategorija;
import lt.kitm.model.Filmai;
import lt.kitm.model.FilmaiDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PridetiFilmaController {
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
    private ArrayList<Kategorija> kategorijos;
    private boolean buvoPrideta = false;

    public boolean buvoPrideta() {
        return this.buvoPrideta;
    }

    public void uzkrautiKategorijas(ArrayList<Kategorija> kategorijos) {
        this.kategorijos = kategorijos;
        kategorija.getItems().addAll(
                kategorijos.stream()
                .map(Kategorija::getPavadinimas)
                .collect(Collectors.toList()
        ));
    }

    public void onActionAtsaukti(ActionEvent actionEvent) {
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void onActionPrideti(ActionEvent actionEvent) {
        //TODO: reikia patikrinti ar laukeliai tušti
        try {
            Filmai filmai = new Filmai(Double.parseDouble(this.reitingas.getText()), this.pavadinimas.getText(), this.aprasas.getText());
            if (!this.kategorija.getSelectionModel().isEmpty()) {
                int pasirinktaKategorija = kategorijos.stream()
                        .filter(k -> k.getPavadinimas().equals(this.kategorija.getValue()))
                        .findFirst().get().getId();
                filmai.setKategorijosId(pasirinktaKategorija);
            }
            FilmaiDAO.create(filmai);
            this.buvoPrideta = true;
            this.onActionAtsaukti(actionEvent);
        } catch (SQLException e) {
            //TODO: žinutė jei klaida
            e.printStackTrace();
        }
    }
}
