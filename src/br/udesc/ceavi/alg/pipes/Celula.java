package br.udesc.ceavi.alg.pipes;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 */
public abstract class Celula {

    private Orientacao orientacao;
    private boolean agua;
    private Point posicao;
    private boolean correctPosition;

    private static final Map<String, BufferedImage> imagens = new HashMap<>();

    public Orientacao getOrientacao() {
        return orientacao;
    }

    public boolean isCorrectPosition() {
        return correctPosition;
    }

    public void setCorrectPosition(boolean correctPosition) {
        this.correctPosition = correctPosition;
    }

    public void setOrientacao(Orientacao orientacao) {
        this.orientacao = orientacao;
    }

    public boolean hasAgua() {
        return agua;
    }

    public void setAgua(boolean agua) {
        this.agua = agua;
    }

    public Point getPosicao() {
        return posicao;
    }

    public void setPosicao(Point posicao) {
        this.posicao = posicao;
    }

    public static Celula fromString(String texto) throws CelulaDesconhecidaException {
        Celula celula;
        switch (texto.charAt(0)) {
            case 'X':
                celula = new CelulaTerminal();
                break;
            case 'T':
                celula = new CelulaT();
                break;
            case 'L':
                celula = new CelulaL();
                break;
            case 'I':
                celula = new CelulaI();
                break;
            default:
                throw new CelulaDesconhecidaException(texto.charAt(0));
        }
        celula.setOrientacao(Orientacao.fromNum(Integer.parseInt(String.valueOf(texto.charAt(1)))));
        return celula;
    }

    public abstract String getTipoTexto();

    @Override
    public String toString() {
        return this.getTipoTexto() + this.getOrientacao();
    }

    public BufferedImage getSprite() {
        return getImagem(this.toString() + (this.hasAgua() ? "W" : ""));
    }

    private static BufferedImage getImagem(String imagem) {
        if (!imagens.containsKey(imagem)) {
            try {
                imagens.put(imagem, ImageIO.read(new File("img/PIPE" + imagem + ".png")));
            } catch (IOException ex) {
                imagens.put(imagem, new BufferedImage(32, 32, BufferedImage.OPAQUE));
            }
        }
        return imagens.get(imagem);
    }

    public abstract Point[] getDirecoesValidacao();
}
