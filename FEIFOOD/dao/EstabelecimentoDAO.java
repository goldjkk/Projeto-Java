package dao;

import model.Estabelecimento;
import java.util.List;
import java.util.Optional;

public interface EstabelecimentoDAO {
    void salvar(Estabelecimento e);
    void atualizar(Estabelecimento e);
    List<Estabelecimento> listarAtivos();
    Optional<Estabelecimento> buscarPorId(int id);
}
