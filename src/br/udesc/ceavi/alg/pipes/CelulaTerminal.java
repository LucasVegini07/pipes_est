package br.udesc.ceavi.alg.pipes;

import java.awt.Point;

/**
 *
 */
public class CelulaTerminal extends Celula {

    @Override
    public String getTipoTexto() {
        return "X";
    }

    @Override
    public Point[] getDirecoesValidacao() {
        switch (this.getOrientacao()) {
            case NORTE:
                return new Point[]{
                    new Point(0, -1)
                };
            case LESTE:
                return new Point[]{
                    new Point(1, 0)
                };
            case SUL:
                return new Point[]{
                    new Point(0, 1),};
            case OESTE:
                return new Point[]{
                    new Point(-1, 0)
                };
            default:
                return new Point[]{};
        }
    }

}
