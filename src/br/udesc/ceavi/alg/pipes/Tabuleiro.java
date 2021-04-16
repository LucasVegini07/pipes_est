package br.udesc.ceavi.alg.pipes;

import java.awt.Point;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.table.AbstractTableModel;

/**
 *
 */
public class Tabuleiro extends AbstractTableModel {

    private final Celula[][] tabuleiro;

    private int largura;
    private int altura;
    private Point posicaoAgua;

    public Tabuleiro(int largura, int altura, Point posicaoAgua) {
        this.largura = largura;
        this.altura = altura;
        this.posicaoAgua = posicaoAgua;
        this.tabuleiro = new Celula[largura][];
        for (int i = 0; i < largura; i++) {
            this.tabuleiro[i] = new Celula[altura];
        }
    }

    public Point getPosicaoAgua() {
        return posicaoAgua;
    }

    public void setCelula(int x, int y, Celula celula) {
        this.tabuleiro[x][y] = celula;
        celula.setPosicao(new Point(x, y));
    }

    public Celula getCelula(int x, int y) {
        return this.tabuleiro[x][y];
    }

    public void from(String[] tabuleiro) throws CelulaDesconhecidaException, ConteudoInvalidoException {

        for (int y = 0; y < tabuleiro.length; y++) {
            String linha = tabuleiro[y];
            if (!linha.matches("([XTLI]\\d,){" + this.getColumnCount() + "}")) {
                throw new ConteudoInvalidoException(tabuleiro);
            }
            String[] colunas = linha.split(",");
            for (int x = 0; x < colunas.length; x++) {
                String coluna = colunas[x];
                this.setCelula(x, y, Celula.fromString(coluna));
            }
        }

    }

    public boolean ehMeta() {
        for (Celula[] colunas : this.tabuleiro) {
            for (Celula linhas : colunas) {
                if (!linhas.hasAgua()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String ret = "";
        for (int y = 0; y < this.getRowCount(); y++) {
            for (int x = 0; x < this.getColumnCount(); x++) {
                ret += this.getCelula(x, y) + ",";
            }
            ret += "\n";
        }
        return ret;
    }

    @Override
    public int getRowCount() {
        return this.altura;
    }

    @Override
    public int getColumnCount() {
        return this.largura;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex
    ) {
        return this.tabuleiro[columnIndex][rowIndex].getSprite();
    }

    public void verificaAgua() {

        for (Celula[] celulas : this.tabuleiro) {
            for (Celula celula : celulas) {
                celula.setAgua(false);
            }
        }

        Queue<Celula> celulas = new LinkedBlockingQueue<>();
        celulas.add(this.tabuleiro[(int) this.posicaoAgua.getX()][(int) this.posicaoAgua.getY()]);
        Celula celulaAtual = celulas.poll();
        while (celulaAtual != null) {
            celulaAtual.setAgua(true);
            
            for (Point direcao : celulaAtual.getDirecoesValidacao()) {
                int posicaoX = (int) (celulaAtual.getPosicao().getX() + direcao.getX());
                int posicaoY = (int) (celulaAtual.getPosicao().getY() + direcao.getY());
                
                if (posicaoX >= 0 && posicaoX < this.getColumnCount()
                        && posicaoY >= 0 && posicaoY < this.getRowCount()) {
                    Celula celulaAlvo = this.tabuleiro[posicaoX][posicaoY];
                    boolean conectada = false;
                    if (!celulaAlvo.hasAgua()) {
                        for (Point direcaoAlvo : celulaAlvo.getDirecoesValidacao()) {
                            if (direcaoAlvo.getX() == -1 * direcao.getX()
                                    && direcaoAlvo.getY() == -1 * direcao.getY()) {
                                conectada = true;
                            }
                        }
                        if (conectada) {
                            celulas.add(celulaAlvo);
                        }
                    }
                }
            }
            celulaAtual = celulas.poll();
        }

    }

    public void rotacionaCano(int x, int y) {
        if (x < 0 || y < 0 || this.getCelula(x, y).isCorrectPosition()) {

            return;
        }

        int orientacao = this.getCelula(x, y).getOrientacao().getOrientacaoNum() + 1;

        String position = "" + y + x;

        switch (position) {

            case "00":

                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 1) {
                        orientacao = 2;
                    }
                    if (orientacao == 4) {
                        orientacao = 2;
                    }

                }

                break;

            case "01":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 5) {
                        orientacao = 2;
                    }

                }

                if (this.getCelula(x, y).getTipoTexto().equals("L")) {

                    if (orientacao == 5) {
                        orientacao = 3;
                    }
                    if (orientacao == 2) {
                        orientacao = 3;
                    }

                }
                break;

            case "02":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 5) {
                        orientacao = 3;
                    }

                    if (orientacao == 2) {
                        orientacao = 3;
                    }

                }

                break;

            case "10":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 4) {
                        orientacao = 1;
                    }

                }

                if (this.getCelula(x, y).getTipoTexto().equals("L")) {

                    if (orientacao == 5) {
                        orientacao = 2;
                    }
                    if (orientacao == 4) {
                        orientacao = 2;
                    }

                }

                break;

            case "11":

                break;

            case "12":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 2) {
                        orientacao = 3;
                    }

                }

                if (this.getCelula(x, y).getTipoTexto().equals("L")) {

                    if (orientacao == 2) {
                        orientacao = 4;
                    }
                    if (orientacao == 3) {
                        orientacao = 4;
                    }

                }

                break;

            case "20":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 3) {
                        orientacao = 1;
                    }

                    if (orientacao == 4) {
                        orientacao = 1;
                    }

                }

                break;
            case "21":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 3) {
                        orientacao = 4;
                    }
                }

                if (this.getCelula(x, y).getTipoTexto().equals("L")) {

                    if (orientacao == 3) {
                        orientacao = 1;
                    }
                    if (orientacao == 4) {
                        orientacao = 1;
                    }

                }

                break;
            case "22":
                if (this.getCelula(x, y).getTipoTexto().equals("X")) {

                    if (orientacao == 2) {
                        orientacao = 4;
                    }

                    if (orientacao == 3) {
                        orientacao = 4;
                    }
                }

                break;

            default:

                break;
        }

        Celula celula = this.getCelula(x, y);
        if (orientacao > 4) {
            orientacao = 1;
        }

        celula.setOrientacao(Orientacao.fromNum(orientacao));

    }
}
