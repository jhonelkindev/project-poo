package model;

import javax.swing.*;
import java.awt.*;

public class Zone1BoilerRoom extends Zone {

    private Rectangle rack;
    private Rectangle [] exhaustFans;
    private Rectangle [] boilers;
    private Rectangle [] tubes;

    private Rectangle decoration;

    private Image backgroundImg;
    private Image rackImg;
    private Image exahust_fanImg;
    private Image boilerImg;
    private Image tubeImg;





    public Zone1BoilerRoom() {
        super("Zona 1: Sala de Calderas");
        int[] xPoints = {565, 1238, 715, 36};
        int[] yPoints = {128, 369, 675, 433};
        this.floorArea = new Polygon(xPoints, yPoints, 4);

        this.rack = new Rectangle(300, 400, 130, 130);

        this.boilers = new Rectangle[] {
                new Rectangle(566, 149, 128, 128),  // Caldera del fondo
                new Rectangle(951, 290, 128, 128),  // Caldera de la derecha
                new Rectangle(566, 466, 128, 128)   // Caldera del frente
        };

        this.tubes = new Rectangle[] {
                new Rectangle(270, 185, 96, 96),    // Pared izquierda, cerca del extractor
                new Rectangle(425, 96, 96, 96),     // Pared izquierda, cerca de la esquina
                new Rectangle(775, 135, 96, 96),    // Pared superior derecha, primer tubo
                new Rectangle(1060, 237, 96, 96)    // Pared superior derecha, segundo tubo
        };
        this.exhaustFans = new Rectangle[]{
                new Rectangle(160, 290, 96, 96),
                new Rectangle(880, 420, 96, 96)

        };

        this.backgroundImg = new ImageIcon(getClass().getResource("/mapas_niveles/nivel1_fondo.png")).getImage();
        this.rackImg = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Estanteria/estanteria_repuestos.png")).getImage();
        this.exahust_fanImg = new ImageIcon(getClass().getResource("/Sprites/Nivel-1/Extractor/extractor_activo.gif")).getImage();
        this.boilerImg = new ImageIcon(getClass().getResource("/Sprites/Nivel-1/Maquina caldera/caldera_apagada.gif")).getImage();
        this.tubeImg = new ImageIcon(getClass().getResource("/Sprites/Nivel-1/Tubos/tuberia_dañada.gif")).getImage();

    }


    @Override
    public void update(Agent agent) {

    }


    @Override
    public void draw(Graphics graphics, Agent agent) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);

        graphics.drawImage(rackImg, rack.x,rack.y, rack.width, rack.height, null);

        for(Rectangle exhaustFan : exhaustFans) {
            graphics.drawImage(exahust_fanImg,exhaustFan.x,exhaustFan.y,exhaustFan.width,exhaustFan.height, null);
        }

        for(Rectangle boiler : boilers) {
            graphics.drawImage(boilerImg,boiler.x,boiler.y,boiler.width,boiler.height, null);
        }

        for(Rectangle tube : tubes) {
            graphics.drawImage(tubeImg,tube.x,tube.y,tube.width,tube.height, null);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        graphics.drawString("ZONA 1 - N para pasar a Zona 2", 20, 40);
    }


    @Override
    public boolean isCompleted() {
        return false;
    }
}