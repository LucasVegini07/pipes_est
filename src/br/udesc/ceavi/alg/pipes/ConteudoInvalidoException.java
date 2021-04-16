package br.udesc.ceavi.alg.pipes;

import java.util.Arrays;

/**
 *
 */
public class ConteudoInvalidoException extends Exception {
    
    private String[] conteudo;
    
    public ConteudoInvalidoException(String[] conteudo){
        super("Conteúdo do arquivo inválido...");
        this.conteudo = conteudo;
    }

    @Override
    public String getMessage() {
        if(PIPES.DEBUG){
            return super.getMessage() + this.getConteudoString();
        }
        return super.getMessage();
    }

    private String getConteudoString() {
        return Arrays.stream(this.conteudo).reduce("", (t, u) -> {
            return t + "\n" + u;
        });
    }
    
}
