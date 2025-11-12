package model;
public abstract class Alimento {
    protected Integer id; protected Estabelecimento estabelecimento; protected String nome; protected String descricao; protected double preco; protected boolean ativo=true;
    public abstract String getTipo();
    public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
    public Estabelecimento getEstabelecimento(){return estabelecimento;} public void setEstabelecimento(Estabelecimento e){this.estabelecimento=e;}
    public String getNome(){return nome;} public void setNome(String nome){this.nome=nome;}
    public String getDescricao(){return descricao;} public void setDescricao(String descricao){this.descricao=descricao;}
    public double getPreco(){return preco;} public void setPreco(double preco){this.preco=preco;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean ativo){this.ativo=ativo;}
}
