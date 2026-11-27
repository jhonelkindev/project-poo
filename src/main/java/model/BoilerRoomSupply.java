package model;

import javax.swing.*;
import java.awt.*;

/**
 * Ayuda que se activa al recogerla con F en el primer nivel. El agente
 * puede tomarla aunque ya lleve un repuesto en la mano.
 */
public class BoilerRoomSupply extends GameObject {
    /** Ayudas disponibles en los pasillos de las calderas. */
    public enum Type {
        IODINE("item_pastilla_yodo.png", 24, 24),
        LEAD_SUIT("item_traje_plomo.png", 32, 40);

        private String sprite;
        private int width;
        private int height;

        Type(String sprite, int width, int height) {
            this.sprite = sprite;
            this.width = width;
            this.height = height;
        }
    }

    private Type type;
    private Image image;

    /**
     * Ubica la pastilla de yodo o el traje de plomo en el piso.
     *
     * @param x Coordenada horizontal del objeto.
     * @param y Coordenada vertical del objeto.
     * @param type Ayuda que encontrará el jugador.
     */
    public BoilerRoomSupply(int x, int y, Type type) {
        super(x, y, type.width, type.height, false);
        this.type = type;
        image = new ImageIcon(getClass().getResource("/frames/power-rites/" + type.sprite)).getImage();
    }

    /**
     * Mantiene la pastilla disponible mientras el agente tenga las tres vidas.
     *
     * @param agent Jugador que intenta recoger esta ayuda.
     * @return true cuando la ayuda se puede aprovechar.
     */
    public boolean canUse(Agent agent) {
        return type == Type.LEAD_SUIT || agent.needsHealing();
    }

    /**
     * Recupera una vida o activa la protección temporal del traje.
     *
     * @param agent Jugador que recoge la ayuda.
     */
    @Override
    public void applyEffect(Agent agent) {
        if (type == Type.IODINE) {
            agent.recoverLife();
        } else {
            agent.wearLeadSuit();
        }
    }

    /**
     * Dibuja la ayuda en el piso de la sala.
     *
     * @param graphics Contexto gráfico de Swing.
     */
    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(image, x, y, widht, height, null);
    }
}