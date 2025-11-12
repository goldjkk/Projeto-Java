package model;
public class Estabelecimento {
    private Integer id; private String nome; private String cnpj; private String cidade; private boolean ativo=true;
    public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
    public String getNome(){return nome;} public void setNome(String nome){this.nome=nome;}
    public String getCnpj(){return cnpj;} public void setCnpj(String cnpj){this.cnpj=cnpj;}
    public String getCidade(){return cidade;} public void setCidade(String cidade){this.cidade=cidade;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean ativo){this.ativo=ativo;}
}
