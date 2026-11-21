package model;

/**
 * Guarda el guion del juego: las escenas de la intro, las tarjetas
 * de cada zona, el final y el game over.
 *
 * @author Mariana Ocampo
 * @version 1.0
 */
public class StoryData {

    /**
     * Escenas de la intro, antes del nivel 1 (paneles 1 al 5)
     *
     * @return arreglo con las escenas de la intro
     */
    public static StoryScene[] getIntro() {
        return new StoryScene[]{
                new StoryScene("/images/intro_1.png", new String[]{
                        "Narrador: Planta nuclear Helix, reactor 7. Lider en energia nuclear, 0 incidentes desde su construccion."
                }),
                new StoryScene("/images/intro_2.png", new String[]{
                        "Doctor Uranio: Este sera el siguiente paso de la revolucion humana... y me llamaban loco."
                }),
                new StoryScene("/images/intro_3.png", new String[]{
                        "Narrador: Un experimento prohibido, mas alla de la comprension y la etica humana, desestabilizo el nucleo y provoco una explosion.",
                        "Narrador: Sin embargo, nadie en la ciudad se entero."
                }),
                new StoryScene("/images/intro_4.png", new String[]{
                        "Ejecutivo: Nadie puede enterarse de este pequeno inconveniente. Llama al Agente.",
                        "Asistente: Pero la radiacio-",
                        "Ejecutivo: Haga su trabajo."
                }),
                new StoryScene("/images/intro_5.png", new String[]{
                        "Agente, tenemos una fuga menor en la planta. Nada grave. Entra, repara y sales. Le pagaremos el triple."
                })
        };
    }

    /**
     * Tarjeta que aparece antes del nivel 1
     *
     * @return arreglo con la escena de la zona 1
     */
    public static StoryScene[] getZone1() {
        return new StoryScene[]{
                new StoryScene("/images/zona_1.png", new String[]{
                        "Las tuberias estan rotas, el vapor sube cada segundo. Repara la tuberia antes de que no puedas ver nada. Cuidado con el vapor, esta que arde."
                })
        };
    }

    /**
     * Tarjeta que aparece antes del nivel 2
     *
     * @return arreglo con la escena de la zona 2
     */
    public static StoryScene[] getZone2() {
        return new StoryScene[]{
                new StoryScene("/images/zona_2.png", new String[]{
                        "El generador se descompuso, recoge las tuercas y reconstruyelo. Cuidado, las ratas no son precisamente comunes."
                })
        };
    }

    /**
     * Escenas antes del nivel 3: el Doctor Uranio (panel 7) y la tarjeta de la zona 3
     *
     * @return arreglo con las escenas de la zona 3
     */
    public static StoryScene[] getZone3() {
        return new StoryScene[]{
                new StoryScene("/images/panel_7.png", new String[]{
                        "Doctor Uranio: En serio crees que les importas? Hace 5 anos yo era su mejor hombre y mirame ahora, me tratan peor que a la escoria."
                }),
                new StoryScene("/images/zona_3.png", new String[]{
                        "El nucleo esta expuesto. Coloca 3 barras para contenerlo antes de que la inestabilidad del uranio provoque una segunda explosion. Esta vez no sera tan pequena."
                })
        };
    }

    /**
     * Escenas del final, cuando el jugador gana (paneles 8 al 12)
     *
     * @return arreglo con las escenas del final
     */
    public static StoryScene[] getEnding() {
        return new StoryScene[]{
                new StoryScene("/images/final_8.png", new String[]{
                        "..."
                }),
                new StoryScene("/images/final_9.png", new String[]{
                        "Excelente trabajo, Agente. La corporacion se encargara de recompensarlo como corresponde.",
                        "Por ahora entreguenos todo lo que encontro en el reactor, hay que mandarlo al laboratorio para examenes. Simple burocracia."
                }),
                new StoryScene("/images/final_10.png", new String[]{
                        "Agente: Sobre el pago, queria saber si-"
                }),
                new StoryScene("/images/final_11.png", new String[]{
                        "*BANG*"
                }),
                new StoryScene("/images/final_12.png", new String[]{
                        "Noticiero: Accidente en el reactor 7 de la planta nuclear Helix, sin victimas mortales."
                })
        };
    }

    /**
     * Escena cuando el jugador pierde (se acaban las vidas o el tiempo)
     *
     * @return arreglo con la escena de game over
     */
    public static StoryScene[] getGameOver() {
        return new StoryScene[]{
                new StoryScene("/images/gameover.png", new String[]{
                        "ALERTA NACIONAL: Accidente nuclear, busque refugio. Esto no es un simulacro."
                })
        };
    }
}