package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * Dibuja una pieza del escenario y guarda su base si ocupa espacio en el piso.
 */
public class Decoration {
    private Image image;
    private Rectangle bounds;
    private Polygon base;

    /**
     * Construye una decoracion que solo se dibuja.
     *
     * @param image Imagen de la decoracion.
     * @param bounds Posicion y dimensiones del dibujo.
     */
    public Decoration(Image image, Rectangle bounds) {
        this(image, bounds, null);
    }

    /**
     * Construye una decoracion con una base medida desde la esquina del dibujo.
     *
     * @param image Imagen de la decoracion.
     * @param bounds Posicion y dimensiones del dibujo.
     * @param localBase Base en pixeles al tamano del dibujo, o null si es visual.
     */
    public Decoration(Image image, Rectangle bounds, Polygon localBase) {
        this.image = image;

        if (localBase == null) {
            this.bounds = bounds;
            base = null;
        } else {
            this.bounds = new Rectangle(bounds);
            base = new Polygon(localBase.xpoints, localBase.ypoints, localBase.npoints);
            base.translate(bounds.x, bounds.y);
        }
    }

    /**
     * Obtiene la base en coordenadas del mapa.
     *
     * @return Poligono de colision, o null si la decoracion es solo visual.
     */
    Polygon getBase() {
        return base;
    }

    /**
     * Obtiene la coordenada del extremo de la base mas cercano a la camara.
     *
     * @return Posicion vertical usada para ordenar las piezas del escenario.
     */
    int getDepth() {
        if (base == null) {
            // Las piezas que solo adornan se dibujan al fondo.
            return Integer.MIN_VALUE;
        }

        return (int) base.getBounds().getMaxY();
    }

    /**
     * Comprueba si esta pieza debe tapar al jugador al pasar por detras.
     *
     * @param sprite Rectangulo que ocupa el dibujo del jugador.
     * @param feet Rectangulo de los pies del jugador.
     * @return true si la base queda delante de los pies a esa altura del mapa.
     */
    boolean isInFrontOf(Rectangle sprite, Rectangle feet) {
        if (base == null || !bounds.intersects(sprite)) {
            return false;
        }

        Rectangle baseBounds = base.getBounds();
        double footY = feet.getMaxY();
        double frontY = baseBounds.getMaxY();

        if (footY >= frontY) {
            return false;
        }

        // Busca el borde de la base a la altura horizontal de los pies.
        double footX = feet.getCenterX();
        footX = Math.max(footX, baseBounds.x);
        footX = Math.min(footX, baseBounds.getMaxX() - 1);

        // Si la linea desde los pies hacia el frente cruza la base, la pieza lo tapa.
        return base.intersects(footX, footY, 1, frontY - footY);
    }

    /**
     * Cambia la imagen de la pieza cuando cambia de estado.
     *
     * @param image Nueva imagen de la decoracion.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Dibuja la imagen de la pieza en su posicion dentro del mapa.
     *
     * @param graphics Contexto grafico de Swing.
     */
    public void draw(Graphics graphics) {
        graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
    }
}