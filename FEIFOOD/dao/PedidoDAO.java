package dao;

import model.Pedido;
import model.PedidoItem;

import java.util.List;

public interface PedidoDAO {
    int criar(Pedido p);
    void atualizarStatus(int pedidoId, String status);
    void remover(int pedidoId);
    void salvarItens(int pedidoId, List<PedidoItem> itens);
}
