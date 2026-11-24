package model;

import javax.swing.*;
import java.awt.*;

public class Zone3CoreRoom extends Zone {

    private Image background;
    private Rectangle core;
    private Image coreImg;
    private Rectangle redDispenser;
    private Image redDispenserImg;
    private Rectangle blueDispenser;
    private Image blueDispenserImg;
    private Rectangle yellowDispenser;
    private Image yellowDispenserImg;

    public Zone3CoreRoom() {

        super("Zona 3, Núcleo del reactor");
        background = new ImageIcon(getClass().getResource("/mapas_niveles/nivel3_fondo.png")).getImage();
        core = new Rectangle(550, 350, 180, 180);

        int[] xPoints = {570, 1225, 650, 60};
        int[] yPoints = {160, 390, 690, 450};
        this.floorArea = new Polygon(xPoints, yPoints, 4);
        redDispenser = new Rectangle(255, 290, 66, 88);
        blueDispenser = new Rectangle(965, 290, 66, 88);
        yellowDispenser = new Rectangle(600, 560, 66, 88);
        redDispenserImg = new ImageIcon(getClass().getResource("/frames/Nivel-3/dispensadores/dispensador_rojo_lleno.png")).getImage();
        blueDispenserImg = new ImageIcon(getClass().getResource("/frames/Nivel-3/dispensadores/dispensador_azul_lleno.png")).getImage();
        yellowDispenserImg = new ImageIcon(getClass().getResource("/frames/Nivel-3/dispensadores/dispensador_amarillo_lleno.png")).getImage();
        coreImg = new ImageIcon(getClass().getResource("/frames/Nivel-3/Nucleo/nucleo_vacio.png")).getImage();
    }

    @Override
    public void update(Agent agent) {

    }

    @Override
    public void draw(Graphics graphics, Agent agent) {
        graphics.drawImage(background, 0, 0, 1280, 720, null);
        graphics.drawImage(coreImg, core.x, core.y, core.width, core.height, null);
        graphics.drawImage(redDispenserImg, redDispenser.x, redDispenser.y, redDispenser.width, redDispenser.height, null);
        graphics.drawImage(blueDispenserImg, blueDispenser.x, blueDispenser.y, blueDispenser.width, blueDispenser.height, null);
        graphics.drawImage(yellowDispenserImg, yellowDispenser.x, yellowDispenser.y, yellowDispenser.width, yellowDispenser.height, null);


    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
