/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controladordatos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alienware
 */
public final class SQLiteUtil {

    private Connection con;
    private Statement smt;
    
    public SQLiteUtil() throws SQLException, ClassNotFoundException {
        File f = new File("database");
        if (!f.exists()) {
            f.mkdirs();
        }
        
        f = new File("database/armas.db");
        if (!f.exists()) {
            conectar();
            createTable();
            desconectar();
        }
    }
    
    public void conectar() {
        try {
            desconectar();
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            con = DriverManager.getConnection("jdbc:sqlite:database/armas.db");
            con.setAutoCommit(false);
            smt = con.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SQLiteUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desconectar() throws SQLException {
        if (smt != null) {
            smt.close();
            smt = null;
        }
        if (con != null) {
            con.commit();
            con.close();
            con = null;
        }
    }
    
    public ResultSet consulta(Integer pk) {
        try {
            // Conectar a la base de datos
            conectar();
            String sql = "SELECT * FROM armas";
            
            if (pk > 0) {
                sql += " WHERE armas_id = " + pk;
            }
            ResultSet resultado = smt.executeQuery(sql);
            return resultado;
            
        } catch (Exception ex) {
            System.out.println("Error en la consulta");
        }
        return null;
    }

    // Metodo dinamico de construir un update de SQL a partir de un Map con campos y valores
    public Integer actualizar(String tabla, String pk_name, Integer pk, Map datos) {
        try {
            conectar();
            StringBuilder campos = new StringBuilder();
            
            for (Iterator it = datos.keySet().iterator(); it.hasNext();) {
                String llave = (String) it.next();
                campos.append(llave).append("=");
                if (datos.get(llave) instanceof Date) {
                    campos.append("'")
                          .append(new java.sql.Date(((Date) datos.get(llave)).getTime()).toString()).append("',");
                } else {
                    campos.append("'")
                           .append(datos.get(llave).toString()).append("',");
                }
            }
            
            String sql = "UPDATE " + tabla + " SET " +
                    campos.toString().substring(0, campos.toString().length() - 1) +
                    " WHERE " + pk_name + "=" + pk;
            
            System.out.println(sql);
            
            int registrosAfectados = smt.executeUpdate(sql);
            System.out.println("Registros afectados: " + registrosAfectados + " registros");
            
            desconectar();
            return registrosAfectados;
            
        } catch (Exception ex) {
            System.out.println("Error en la actualizacion: " + ex.toString());
        }
        
        return 0;
    }

    // Metodo dinamico para construir un insert a partir de un Map con campos y valores
    public Integer insertar(String tabla, Map datos) {
        try {
            conectar();
            String sql;
            StringBuilder campos = new StringBuilder();
            StringBuilder valores = new StringBuilder();
            
            for (Iterator it = datos.keySet().iterator(); it.hasNext();) {
                String llave = (String) it.next();
                campos.append(llave).append(",");
                if (datos.get(llave) instanceof Date) {
                    valores.append("'").append(new SimpleDateFormat("yyyy-MM-dd").format((Date) datos.get(llave))).append("',");
                } else {
                    valores.append("'").append(datos.get(llave).toString()).append("',");
                }
            }
            sql = "INSERT INTO " + tabla + " ("
                    + campos.toString().substring(0, campos.toString().length() - 1)
                    + ") VALUES ("
                    + valores.toString().substring(0, valores.toString().length() - 1)
                    + ")";
            
            int registrosAfectados = smt.executeUpdate(sql.toString());
            System.out.println("Registros afectados: " + registrosAfectados + " registros");
            desconectar();
            return registrosAfectados;
            
        } catch (Exception ex) {
            System.out.println("Error en la insercion");
        }
        return 0;
    }
    
    public Integer borrar(Integer pk) {
        
        try {
            conectar();
            String sql = "DELETE FROM armas WHERE armas_id = " + pk;
            
            int registrosAfectados = smt.executeUpdate(sql);
            desconectar();
            System.out.println("Registros afectados: " + registrosAfectados + " registros");
            return registrosAfectados;
        } catch (Exception e) {
            System.out.println("Error en el borrado");
        }
        return 0;
    }
    
    public void createTable() throws SQLException, ClassNotFoundException {
        smt.setQueryTimeout(30);
        smt.executeUpdate("DROP TABLE IF EXISTS armas");
        
        smt.executeUpdate("CREATE TABLE IF NOT EXISTS  armas (armas_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  ,marca VARCHAR(120)  NULL DEFAULT NULL COLLATE BINARY,calibre VARCHAR(30)  NULL DEFAULT NULL COLLATE BINARY,serie VARCHAR(39)  NULL DEFAULT NULL COLLATE BINARY,fecha_registro DATE  NULL DEFAULT NULL ,tipo VARCHAR(60)  NULL DEFAULT NULL COLLATE BINARY)");
        smt.executeUpdate("INSERT INTO armas VALUES(1,'Beretta','9mm','423532534','2013-12-12','Pistola')");
        smt.executeUpdate("INSERT INTO armas VALUES(2,'Smith and Wesson','.45','645645634','2014-11-12','Revolver')");
        
        smt.executeUpdate("DELETE FROM sqlite_sequence");
        smt.executeUpdate("INSERT INTO sqlite_sequence VALUES('armas',3)");
    }
    
    public static String resultadosSQL(ResultSet result) throws SQLException {
        String salida = "";
        ResultSetMetaData rsMeta = (ResultSetMetaData) result.getMetaData();
        for (int i = 1; i < rsMeta.getColumnCount() + 1; i++) {
            salida += rsMeta.getColumnName(i) + "\t";
            //System.out.print(rsMeta.getColumnName(i) + "\t");
        }
        salida += "\n";
        //System.out.println("");

        int contador = 0;
        while (result.next()) {
            for (int i = 1; i < rsMeta.getColumnCount() + 1; i++) {
                salida += result.getString(rsMeta.getColumnName(i)) + "\t";
                //System.out.print(result.getString(rsMeta.getColumnName(i)) + "\t");
            }
            salida += "\n";
            //System.out.println("");

        }
        return salida;
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        System.out.println(resultadosSQL(new SQLiteUtil().consulta(0)));
    }
}
