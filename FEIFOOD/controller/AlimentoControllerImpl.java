package controller;

import dao.AlimentoDAO;
import model.Alimento;
import java.util.List;

public class AlimentoControllerImpl implements AlimentoController {
    private final AlimentoDAO alimentoDAO;

    public AlimentoControllerImpl(AlimentoDAO dao) {
        this.alimentoDAO = dao;
    }

    @Override
    public List<Alimento> buscarPorNome(String termo, int limit, int offset) {
        return alimentoDAO.buscarPorNome(termo, limit, offset);
    }
}
