package model;

import javax.swing.*;
import java.awt.*;

public class Zone1BoilerRoom extends Zone {

    private Rectangle rack;
    private Rectangle [] exhaustFans;
    private Rectangle [] boilers;
    private Rectangle [] tubes;


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


        Image verticalTankImg = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_vertical.png")).getImage();
        Image auxiliaryTankImg = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_auxiliar.png")).getImage();
        Image horizontalTankImg =  new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tanque_horizontal.png")).getImage();
        Image lTubeImg = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_codo.png")).getImage();
        Image straightLineTube = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_recta.png")).getImage();
        Image unionTube = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_union_t.png")).getImage();
        Image valveTube = new ImageIcon(getClass().getResource("/frames/Nivel-1/Diseños/Decoracion/tuberia_valvula.png")).getImage();

        // Grupo izquierdo. La fila gira hacia la estanteria y forma el primer cruce.
        addDecoration(new Decoration(horizontalTankImg,new Rectangle(374, 194, 128, 96)));
        addDecoration(new Decoration(horizontalTankImg,new Rectangle(458, 224, 128, 96)));
        addDecoration(new Decoration(verticalTankImg,new Rectangle(484, 269, 72, 96)));
        addDecoration(new Decoration(auxiliaryTankImg,new Rectangle(447, 318, 48, 72)));

        // Grupo del fondo. Separa las dos calderas y deja una salida hacia el centro.
        addDecoration(new Decoration(verticalTankImg,new Rectangle(860, 192, 72, 96)));
        addDecoration(new Decoration(verticalTankImg,new Rectangle(813, 219, 72, 96)));
        addDecoration(new Decoration(verticalTankImg,new Rectangle(766, 246, 72, 96)));
        addDecoration(new Decoration(horizontalTankImg,new Rectangle(797, 265, 128, 96)));

        // Grupo inferior. Divide el camino hacia la caldera del frente y el extractor derecho.
        addDecoration(new Decoration(horizontalTankImg,new Rectangle(595, 340, 128, 96)));
        addDecoration(new Decoration(horizontalTankImg,new Rectangle(679, 370, 128, 96)));
        addDecoration(new Decoration(verticalTankImg,new Rectangle(777, 387, 72, 96)));
        addDecoration(new Decoration(auxiliaryTankImg,new Rectangle(747, 438, 48, 72)));

        // Tuberia baja entre el extractor izquierdo y la estanteria.
        addDecoration(new Decoration(straightLineTube,new Rectangle(255, 324, 96, 96)));
        addDecoration(new Decoration(valveTube,new Rectangle(307, 343, 96, 96)));

        // Tubo recto cerca del extractor izquierdo.
        addDecoration(new Decoration(straightLineTube, new Rectangle(252, 320, 96, 96)));

        // Tubo en L en la entrada al pasillo central, arriba de la estanteria.
        addDecoration(new Decoration(lTubeImg, new Rectangle(480, 430, 96, 96)));

        // Tubo con valvula al costado derecho de la caldera derecha.
        addDecoration(new Decoration(valveTube, new Rectangle(1110, 325, 96, 96)));

        // Union en T en el pasillo del frente, a la derecha de la caldera inferior.
        addDecoration(new Decoration(unionTube, new Rectangle(725, 532, 96, 96)));

    }



    @Override
    public void update(Agent agent) {

    }


    @Override
    public void draw(Graphics graphics, Agent agent) {
        graphics.drawImage(backgroundImg, 0, 0, 1280, 720, null);
        drawDecorations(graphics);

        graphics.drawImage(rackImg, rack.x,rack.y, rack.width, rack.height, null);

        for(Rectangle exhaustFan : exhaustFans) {
            graphics.drawImage(exahust_fanImg,exhaustFan.x,exhaustFan.y,exhaustFan.width,exhaustFan.height, null);
        }

        for(Rectangle boiler : boilers) {
            graphics.drawImage(boilerImg,boiler.x,boiler.y,boiler.width,boiler.height, null);
        }


        for(int i = 0; i < tubes.length; i++) {
            Rectangle tube = tubes[i];
            if (i < 2) {
                // Pared izquierda: se dibuja en espejo para que siga la inclinación de la pared
                graphics.drawImage(tubeImg, tube.x + tube.width, tube.y, -tube.width, tube.height, null);
            } else {
                graphics.drawImage(tubeImg, tube.x, tube.y, tube.width, tube.height, null);
            }
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