package br.udesc.ceavi.alg.pipes;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LeitorArquivoPIPES implements Runnable {

    private final File file;
    private final PIPES jogo;

    public LeitorArquivoPIPES(File file, PIPES jogo) {
        this.file = file;
        this.jogo = jogo;
    }

    @Override
    public void run() {
        try {
            List<String> linhas = Files.readAllLines(Paths.get(this.file.getPath()));
            String linhaInicial = linhas.get(0);
            linhas.remove(0);
            String tamanhos = linhas.get(0);
            linhas.remove(0);
            int largura = Integer.parseInt(tamanhos.substring(0, 1));
            int altura = Integer.parseInt(tamanhos.substring(2, 3));
            if (linhas.size() != altura) {
                throw new ConteudoInvalidoException(linhas.toArray(new String[]{}));
            }
            Tabuleiro tabuleiro = new Tabuleiro(largura, altura, new Point(Integer.parseInt(linhaInicial.substring(0, 1)),
                    Integer.parseInt(linhaInicial.substring(2, 3))));
            tabuleiro.from(linhas.toArray(new String[]{}));
            this.jogo.carregaTabuleiro(tabuleiro);
        } catch (IOException | ConteudoInvalidoException | CelulaDesconhecidaException ex) {
            this.jogo.notificaErro("Não foi possível ler o arquivo.", ex);
        }
    }

}
