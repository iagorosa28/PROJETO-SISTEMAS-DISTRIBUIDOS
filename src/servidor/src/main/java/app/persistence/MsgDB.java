package app.persistence;

import java.util.*;
import java.sql.*;

public class MsgDB{

    private final String url;
    private final String table;

    public MsgDB(String url, String table){
        this.url = url;
        this.table = table;
        criarTabela(); // se não existir, criar
    }

    private void criarTabela(){
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                 "nomeOrigem TEXT NOT NULL, " +
                 "nomeDestino TEXT NOT NULL, " +
                 "mensagem TEXT NOT NULL, " +
                 "data TEXT NOT NULL" +
                 ")";
        try(Connection c = DriverManager.getConnection(url); // abre conexão com o banco (dentro do try fecha automaticamente)
            Statement st = c.createStatement()){
            st.execute(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMsg(String origem, String destino, String mensagem, String data) {
        String sql = "INSERT INTO " + table +
                    " (nomeOrigem, nomeDestino, mensagem, data) VALUES (?, ?, ?, ?)";
        try (Connection c = DriverManager.getConnection(url);
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, origem);
            ps.setString(2, destino);
            ps.setString(3, mensagem);
            ps.setString(4, data);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
