package controller;
import model.*; import dao.PedidoDAO; import java.util.*;
public class PedidoController {
    private final PedidoDAO pedidoDAO; private Pedido pedidoAtual;
    public PedidoController(PedidoDAO pedidoDAO){ this.pedidoDAO=pedidoDAO; }
    public void novoPedido(Usuario u){ pedidoAtual=new Pedido(); pedidoAtual.setUsuario(u); int id=pedidoDAO.criar(pedidoAtual); pedidoAtual.setId(id); }
    public Pedido getPedido(){ return pedidoAtual; }
    public void adicionar(Alimento a,int qtd){ pedidoAtual.adicionarItem(a,qtd); }
    public void remover(Alimento a){ pedidoAtual.removerItem(a); }
    public void salvarItens(){ List<PedidoItem> itens=new ArrayList<>(pedidoAtual.getItens().values()); pedidoDAO.salvarItens(pedidoAtual.getId(), itens); }
    public void excluirPedido(){ pedidoDAO.remover(pedidoAtual.getId()); pedidoAtual=null; }
}
