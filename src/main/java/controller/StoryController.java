package controller;

import model.StoryScene;
import view.StoryPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Clase para escuchar el evento de presionar ciertas teclas
 * y adelantar el texto que se genere en los paneles
 */
public class StoryController extends KeyAdapter {
    private StoryPanel panel;
    private StoryScene[] scenes;
    private int sceneIndex;
    private int lineIndex;
    private TypewriterThread typer;
    private boolean finished;

    /**
     * Crea la escena con la vista y el modelo
     *
     * @param panel  parte visual de los paneles
     * @param scenes parte logica de la obtencion de imagenes y texto
     */
    public StoryController(StoryPanel panel, StoryScene[] scenes) {
        this.panel = panel;
        this.scenes = scenes;
        this.sceneIndex = 0;
        this.lineIndex = 0;
        this.finished = false;
    }

    /**
     * Inicia la cinematica de manera que si
     * el jugador reinicia o vuelve a jugar
     * empiece desde el inicio
     */
    public void start() {
        finished = false;
        sceneIndex = 0;
        lineIndex = 0;
        showScene();

    }

    /**
     * Crea una linea que extrae las lines de la escena
     * o el guion, crea el hilo con el panel y la linea
     * e inicia el hilo
     */
    private void showLine() {
        String line = scenes[sceneIndex].getLine(lineIndex);
        typer = new TypewriterThread(panel, line);
        typer.start();
    }

    /**
     * Vuelve a la bandera finished true
     * y muestra en la pantalla que se ha acabado la historia
     */
    private void finishStory() {
        finished = true;
        System.out.println("Fin de la historia");
    }

    /**
     * Mete la imagen en el background del panel y muestra
     * la linea en la barrita de informacion
     */
    private void showScene() {
        panel.setBackgroundImage(scenes[sceneIndex].getImagePath());
        showLine();
    }

    /**
     * Metodo que escucha la tecla de adelantar, que es el espacio o el enter
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            next();
        }
    }

    /**
     * Avanza la historia cada vez que el jugador presiona la tecla de adelantar.
     * Si la historia ya termino, no hace nada. Si el texto todavia se esta
     * escribiendo, lo muestra completo de una vez. Si no, pasa a la siguiente
     * linea de la escena; si la escena ya no tiene mas lineas, pasa a la
     * siguiente escena, y si ya no hay mas escenas, termina la historia.
     */
    public void next() {
        if (finished) {                     //la historia ya termino
            return;
        }
        if (typer.isTyping()) {             //texto aun escribiendose
            typer.skip();                   //adelantar todoo

        } else {
            lineIndex++;                    //siguiente linea
            if (lineIndex < scenes[sceneIndex].getCountLines()) {
                showLine();                  //mientras todavia haya lineas, mostrar lineas
            } else {
                sceneIndex++;               //siguiente escena
                lineIndex = 0;              //primera linea
                if (sceneIndex < scenes.length) {
                    showScene();            //todavia hay escenas

                } else {
                    finishStory();          //terminar la historia
                }
            }
        }
    }
}
