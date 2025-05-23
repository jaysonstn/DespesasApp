package controller;

import model.Despesa;
import model.DespesaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DespesaController {
    private DespesaDAO dao;

    public DespesaController() {
        dao = new DespesaDAO();
    }

    public void adicionarDespesa(String descricao, double valor, LocalDate data) throws SQLException {
        Despesa despesa = new Despesa(descricao, valor, data);
        dao.adicionarDespesa(despesa);
    }

    public List<Despesa> listarDespesas() throws SQLException {
        return dao.listarDespesas();
    }

    public void excluirDespesa(int id) throws SQLException {
        dao.excluir(id);
    }
}
