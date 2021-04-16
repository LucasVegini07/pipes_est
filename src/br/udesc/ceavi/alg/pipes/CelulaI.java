package br.udesc.ceavi.alg.pipes;

import java.awt.Point;

/**
 *
 */
public class CelulaI extends Celula {

    @Override
    public String getTipoTexto() {
        return "I";
    }

    @Override
    public Point[] getDirecoesValidacao() {
        switch(this.getOrientacao()){
            case NORTE:
            case SUL:
                return new Point[]{
                    new Point(0, -1),
                    new Point(0, 1)
                };
            case LESTE:
            case OESTE:
                return new Point[]{
                    new Point(-1, 0),
                    new Point(1, 0)
                };
            default:
                return new Point[]{};
        }
    }
    
}
