package br.udesc.ceavi.alg.pipes;

/**
 *
 */
public class CelulaDesconhecidaException extends Exception {

    public CelulaDesconhecidaException(char tipo) {
        super("Célula de tipo " + tipo + " desconhecida.");
    }

}
