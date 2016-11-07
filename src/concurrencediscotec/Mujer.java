/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencediscotec;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Erik
 */
public class Mujer extends Thread {

    private Color color;
    /* default*/
    private int coordenadaX;
    private int coordenadaY;
    private JPanel panel;
    private Graphics graphics;
    private int index;
    /* delay*/
    private int delay;

    public Mujer(int index,
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
                    /* manejamos la region crtica*/
                    Lienzo.estaLibre.Wait();
                    if (Lienzo.nro_chicos_cola == 0
                            && Lienzo.nro_chicos_banio == 0
                            && Lienzo.nro_chicas_banio == 0) {

                        /* System.out.println("#### : "
                                + Lienzo.nro_chicos_cola
                                + " - " + Lienzo.nro_chicos_banio
                                + " - " + Lienzo.nro_chicas_banio);
                         */ ingresarBanio(); // ++
                        Lienzo.chicaDentro.Signal();
                        /* protocolo de salida*/
                        Lienzo.chicaDentro.Wait();
                        sleep(4000);
                        SalirBanio(); // --
                        Lienzo.estaLibre.Signal();
                    } else {
                        esperarEnLaCola();
                        Lienzo.estaLibre.Signal();
                        //  System.out.println("no esta libre asi que lo liberamos ");
                    }

                } else if (salioDelBanio()) {
                    //System.out.println("corre");
                    Lienzo.estaLibre.Wait();
                    coordenadaX = coordenadaX + 1;
                    getPanel().repaint();
                    /*repintar*/
                    sleep(20);

                    Lienzo.estaLibre.Signal();

                } else if (salioDeDiscoteca()) {
                    terminar = false;

                } else {

                    //System.out.println("corre");
                    coordenadaX = coordenadaX + 1;
                    getPanel().repaint();
                    /*repintar*/
                    sleep(15);
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
        Lienzo.nro_chicas_banio = Lienzo.nro_chicas_banio + 1;

        setCordenadaX(coordenadaX + 50);
        getPanel().repaint();
        //System.out.println("CHICA Ingreso al baño ");
    }

    public void SalirBanio() throws InterruptedException {
        Lienzo.nro_chicas_banio = Lienzo.nro_chicas_banio - 1;
        //System.out.println("CHICA Ingreso al baño ");
        setCordenadaX(700); // para que salga del baño
    }

    public void esperarEnLaCola() {
        // nro_chicas_cola ++; // no es necesario por la prioridad de varones
        coordenadaX = 499;
    }
}
