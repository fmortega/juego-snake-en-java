package paquete;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelFondo extends JPanel {

    Color colorFondo = Color.gray;
    int tanmax, tam, can,res;

    public PanelFondo(int tanmax, int can) {
    this.tanmax = tanmax;
    this.can= can;
    this.tam=tanmax/can;
    this.res= tanmax%can;
    }
    @Override
    public void paint(Graphics pintor){
    super.paint(pintor);
    pintor.setColor(colorFondo);
        for (int i = 0; i < can; i++) {
            for (int j = 0; j < can; j++) {
                pintor.fillRect(res/2+i*tam, res/2+j*tam, tam-1, tam-1);
            }
        }
    }
}
