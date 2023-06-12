package lt.kitm.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lt.kitm.dto.FilmaiDTO;
import lt.kitm.model.Kategorija;
import lt.kitm.model.KategorijaDAO;
import lt.kitm.model.FilmaiDAO;
import lt.kitm.utils.PrisijungesVartotojas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class KinasAdminasController {
    @FXML
    private VBox pagrindinis_langas;
    @FXML
    private VBox kategoriju_sarasas;
    @FXML
    private VBox filmu_sarasas;
    @FXML
    private TextField naujos_kategorijos_pavadinimas;
    @FXML
    private Label zinute;
    private ArrayList<FilmaiDTO> filmai;
    private ArrayList<Kategorija> kategorijos;

    public void initialize() {
        this.filmu_sarasas.prefWidthProperty().bind(this.pagrindinis_langas.widthProperty());
        try {
            this.filmai = FilmaiDAO.visiFilmai();
            this.kategorijos = KategorijaDAO.visosKategorijos();
        } catch (SQLException e) {
            this.zinute.setText("Nepavyko užkrauti atsakymų");
        }
        this.uzkrautiFilmus();
        this.uzkrautiKategorijas();
    }

    private void atnaujintiKategorijas() {
        try {
            this.kategorijos = KategorijaDAO.visosKategorijos();
            this.uzkrautiKategorijas();
        } catch (SQLException e) {
            // Gal reikia pranešimo kad įvyko klaida?
            e.printStackTrace();
        }
    }

    private void atnaujintiFilmus() {
        try {
            this.filmai = FilmaiDAO.visiFilmai();
            this.uzkrautiFilmus();
        } catch (SQLException e) {
            // Gal reikia pranešimo kad įvyko klaida?
            e.printStackTrace();
        }
    }

    private void uzkrautiKategorijas() {
        this.kategoriju_sarasas.getChildren().clear();
        if (this.kategorijos.isEmpty()) {
            this.kategoriju_sarasas.getChildren().add(new Label("Kategorijų nėra"));
        } else {
            for (Kategorija kategorija: this.kategorijos) {
                HBox kategorijosHbox = new HBox();
                kategorijosHbox.setSpacing(10);
                kategorijosHbox.setAlignment(Pos.CENTER_LEFT);
                Label kategorija_pavadinimas = new Label(kategorija.getPavadinimas());
                Button kategorija_redaguoti = new Button("Redaguoti");
                kategorija_redaguoti.setOnAction(event -> this.redaguotiKategorija(kategorija));
                Button kategorija_trinti = new Button("Trinti");
                kategorija_trinti.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        trintiKategorija(kategorija.getId());
                    }
                });
                kategorijosHbox.getChildren().addAll(kategorija_pavadinimas, kategorija_redaguoti, kategorija_trinti);
                this.kategoriju_sarasas.getChildren().add(kategorijosHbox);
            }
        }
    }

    private void uzkrautiFilmus() {
        this.filmu_sarasas.getChildren().clear();
        if (this.filmai.isEmpty()) {
            this.filmu_sarasas.getChildren().add(new Label("Filmu nėra"));
        } else {
            HBox filmoHBoxAntraste = new HBox();
            filmoHBoxAntraste.setSpacing(20);
            filmoHBoxAntraste.setAlignment(Pos.CENTER_LEFT);
            Label idAntraste = new Label("Id");
            idAntraste.setMinWidth(30);
            idAntraste.setMaxWidth(30);
            Label IMDBAntraste = new Label("IMDB");
            IMDBAntraste.setMinWidth(100);
            IMDBAntraste.setMaxWidth(100);
            Label pavadinimasAntraste = new Label("Pavadinimas");
            pavadinimasAntraste.setMinWidth(150);
            pavadinimasAntraste.setMaxWidth(150);
            Label aprasasAntraste = new Label("Aprasas");
            aprasasAntraste.setMinWidth(200);
            aprasasAntraste.setMaxWidth(200);
            aprasasAntraste.setWrapText(true);
            Label kategorijaAntraste = new Label("Kategorija");
            kategorijaAntraste.setMinWidth(100);
            kategorijaAntraste.setMaxWidth(100);
            filmoHBoxAntraste.getChildren().addAll(idAntraste, IMDBAntraste, pavadinimasAntraste, aprasasAntraste, kategorijaAntraste);
            this.filmu_sarasas.getChildren().add(filmoHBoxAntraste);
            for (FilmaiDTO filmas: filmai) {
                HBox knygosHBox = new HBox();
                knygosHBox.setSpacing(20);
                knygosHBox.setAlignment(Pos.CENTER_LEFT);
                Label id = new Label(String.valueOf(filmas.getId()));
                id.setTextAlignment(TextAlignment.CENTER);
                id.setMinWidth(30);
                id.setMaxWidth(30);
                Label reitingas = new Label(String.valueOf(filmas.getReitingas()));
                reitingas.setMinWidth(100);
                reitingas.setMaxWidth(100);
                reitingas.setTextAlignment(TextAlignment.CENTER);
                Label pavadinimas = new Label(filmas.getPavadinimas());
                pavadinimas.setMinWidth(150);
                pavadinimas.setMaxWidth(150);
                pavadinimas.setTextAlignment(TextAlignment.CENTER);
                Label santrauka = new Label(filmas.getAprasas());
                santrauka.setMinWidth(200);
                santrauka.setMaxWidth(200);
                santrauka.setWrapText(true);
                santrauka.setTextAlignment(TextAlignment.CENTER);
                Label kategorija = new Label(filmas.getKategorijosPavadinimas());
                kategorija.setMinWidth(100);
                kategorija.setMaxWidth(100);
                kategorija.setTextAlignment(TextAlignment.CENTER);
                Button mygtukasRedaguoti = new Button("Redaguoti");
                mygtukasRedaguoti.setOnAction(event -> this.redaguotiFilma(filmas));
                Button mygtukasTrinti = new Button("Trinti");
                mygtukasTrinti.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        trintiFilma(filmas.getId());
                    }
                });
                knygosHBox.getChildren().addAll(id, reitingas, pavadinimas, santrauka, kategorija, mygtukasRedaguoti, mygtukasTrinti);
                this.filmu_sarasas.getChildren().add(knygosHBox);
            }
        }
    }

    private void trintiKategorija(int id) {
        //TODO: reikia žinučių tiek ištrynus tiek jei yra klaida?
        try {
            if (KategorijaDAO.kategorijaTuriFilmu(id)) {
                this.zinute.setText("Negalima ištrinti kategorijos, kuri turi knygų");
            } else {
                KategorijaDAO.delete(id);
                this.atnaujintiKategorijas();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void redaguotiKategorija(Kategorija kategorija) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/taisyti_kategorija.fxml"));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Kategorijos redagavimas");
            stage.setScene(new Scene(loader.load(), 400, 300));

            TaisytiKategorijaController controller = loader.getController();
            controller.uzkrautiKategorijosInformacija(kategorija);
            stage.showAndWait();
            if (controller.isPakeista()) {
                this.atnaujintiKategorijas();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionPridetiKategorija() {
        //TODO: reikia patikrinti ar laukelis nėra tuščas
        try {
            KategorijaDAO.create(this.naujos_kategorijos_pavadinimas.getText());
            this.naujos_kategorijos_pavadinimas.clear();
            this.atnaujintiKategorijas();
        } catch (SQLException e) {
            this.zinute.setText("Tokia kategorija jau egzistuoja");
        }
    }

    public void onActionPridetiKnyga(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/prideti_filma.fxml"));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Pridėti filma");
            stage.setScene(new Scene(loader.load(), 800, 600));

            PridetiFilmaController controller = loader.getController();
            controller.uzkrautiKategorijas(this.kategorijos);
            stage.showAndWait();
            if (controller.buvoPrideta()) {
                this.atnaujintiFilmus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trintiFilma(int id) {
        try {
            FilmaiDAO.delete(id);
            this.atnaujintiFilmus();
        } catch (SQLException e) {
            //TODO: reikia zinutes
            e.printStackTrace();
        }
    }

    private void redaguotiFilma(FilmaiDTO filmas) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/redaguoti_filma.fxml"));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Pridėti filma");
            stage.setScene(new Scene(loader.load(), 800, 600));

            RedaguotiFilmaiController controller = loader.getController();
            controller.uzkrautiDuomenis(filmas, this.kategorijos);
            stage.showAndWait();
            if (controller.isAtnaujinta()) {
                this.atnaujintiFilmus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionAtsijungti(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/prisijungimo_langas.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Bible");
            stage.setScene(new Scene(root, 600, 400));
            PrisijungesVartotojas.pasalintiVartotoja();
        } catch (IOException e) {
            this.zinute.setText("Klaida grįžtant į prisijungimo langą");
        }
    }

}
