package view;

import controller.DespesaController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Despesa;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainView extends Application {
    private DespesaController controller;
    private ObservableList<Despesa> despesas;
    private ListView<Despesa> listaView;
    private Label totalLabel;

    @Override
    public void start(Stage stage) {
        controller = new DespesaController();
        despesas = FXCollections.observableArrayList();

        // Campos de entrada
        TextField txtDescricao = new TextField();
        txtDescricao.setPromptText("Descrição");
        txtDescricao.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor");
        txtValor.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-background-color: #40444b; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        datePicker.setPrefWidth(140);

        Button btnAdicionar = new Button("Adicionar");
        btnAdicionar.setStyle("-fx-background-color: #7289da; -fx-text-fill: white;");
        btnAdicionar.setOnAction(e -> {
            String desc = txtDescricao.getText();
            double valor;
            LocalDate data;
            try {
                valor = Double.parseDouble(txtValor.getText());
            } catch (NumberFormatException ex) {
                showAlert("Valor inválido.");
                return;
            }
            data = datePicker.getValue();
            if (desc.isEmpty() || data == null) {
                showAlert("Descrição e data são obrigatórias.");
                return;
            }
            try {
                controller.adicionarDespesa(desc, valor, data);
                txtDescricao.clear();
                txtValor.clear();
                datePicker.setValue(LocalDate.now());
                atualizarLista();
            } catch (SQLException ex) {
                showAlert("Erro ao adicionar despesa: " + ex.getMessage());
            }
        });

        // Lista de despesas
        listaView = new ListView<>(despesas);
        listaView.setStyle("-fx-control-inner-background: #2c2f33; -fx-text-fill: white;");
        VBox.setVgrow(listaView, Priority.ALWAYS); // Responsiva ao redimensionamento

        // Botão de exclusão
        Button btnExcluir = new Button("Excluir");
        btnExcluir.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnExcluir.setOnAction(e -> {
            Despesa selecionada = listaView.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                try {
                    controller.excluirDespesa(selecionada.getId());
                    atualizarLista();
                } catch (SQLException ex) {
                    showAlert("Erro ao excluir despesa: " + ex.getMessage());
                }
            } else {
                showAlert("Selecione uma despesa para excluir.");
            }
        });

        // Totalizador
        totalLabel = new Label("Total: R$ 0,00");
        totalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Layouts
        HBox entradaBox = new HBox(10, txtDescricao, txtValor, datePicker, btnAdicionar);
        entradaBox.setPadding(new Insets(10));
        entradaBox.setAlignment(Pos.CENTER);
        entradaBox.setStyle("-fx-background-color: #2c2f33;");

        HBox totalBox = new HBox(10, totalLabel);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(0, 10, 0, 0));

        HBox botoesBox = new HBox(10, btnExcluir, totalBox);
        botoesBox.setPadding(new Insets(10));
        botoesBox.setAlignment(Pos.CENTER_RIGHT);
        botoesBox.setStyle("-fx-background-color: #23272a;");

        VBox root = new VBox(10, entradaBox, listaView, botoesBox);
        VBox.setVgrow(listaView, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #2c2f33;");
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 650, 400);
        stage.setScene(scene);
        stage.setTitle("Controle de Despesas");
        stage.show();

        atualizarLista();
    }

    private void atualizarLista() {
        try {
            despesas.setAll(controller.listarDespesas());
            double total = despesas.stream().mapToDouble(Despesa::getValor).sum();
            totalLabel.setText(String.format("Total: R$ %.2f", total));
        } catch (SQLException ex) {
            showAlert("Erro ao carregar despesas: " + ex.getMessage());
        }
    }

    private void showAlert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
