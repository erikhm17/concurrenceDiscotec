/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencediscotec;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import static java.lang.Thread.sleep;

/**
 *
 * @author Erik
 */
public class Varon extends Thread {

    private Color color;
    /* default*/
    private int coordenadaX;
    private int coordenadaY;
    private JPanel panel;
    private Graphics graphics;
    private int index;
    /* delay*/
    private int delay;

    public Varon(int index,
            JPanel panel,
            Graphics graphics,
            int coordenadaX,
            int coordenadaY,
            int delay,
            Color color
    ) {
        this.panel = panel;
        this.graphics = graphics;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.index = index;
        this.delay = delay;
        this.color = color;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;

    }

    public int getCordenadaX() {
        return coordenadaX;
    }

    public void setCordenadaX(int cordenadaX) {
        this.coordenadaX = cordenadaX;

    }

    public int getCordenadaY() {
        return coordenadaY;
    }

    public void setCordenadaY(int cordenadaY) {
        this.coordenadaY = cordenadaY;

    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
    private boolean terminar = true;

    @Override
    public void run() {
        while (terminar) {
            try {
                if (llegoAlBanio()) {
                    if (Lienzo.nro_chicas_banio>0) {
                        Lienzo.mutex.Wait();
                        esperarEnLaCola();
                        Lienzo.mutex.Signal();
                    }
                    
                    /* manejamos la region crtica*/
                    Lienzo.estaLibre.Wait();
                    if (Lienzo.nro_chicos_banio <= 5) {
//                        
//                        if (Lienzo.nro_chicos_cola > 0) {
//                            Lienzo.nro_chicos_cola = Lienzo.nro_chicos_cola - 1;
//                        }

                        Lienzo.mutex.Wait();
                        //Lienzo.nro_chicos_banio = Lienzo.nro_chicos_banio +1;
                        ingresarBanio();
                        Lienzo.mutex.Signal();
                        Lienzo.consumir.Signal();
                        Lienzo.estaLibre.Signal();

                        sleep((int) (Math.random() * 10000));

                        /* protocolo de salida*/
                        Lienzo.consumir.Wait();
                        Lienzo.mutex.Wait();

                        Lienzo.nro_chicos_banio = Lienzo.nro_chicos_banio - 1;
                        if (Lienzo.nro_chicos_cola>0) {
                            Lienzo.nro_chicos_cola = Lienzo.nro_chicos_cola-1;
                        }
                        Lienzo.posicion_cola_varones = Lienzo.posicion_cola_varones + 30;
                        System.out.println("salio nro : "+Lienzo.nro_chicos_cola);
                        
                        Lienzo.mutex.Signal();
                        //Lienzo.nro_chicos_banio = Lienzo.nro_chicos_banio -1;                        Lienzo.estaLibre.Signal();

                    } else if (Lienzo.nro_chicos_cola > 5) {
                        System.out.println("hay chica en el baño ");
                        esperarEnLaCola();
                        Lienzo.estaLibre.Signal();
                    }

                } else if (salioDelBanio()) {
                    //System.out.println("corre");
                    coordenadaX = coordenadaX + 1;
                    getPanel().repaint();
                    /*repintar*/
                    sleep(20);
                } else if (salioDeDiscoteca()) {
                    terminar = false;
                } else {
                    coordenadaX = coordenadaX + 1;
                    getPanel().repaint();
                    /*repintar*/
                    sleep(20);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Mujer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean llegoAlBanio() {
        if (coordenadaX == 500) {
            return true;
        }
        return false;
    }

    public boolean salioDelBanio() {
        if (coordenadaX == 700) {
            
            return true;
        }
        return false;
    }

    public boolean salioDeDiscoteca() {
        if (coordenadaX == 1000) {
            return true;
        }
        return false;
    }

    public void ingresarBanio() throws InterruptedException {
        Lienzo.nro_chicos_banio = Lienzo.nro_chicos_banio + 1;
        setCordenadaX(coordenadaX + 50);
        setCordenadaY(coordenadaY + Lienzo.posicion_cola_varones);
        Lienzo.posicion_cola_varones = Lienzo.posicion_cola_varones - 30;
        getPanel().repaint();
        //System.out.println("CHICO Ingreso al baño ");
    }

    public void esperarEnLaCola() {
        Lienzo.nro_chicos_cola = Lienzo.nro_chicos_cola + 1;
        coordenadaX = 499;
    }
}
