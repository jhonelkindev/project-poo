package model;

/**
 * Clase que guarda la lógica de las escenas,
 * guarda las lineas y la direccion en donde estan los recursos
 * visuales de la escena.
 *
 * @author Mariana Ocampo
 * @version 1.0
 */
public class StoryScene {
    private String[] lines;
    private String imagePath;

    /**
     * Crear una escena de la historia
     *
     * @param imagePath ruta de la imagen
     * @param lines     numero de lineas que van en la barra de informacion
     */
    public StoryScene(String imagePath, String[] lines) {
        this.imagePath = imagePath;
        this.lines = lines;
    }

    /**
     *
     * @return la ruta de la imagen
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     *
     * @return la cantidad de lineas que hay en el guion
     */
    public int getCountLines(){
        return lines.length;
    }

    /**
     *
     * @param index posicion de la linea en la barra de informacion
     * @return  el texto de esa linea
     */
    public String getLine(int index){
        return lines[index];
    }
}
