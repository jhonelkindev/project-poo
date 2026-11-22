package model;

import javax.swing.*;
import java.awt.*;

/**
 * Zona 2, sala de maquinas
 */
public class Zone2EngineRoom extends Zone {
    private Rectangle centralGenerator;
    private Rectangle cleaningTable;
    private Rectangle panel1;
    private Rectangle panel2;
    private Rectangle panel3;

    private Image backgroundImg;
    private Image generatorImg;
    private Image cleaningTableImg;
    private Image panelImg;

    public Zone2EngineRoom() {
        super("Zona 2 sala de maquinas");

        int[] xPoints = {581, 1210, 719, 67};
        int[] yPoints = {158, 343, 686, 441};
        this.floorArea = new Polygon(xPoints, yPoints, 4);

        this.centralGenerator = new Rectangle(545, 285, 125, 100);
        this.cleaningTable = new Rectangle(785, 355, 95, 70);
        this.panel1 = new Rectangle(375, 195, 55, 75);
        this.panel2 = new Rectangle(715, 145, 55, 75);
        this.panel3 = new Rectangle(1065, 235, 55, 75);

        this.backgroundImg = new ImageIcon(getClass().getResource("/mapas_niveles/nivel2_fondo.png")).getImage();
        this.generatorImg = new ImageIcon(getClass().getResource("/frames/Nivel-2/generador/generador_danado.png")).getImage();
        this.cleaningTableImg = new ImageIcon(getClass().getResource("/frames/Nivel-2/mesa_limpieza/mesa_limpieza_inactiva_spritesheet.png")).getImage();
        this.panelImg = new ImageIcon(getClass().getResource("/frames/Nivel-2/tablero_electrico/tablero_descalibrado.png")).getImage();
    }


    @Override
    /**
     * Actualiza la logica de la sala de maquinas
     */
    public void update(Agent agent) {

    }


    @Override
    public void draw(Graphics g, Agent agent) {
        g.drawImage(backgroundImg, 0, 0, 1280, 720, null);

        g.drawImage(cleaningTableImg, cleaningTable.x, cleaningTable.y, cleaningTable.width, cleaningTable.height, null);
        g.drawImage(generatorImg, centralGenerator.x, centralGenerator.y, centralGenerator.width, centralGenerator.height, null);
        g.drawImage(panelImg, panel1.x, panel1.y, panel1.width, panel1.height, null);
        g.drawImage(panelImg, panel2.x, panel2.y, panel2.width, panel2.height, null);
        g.drawImage(panelImg, panel3.x, panel3.y, panel3.width, panel3.height, null);

        g.setColor(new Color(20, 23, 28));
        g.fillRect(0, 0, 1280, 50);
    }


    /**
     * Indica si la zona 2 fue completado
     *
     * @return true si el nivel fue completado, al contrario false
     */
    @Override
    public boolean isCompleted() {
        return false;
    }


}
