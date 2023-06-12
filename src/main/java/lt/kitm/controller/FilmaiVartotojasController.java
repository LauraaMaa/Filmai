package lt.kitm.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lt.kitm.dto.FilmaiDTO;
import lt.kitm.model.FilmaiDAO;
import lt.kitm.utils.PrisijungesVartotojas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilmaiVartotojasController {
    @FXML
    private RadioButton mygtukas_filmai;
    @FXML
    private RadioButton mygtukas_rezervacijos;
    @FXML
    private RadioButton mygtukas_pamegti;
    @FXML
    private VBox turinys;
    @FXML
    private Label zinute;

    public void initialize() {
        this.mygtukas_filmai.getStyleClass().remove("radio-button");
        this.mygtukas_filmai.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FilmaiVartotojasController.this.uzkrautiFilma();
            }
        });
        this.mygtukas_rezervacijos.getStyleClass().remove("radio-button");
        this.mygtukas_rezervacijos.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FilmaiVartotojasController.this.uzkrautiRezervacijas();
            }
        });
        this.mygtukas_pamegti.getStyleClass().remove("radio-button");
        this.mygtukas_pamegti.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FilmaiVartotojasController.this.uzkrautiPamegtusFilmus();
            }
        });
        this.uzkrautiFilma();
    }

    public void onActionAtsijungti(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/prisijungimo_langas.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Filmai");
            stage.setScene(new Scene(root, 600, 400));
            PrisijungesVartotojas.pasalintiVartotoja();
        } catch (IOException e) {
            this.zinute.setText("Klaida grįžtant į prisijungimo langą");
        }
    }

    private void rezervuotiFilma(int vartotojoId, int filmaiId) {
        try {
            if (FilmaiDAO.filmasRezervuotas(filmaiId)) {
                this.zinute.setText("Filmas yra rezervuota kito vartotojo");
            } else {
                FilmaiDAO.rezervuotiFilma(vartotojoId, filmaiId);
                this.zinute.setText("Filmas rezervuota");
            }
        } catch (SQLException e) {
            //TODO: reikia žinutės jei nepavyko
            e.printStackTrace();
        }
    }

    private void nebemegtiFilmai(int vartotojoId, int filmaiId) {
        try {
            FilmaiDAO.nebemegtiFilmo(vartotojoId, filmaiId);
            this.zinute.setText("Filmas pašalita iš pamėgti filmu sąrašo");
            this.uzkrautiPamegtusFilmus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void atsauktiRezervacija(int vartotojoId, int filmaiId) {
        try {
            FilmaiDAO.atsauktiRezervacija(vartotojoId, filmaiId);
            this.zinute.setText("Filmo rezervacija atšaukta");
            this.uzkrautiRezervacijas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void pamegtiFilma(int vartotojoId, int filmaiId) {
        try {
            if (FilmaiDAO.arVartotojasPamegesFilma(vartotojoId, filmaiId)) {
                this.zinute.setText("Filma jau turite pamėgtų sąraše");
            } else {
                FilmaiDAO.pamegtiFilmai(vartotojoId, filmaiId);
                this.zinute.setText("Filmas pamėgtas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void uzkrautiFilma() {
        try {
            this.turinys.getChildren().clear();
            TextField filtravimas = new TextField();
            filtravimas.setPromptText("Filmo pavadinimas");
            this.turinys.getChildren().add(filtravimas);
            //TODO: reikia padaryti, kad gražiau išsiplėstu lentelės
            ArrayList<FilmaiDTO> knygos = FilmaiDAO.visiFilmai();
            TableView<FilmaiDTO> tableView = new TableView<>();
            TableColumn<FilmaiDTO, String> pavadinimas = new TableColumn<>("Pavadinimas");
            pavadinimas.setCellValueFactory(new PropertyValueFactory<>("pavadinimas"));
            TableColumn<FilmaiDTO, String> aprasas = new TableColumn<>("Aprasas");
            aprasas.setCellValueFactory(new PropertyValueFactory<>("aprasas"));
            aprasas.setMaxWidth(300);
            TableColumn<FilmaiDTO, String> kategorija = new TableColumn<>("Kategorija");
            kategorija.setCellValueFactory(new PropertyValueFactory<>("kategorijosPavadinimas"));
            TableColumn<FilmaiDTO, Integer> IMDB = new TableColumn<>("IMDB");
            IMDB.setCellValueFactory(new PropertyValueFactory<>("IMDB"));
            TableColumn<FilmaiDTO, Void> rezervuoti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryRezervuoti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Komentuoti");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                rezervuotiFilma(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            rezervuoti.setCellFactory(cellFactoryRezervuoti);
            TableColumn<FilmaiDTO, Void> pamegti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryPamegti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Filmo informacija");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                pamegtiFilma(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            pamegti.setCellFactory(cellFactoryPamegti);
            tableView.getColumns().addAll(pavadinimas, aprasas, kategorija, IMDB, rezervuoti, pamegti);
            this.turinys.getChildren().add(tableView);
            tableView.getItems().setAll(knygos);
            filtravimas.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    ArrayList<FilmaiDTO> prafiltruotiKlausimai = (ArrayList<FilmaiDTO>) knygos.
                            stream().
                            filter(p -> p.getPavadinimas().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    tableView.getItems().setAll(prafiltruotiKlausimai);
                }
            });
            tableView.prefHeightProperty().bind(this.turinys.heightProperty());
        } catch (SQLException e) {
            //TODO: žinutė jei nepavyko?
        }
    }

    private void uzkrautiRezervacijas() {
        try {
            this.turinys.getChildren().clear();
            TextField filtravimas = new TextField();
            filtravimas.setPromptText("Filmo pavadinimas");
            this.turinys.getChildren().add(filtravimas);
            //TODO: reikia padaryti, kad gražiau išsiplėstu lentelės
            ArrayList<FilmaiDTO> filmai = FilmaiDAO.gautiRezervuotusFilmus(PrisijungesVartotojas.getVartotojas().getId());
            TableView<FilmaiDTO> tableView = new TableView<>();
            TableColumn<FilmaiDTO, String> pavadinimas = new TableColumn<>("Pavadinimas");
            pavadinimas.setCellValueFactory(new PropertyValueFactory<>("pavadinimas"));
            TableColumn<FilmaiDTO, String> aprasas = new TableColumn<>("Aprasas");
            aprasas.setCellValueFactory(new PropertyValueFactory<>("aprasas"));
            aprasas.setMaxWidth(300);
            TableColumn<FilmaiDTO, String> kategorija = new TableColumn<>("Kategorija");
            kategorija.setCellValueFactory(new PropertyValueFactory<>("kategorijosPavadinimas"));
            TableColumn<FilmaiDTO, Integer> IMDB = new TableColumn<>("IMDB");
            IMDB.setCellValueFactory(new PropertyValueFactory<>("IMDB"));
            TableColumn<FilmaiDTO, Void> rezervuoti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryRezervuoti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Naujas komentaras");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                atsauktiRezervacija(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            rezervuoti.setCellFactory(cellFactoryRezervuoti);
            TableColumn<FilmaiDTO, Void> pamegti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryPamegti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Trinti");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                pamegtiFilma(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            pamegti.setCellFactory(cellFactoryPamegti);
            tableView.getColumns().addAll(pavadinimas, aprasas, kategorija, IMDB, rezervuoti, pamegti);
            this.turinys.getChildren().add(tableView);
            tableView.getItems().setAll(filmai);
            filtravimas.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    ArrayList<FilmaiDTO> prafiltruotiKlausimai = (ArrayList<FilmaiDTO>) filmai.
                            stream().
                            filter(p -> p.getPavadinimas().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    tableView.getItems().setAll(prafiltruotiKlausimai);
                }
            });
            tableView.prefHeightProperty().bind(this.turinys.heightProperty());
        } catch (SQLException e) {
            //TODO: žinutė jei nepavyko?
        }
    }

    private void uzkrautiPamegtusFilmus() {
        this.turinys.getChildren().clear();
        try {
            this.turinys.getChildren().clear();
            TextField filtravimas = new TextField();
            filtravimas.setPromptText("Filmo pavadinimas");
            this.turinys.getChildren().add(filtravimas);
            //TODO: reikia padaryti, kad gražiau išsiplėstu lentelės
            ArrayList<FilmaiDTO> filmai = FilmaiDAO.gautiPamegtusFilmus(PrisijungesVartotojas.getVartotojas().getId());
            TableView<FilmaiDTO> tableView = new TableView<>();
            TableColumn<FilmaiDTO, String> pavadinimas = new TableColumn<>("Pavadinimas");
            pavadinimas.setCellValueFactory(new PropertyValueFactory<>("pavadinimas"));
            TableColumn<FilmaiDTO, String> aprasas = new TableColumn<>("Aprasas");
            aprasas.setCellValueFactory(new PropertyValueFactory<>("aprasas"));
            aprasas.setMaxWidth(300);
            TableColumn<FilmaiDTO, String> kategorija = new TableColumn<>("Kategorija");
            kategorija.setCellValueFactory(new PropertyValueFactory<>("kategorijosPavadinimas"));
            TableColumn<FilmaiDTO, Integer> IMDB = new TableColumn<>("IMDB");
            IMDB.setCellValueFactory(new PropertyValueFactory<>("IMDB"));
            TableColumn<FilmaiDTO, Void> rezervuoti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryRezervuoti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Rezervuoti");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                rezervuotiFilma(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            rezervuoti.setCellFactory(cellFactoryRezervuoti);
            TableColumn<FilmaiDTO, Void> pamegti = new TableColumn<>();
            Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>> cellFactoryPamegti = new Callback<TableColumn<FilmaiDTO, Void>, TableCell<FilmaiDTO, Void>>() {
                @Override
                public TableCell<FilmaiDTO, Void> call(TableColumn<FilmaiDTO, Void> param) {
                    final TableCell<FilmaiDTO, Void> mygtukasRezervuotiLangelis = new TableCell<FilmaiDTO, Void>() {
                        final Button mygtukas = new Button("Nebemėgstu");
                        {
                            mygtukas.setOnAction((ActionEvent event) -> {
                                FilmaiDTO knyga = getTableView().getItems().get(getIndex());
                                nebemegtiFilmai(PrisijungesVartotojas.getVartotojas().getId(), knyga.getId());
                            });
                        }
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(mygtukas);
                            }
                        }
                    };
                    return mygtukasRezervuotiLangelis;
                }
            };
            pamegti.setCellFactory(cellFactoryPamegti);
            tableView.getColumns().addAll(pavadinimas, aprasas, kategorija, IMDB, rezervuoti, pamegti);
            this.turinys.getChildren().add(tableView);
            tableView.getItems().setAll(filmai);
            filtravimas.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    ArrayList<FilmaiDTO> prafiltruotiKlausimai = (ArrayList<FilmaiDTO>) filmai.
                            stream().
                            filter(p -> p.getPavadinimas().toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList());
                    tableView.getItems().setAll(prafiltruotiKlausimai);
                }
            });
            tableView.prefHeightProperty().bind(this.turinys.heightProperty());
        } catch (SQLException e) {
            //TODO: žinutė jei nepavyko?
        }
    }
}
