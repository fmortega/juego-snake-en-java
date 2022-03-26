package paquete;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static paquete.Vista.id;

public class PanelSnake extends JPanel {

    int nivel = 2;
    int id;
    int record;
    int puntaje;
    String nombre;
    Color colorSnake = Color.black;
    Color colorComida = Color.ORANGE;
    int tanmax, tam, can, res;
    List<int[]> snake = new ArrayList<>();
    int[] comida = new int[2];
    String direccion = "de";
    String direccionProxima = "de";
    Thread hilo;
    Thread hilo2;
    Caminante camino;

    String respuesta[] = {"Continuar Jugando", "Salir del juego"};
    Icon icono = new ImageIcon(new ImageIcon(getClass().getResource("/paquete/perder.png")).getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));

    public PanelSnake(int tanmax, int can) {
        this.tanmax = tanmax;
        this.can = can;
        this.tam = tanmax / can;
        this.res = tanmax % can;
        int[] a = {can / 2 - 1, can / 2 - 1};
        int[] b = {can / 2, can / 2 - 1};

        snake.add(a);
        snake.add(b);
        generarComida();

        camino = new Caminante(this);
        hilo = new Thread(camino);
        hilo.start();

    }

    public PanelSnake() {
    }//Constructor vacio

    @Override
    public void paint(Graphics pintor) {
        super.paint(pintor);
        pintor.setColor(colorSnake);
// pintado serpiente
        for (int[] par : snake) {
            pintor.fillRect(res / 2 + par[0] * tam, res / 2 + par[1] * tam, tam - 1, tam - 1);
        }
        // pintando comida
        pintor.setColor(colorComida);

        pintor.fillRect(res / 2 + comida[0] * tam, res / 2 + comida[1] * tam, tam - 1, tam - 1);

    }

    public void avanzar() {
        id = Login.id;
        nombre = Login.nombre;
        record = Login.consultaPuntaje();
        Vista.lblRecord.setText(" " + record);
        Vista.lblusuario.setText(" " + nombre);
        igualarDir();
        int[] ultimo = snake.get(snake.size() - 1);
        int agregarX = 0;
        int agregarY = 0;
        switch (direccion) {
            case "de":
                agregarX = 1;
                break;
            case "iz":
                agregarX = -1;
                break;
            case "ar":
                agregarY = -1;
                break;
            case "ab":
                agregarY = 1;
                break;
        }
        int[] nuevo = {(ultimo[0] + agregarX),
            (ultimo[1] + agregarY)};
        if (nuevo[0] >= can || nuevo[0] == -1 || nuevo[1] >= can || nuevo[1] == -1) {
            audioPierde();
            Caminante.estado = false;
            if (nivel > record) {
                JOptionPane.showMessageDialog(this, "Has alcanzado un nuevo record");
                actualizaPuntaje();
            }
            try {

                Thread.sleep(70);
            } catch (InterruptedException ex) {
                Logger.getLogger(PanelSnake.class.getName()).log(Level.SEVERE, null, ex);
            }

            int eleccion = JOptionPane.showOptionDialog(this, "!Perdiste!", "Fin", JOptionPane.PLAIN_MESSAGE, 0, icono, respuesta, this);
            if (eleccion == JOptionPane.YES_OPTION) {
                nuevo[0] = 10;
                nuevo[1] = 11;
                nivel = 2;

                Vista.lblnivel.setText(" " + nivel);
                Caminante.velocidad = 200;
                Caminante.estado = true;
                agregarX = 1;
                for (int i = 0; i < snake.size() - 1; i++) {
                    for (int j = 0; j < snake.size() - 1; j++) {
                        snake.remove(i);
                        if (snake.size() == 1) {
                            snake.add(nuevo);
                        }

                    }

                }
                if (snake.size() >= 3) {
                    snake.remove(0);
                }

            } else {
                System.exit(0);
            }

        }

        for (int i = 0; i < snake.size(); i++) {
            for (int j = 0; j < snake.size(); j++) {

            }
            if (nuevo[0] == snake.get(i)[0] && nuevo[1] == snake.get(i)[1]) {
                try {
                    Caminante.estado = false;
                    audioPierde();

                    Thread.sleep(70);
                    Caminante.estado = false;
                    if (nivel > record) {
                        JOptionPane.showMessageDialog(this, "Has alcanzado un nuevo record");
                        actualizaPuntaje();
                    }
                    int eleccion = JOptionPane.showOptionDialog(this, "!Perdiste!", "Fin", JOptionPane.PLAIN_MESSAGE, 0, icono, respuesta, this);
                    if (eleccion == JOptionPane.YES_OPTION) {
                        nuevo[0] = 11;
                        nuevo[1] = 10;

                        nivel = 2;
                        Vista.lblnivel.setText(" " + nivel);
                        Caminante.velocidad = 200;
                        Caminante.estado = true;
                        agregarX = 1;
                        for (int k = 0; k < snake.size() - 1; k++) {
                            for (int j = 0; j < snake.size() - 1; j++) {
                                snake.remove(k);
                                if (snake.size() == 1) {
                                    snake.add(nuevo);
                                }

                            }

                        }
                        if (snake.size() >= 3) {
                            snake.remove(0);
                        }

                    } else {
                        System.exit(0);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PanelSnake.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        if (nuevo[0] == comida[0] && nuevo[1] == comida[1]) {
            try {
                audio();
                Thread.sleep(70);
                snake.add(nuevo);
                nivel++;
                if (nivel > 5) {

                    Caminante.velocidad = 140;

                }
                if (nivel >= 20) {
                    Caminante.velocidad = 120;

                }
                  if (nivel >= 30) {
                    Caminante.velocidad = 110;

                }
                

                Vista.lblnivel.setText(" " + nivel);

                generarComida();
            } catch (InterruptedException ex) {
                Logger.getLogger(PanelSnake.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            snake.add(nuevo);
            snake.remove(0);
        }

    }

    public void generarComida() {
        boolean existe = false;
        int a = (int) (Math.random() * can);
        int b = (int) (Math.random() * can);
        for (int[] par : snake) {
            if (par[0] == a && par[1] == b) {
                existe = true;
                generarComida();
                break;
            }
        }
        if (!existe) {
            this.comida[0] = a;
            this.comida[1] = b;
        }
    }

    public void cambiarDireccion(String dir) {
        if (this.direccion.equals("de") || this.direccion.equals("iz") && dir.equals("ar") || dir.equals("ab")) {
            this.direccionProxima = dir;
        }
        if (this.direccion.equals("ar") || this.direccion.equals("ab") && dir.equals("iz") || dir.equals("de")) {
            this.direccionProxima = dir;
        }

    }

    public void igualarDir() {
        this.direccion = this.direccionProxima;
    }

    public void audio() {
        AudioClip sonido = java.applet.Applet.newAudioClip(getClass().getResource("/paquete/mario.wav"));
        sonido.play();
    }

    public void audioPierde() {
        AudioClip sonido = java.applet.Applet.newAudioClip(getClass().getResource("/paquete/audioPierde.wav"));
        sonido.play();
    }

    public void actualizaPuntaje() {
        Connection con = Conexion.conectar();
        try {
            CallableStatement cs = con.prepareCall("{call actualizapuntaje (?,?)}");
            cs.setString(1, String.valueOf(id));
            cs.setString(2, String.valueOf(nivel));
            int rs = cs.executeUpdate();
            if (rs>0) {
                JOptionPane.showMessageDialog(this, "Puntaje actualizado");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

}
