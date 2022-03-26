
package paquete;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Caminante implements Runnable {

    public static int velocidad = 200;

    PanelSnake panel;
    static boolean estado = true;

    public Caminante(PanelSnake panel) {
        this.panel = panel;
    }

 

    @Override
    public void run() {
        while (estado) {
            try {
                
                    panel.avanzar();

                    panel.repaint();
             

                Thread.sleep(velocidad);
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void parar() {
        estado = false;
    }

    public void star() {
        estado = true;
        new Thread(this).start();
    }
}
