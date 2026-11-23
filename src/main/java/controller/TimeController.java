package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que permite a traves del timer implementar un cronometro
 * para los niveles.
 *
 * @author Mariana Ocampo
 * @version 1.0
 */
public class TimeController implements ActionListener {
    /**
     * Timer que nos permite crear los cronometros de los niveles
     */
    private Timer timer;
    /**
     * segundos iniales
     */
    private int initialSeconds;
    /**
     * segundos que quedan o faltan
     */
    private int secondsLeft;
    /**
     * boolean que devuelve si ya acabo el tiempo
     */
    private boolean timerOver;

    /**
     * crea el cronometro con el tiempo del nivel
     *
     * @param initialSeconds tiempo en el que empieza el cronometro
     */
    public TimeController(int initialSeconds) {
        this.initialSeconds = initialSeconds;
        this.secondsLeft = initialSeconds;
        this.timerOver = false;
        this.timer = new Timer(1000, this);
    }

    /**
     * Implementa el metodo de ActionListener.
     * Se ejecuta cada segundo: resta un segundo y,
     * si llega a 0, marca que se acabó el tiempo y detiene el cronómetro
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        secondsLeft--;
        if (secondsLeft <= 0) {
            secondsLeft = 0;
            timerOver = true;
            timer.stop();
        }
    }


    /**
     * Iniciar el cronometro
     */
    public void start() {
        timer.start();
    }

    /**
     * Detener el cronometro
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Reiniciar el cronometro con nuevo tiempo, no lo empieza
     *
     * @param seconds segundos con los que empieza de nuevo
     */
    public void reset(int seconds) {
        timer.stop();
        initialSeconds = seconds;
        secondsLeft = seconds;
        timerOver = false;
    }

    /**
     *
     * @return se acabo el tiempo?
     */
    public boolean isTimerOver() {
        return timerOver;
    }

    /**
     *
     * @return cuantos segundos quedan
     */
    public int getSecondsLeft() {
        return secondsLeft;
    }

    /**
     *
     * @return tiempo en el que inicia el timer
     */
    public int getInitialSeconds() {
        return initialSeconds;
    }

    /**
     *
     * @return el tiempo jugado
     */
    public int getSecondsPlayed() {
        return initialSeconds - secondsLeft;
    }

    /**
     *
     * @return el cronometro escrito con formato
     */
    public String getCronTime() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
