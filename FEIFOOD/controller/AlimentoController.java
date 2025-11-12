package controller; import model.Alimento; import java.util.*; public interface AlimentoController { List<Alimento> buscarPorNome(String termo,int limit,int offset); }
