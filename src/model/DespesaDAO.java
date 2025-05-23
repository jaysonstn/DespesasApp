package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {
    private Connection connection;

    public DespesaDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:data/despesas.db");
            criarTabelaSeNaoExistir();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void criarTabelaSeNaoExistir() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS despesas (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "descricao TEXT NOT NULL," +
                     "valor REAL NOT NULL," +
                     "data TEXT NOT NULL" +
                     ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void adicionarDespesa(Despesa despesa) throws SQLException {
        String sql = "INSERT INTO despesas (descricao, valor, data) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, despesa.getDescricao());
            pstmt.setDouble(2, despesa.getValor());
            pstmt.setString(3, despesa.getData().toString());
            pstmt.executeUpdate();
        }
    }

    public List<Despesa> listarDespesas() throws SQLException {
        List<Despesa> lista = new ArrayList<>();
        String sql = "SELECT * FROM despesas ORDER BY id DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Despesa(
                    rs.getInt("id"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    LocalDate.parse(rs.getString("data"))
                ));
            }
        }
        return lista;
    }

    public List<Despesa> listarDespesasPorData(LocalDate dataFiltro) throws SQLException {
        List<Despesa> lista = new ArrayList<>();
        String sql = "SELECT * FROM despesas WHERE data = ? ORDER BY id DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dataFiltro.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Despesa(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getDouble("valor"),
                        LocalDate.parse(rs.getString("data"))
                    ));
                }
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM despesas WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
