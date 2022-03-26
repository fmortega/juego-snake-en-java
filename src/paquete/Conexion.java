
package paquete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Conexion {
  public static Connection conectar (){
    String conexionURL="jdbc:sqlserver://localhost:1433;"
            + "database=JuegoSnake;user=sa;password=1234;";
      
      try {
          Connection con = DriverManager.getConnection(conexionURL);
          return con;
      } catch (SQLException ex) {
          Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
      
  }  
}
