/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencediscotec;

/**
 *
 * @author Erik
 */
class Semaforo {

    protected long a_permisos;

    public Semaforo() {
        a_permisos = 0;
    }

    public Semaforo(long p_permisos) {
        a_permisos = p_permisos;
    }

    public long getA_permisos() {
        return a_permisos;
    }

    public void setA_permisos(long a_permisos) {
        this.a_permisos = a_permisos;
    }

    /* signal*/
    public synchronized void Signal() {
        ++a_permisos;
        /* despierta al primero que esta en la cola de los bloqueados,
        si existe algun proceso*/
        //System.err.println("Sgnal activados : "+getA_permisos());
        notify();
    }

    /* wait*/
    public void Wait() throws InterruptedException {
        /*
            si fue interrumpido lanza una excepcion
         */
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        synchronized (this) {
            /* se ejecuta hasta que termine todo este bloque de codigo*/
            try {
                /* mientras el contador sea <= 0 espera*/
                //System.err.println("Wait activados antes : "+getA_permisos());
                while (a_permisos <= 0) {
                    wait();
                }
                --a_permisos;
                /*permisos le bajo 1*/
                //System.err.println("Wait activados despues: "+getA_permisos());
            } catch (InterruptedException ie) {
                /* en caso de presentar problemas con el hilo 
                    notificar a todos los que estan en la seccion bloqueado
                    a preparado
                 */
                notifyAll();
                throw ie;
            }
        }
    }
}
