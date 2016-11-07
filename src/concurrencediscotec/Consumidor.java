/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencediscotec;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik
 */
public class Consumidor extends Thread {

    @Override
    public void run() {
        try {
            //System.out.println("Consumidor : ");
            Lienzo.consumir.Wait();
            //System.out.println("ingreso : ");
            Lienzo.mutex.Wait();
            //System.out.println("ingreso 2: ");
            Lienzo.nro_chicos_banio = Lienzo.nro_chicos_banio -1;
            System.out.println("consumir chicos baño : "
                    + Lienzo.nro_chicos_banio);
            Lienzo.posicion_cola_varones = Lienzo.posicion_cola_varones + 30;        
            
            Lienzo.mutex.Signal();
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
