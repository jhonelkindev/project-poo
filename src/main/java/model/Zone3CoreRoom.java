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
    private boolean carryingRod;
    private boolean redEmpty;
    private boolean blueEmpty;
    private boolean yellowEmpty;
    private int rodPlaced;
    private Decoration redDecoration;
    private Decoration blueDecoration;
    private Decoration yellowDecoration;
    private Decoration coreDecoration;

    private Image empyDispenserImg;
    private Image [] coreImages;


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
        Polygon coreBase = new Polygon(new int[]{30, 85, 152, 97}, new int[]{135, 170, 138, 103}, 4);
        Polygon dispenserBase = new Polygon(new int[]{4, 38, 61, 27}, new int[]{70, 86, 72, 56}, 4);
        coreDecoration = new Decoration(coreImg, core, coreBase);
        addDecoration(coreDecoration);
        redDecoration = new Decoration(redDispenserImg, redDispenser, dispenserBase);
        addDecoration(redDecoration);
        blueDecoration = new Decoration(blueDispenserImg, blueDispenser, dispenserBase);
        addDecoration(blueDecoration);
        yellowDecoration = new Decoration(yellowDispenserImg, yellowDispenser, dispenserBase);
        addDecoration(yellowDecoration);
        sortDecorationsByDepth();
    }

    @Override
    public void update(Agent agent) {

    }

    @Override
    public void draw(Graphics graphics, Agent agent) {
        graphics.drawImage(background, 0, 0, 1280, 720, null);
        drawDecorations(graphics);

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void drawScene(Graphics graphics, Agent agent, Runnable drawAgent) {
        graphics.drawImage(background, 0, 0, 1280, 720, null);
        drawDecorationsWithAgent(graphics, agent, drawAgent);
    }

    public boolean isYellowEmpty() {
        return yellowEmpty;
    }

    public boolean isBlueEmpty() {
        return blueEmpty;
    }

    public boolean isRedEmpty() {
        return redEmpty;
    }

    public boolean isCarryingRod() {
        return carryingRod;
    }
}
