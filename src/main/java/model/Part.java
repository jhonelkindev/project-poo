package model;

import javax.swing.*;
import java.awt.*;

/**
 * Pieza del generador que el jugador recoge con F y suelta con Q
 */
public class Part extends GameObject {
    public static final String NUT = "Tuerca";
    public static final String CABLE = "Cable";
    private String type;
    private Image image;
    private boolean clean;

    /**
     * Crea una pieza en el mapa
     *
     * @param x    Cordenada horizaontal en pixeles
     * @param y    Coordenada vertical en pixeles
     * @param type Tipo de pieza part.nut o part.cable
     */
    public Part(int x, int y, String type) {
        super(x, y, 32, 32, false);
        this.type = type;
        this.clean = false;

        if (type.equals(NUT)) {
            image = new ImageIcon(getClass().getResource("/frames/power-rites/item_tuerca.png")).getImage();

        } else {
            image = new ImageIcon(getClass().getResource("/frames/power-rites/item_cable.png")).getImage();
        }
    }

    /**
     * Pone la pieza en el inventario del jugador.
     *
     * @param agent jugador que recoge la pieza
     */
    @Override
    public void applyEffect(Agent agent) {
        agent.carryPart(this);
    }

    /**
     * Dibuja la pieza en el piso. Si esta sucia se dibuja con un brillo verde radiactivo.
     *
     * @param graphics Contexto grafico de Swing.
     */
    @Override
    public void draw(Graphics graphics) {
        if (!clean) {
            graphics.setColor(new Color(80, 255, 80, 110));
            graphics.fillOval(x, y + 4, widht + 2, height - 2);
        }
        graphics.drawImage(image, x, y, widht, height, null);
    }

    /**
     * Indica si la pieza ya fue descontaminada en la mesa de limpieza.
     *
     * @return true si la pieza esta limpia.
     */
    public boolean isClean() {
        return clean;
    }

    /**
     * Marca la pieza como limpia.
     */
    public void cleanPart() {
        clean = true;
    }

    /**
     * Deja la pieza otra vez en el piso en una nueva posicion.
     *
     * @param x Nueva posicion horizontal.
     * @param y Nueva posicion vertical.
     */
    public void placeAt(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    /**
     * Obtiene el tipo de la pieza.
     *
     * @return part.nut o part.cable.
     */
    public String getType() {
        return type;
    }
}