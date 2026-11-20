package controller;

import view.StoryPanel;

/**
 * Clase que sirve para controlar el tiempo
 * en el que aparece el texto en la barra de informacion
 * mediante hilos
 *
 * @author Mariana Ocampo
 * @version 1.0
 *
 */
public class TypewriterThread extends Thread {
    /**
     * Estado corriendo true or false
     */
    private boolean running;
    /**
     * Palabras que se muestran en la barra de informacion
     */
    private String text;
    /**
     * Panel de historia
     */
    private StoryPanel panel;

    /**
     * Crea el hilo inicializando sus valores
     *
     * @param panel el panel de la historia
     * @param text  el texto que se muestra en la barra de informacion
     *
     */
    public TypewriterThread(StoryPanel panel, String text) {
        this.running = false;
        this.panel = panel;
        this.text = text;

    }

    /**
     * Sobrescribe el metodo run de Thread para que muestre las letras una por una
     */
    @Override
    public void run() {
        running = true;
        int visibleChars = 0;
        while (running && visibleChars < text.length()) {
            visibleChars++;
            panel.showText(text.substring(0, visibleChars));
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                //si el jugador adelanta el texto
                running = false;
            }

        }
        panel.showText(text);

    }

    /**
     * Adelanta la animacion
     *
     */
    public void skip() {
        interrupt();
    }

    /**
     *
     * @return retorna verdadero si la barra todavia esta escribiendo
     *
     */
    public boolean isTyping() {
        return isAlive();
    }
}
