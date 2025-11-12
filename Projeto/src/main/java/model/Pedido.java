package model;
import java.time.LocalDateTime; import java.util.LinkedHashMap; import java.util.Map;
public class Pedido {
    private Integer id; private Usuario usuario; private String status="CRIADO"; private LocalDateTime criadoEm=LocalDateTime.now(); private LocalDateTime atualizadoEm;
    private final Map<Integer, PedidoItem> itens=new LinkedHashMap<>();
    public void adicionarItem(Alimento a,int qtd){ itens.merge(a.getId(), new PedidoItem(a,qtd,a.getPreco()), (o,n)->{o.setQuantidade(o.getQuantidade()+qtd); return o;}); }
    public void removerItem(Alimento a){ itens.remove(a.getId()); }
    public double total(){ return itens.values().stream().mapToDouble(i->i.getPrecoUnitario()*i.getQuantidade()).sum(); }
    public Map<Integer,PedidoItem> getItens(){return itens;}
    public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
    public Usuario getUsuario(){return usuario;} public void setUsuario(Usuario u){this.usuario=u;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public LocalDateTime getCriadoEm(){return criadoEm;} public void setCriadoEm(LocalDateTime c){this.criadoEm=c;}
    public LocalDateTime getAtualizadoEm(){return atualizadoEm;} public void setAtualizadoEm(LocalDateTime a){this.atualizadoEm=a;}
}
