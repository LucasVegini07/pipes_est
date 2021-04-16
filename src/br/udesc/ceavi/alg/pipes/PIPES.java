package br.udesc.ceavi.alg.pipes;

import br.udesc.ceavi.alg.busca.BuscaLargura;
import br.udesc.ceavi.alg.busca.BuscaProfundidade;
import br.udesc.ceavi.alg.busca.Estado;
import br.udesc.ceavi.alg.busca.Nodo;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PIPES implements Estado {

    public static final String NOME_APLICACAO = "PIPES";
    public static final boolean DEBUG = true;

    private JanelaAplicacao janelaPrincipal;
    private Tabuleiro tabuleiro;
    final String op; // operacao que gerou o estado

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        (new PIPES()).inicia();
    }

    public PIPES(Tabuleiro tabuleiro, String op) {

        this.tabuleiro = tabuleiro;
        this.op = op;

    }

    public PIPES() {
        this.op = "inicio";
    }

    public void inicia() {
        this.janelaPrincipal = new JanelaAplicacao(NOME_APLICACAO, this);
        this.janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.janelaPrincipal.setVisible(true);
    }

    public JanelaAplicacao getJanelaPrincipal() {
        return janelaPrincipal;
    }

    public void carregaArquivo(File file) {
        (new Thread(new LeitorArquivoPIPES(file, this))).start();
    }

    private void carregaTabuleiro(Tabuleiro tabuleiro, boolean mensagem) {
        this.tabuleiro = tabuleiro;
        this.janelaPrincipal.carregaTabuleiro(tabuleiro);
        if (PIPES.DEBUG) {
            System.out.println(tabuleiro);
        }
        this.janelaPrincipal.tabuleiroCarregado();
        this.tabuleiro.verificaAgua();
        this.valida(false);
        if (mensagem) {
            this.notificaInfo("Tabuleiro carregado com sucesso!");
        }
    }

    public void carregaTabuleiro(Tabuleiro tabuleiro) {
        this.carregaTabuleiro(tabuleiro, true);
    }

    public void notificaInfo(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void notificaErro(String mensagem, Exception ex) {
        if (PIPES.DEBUG) {
            ex.printStackTrace(System.err);
        }
        notificaErro(mensagem);
    }

    public void notificaErro(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public String getTextoSobre() {
        return "Desenvolvido por:\nAfonso Ueslei Boing\nLucas Ramthum Vegini";
    }

    public void rotacionaCano(int selectedColumn, int selectedRow, boolean valida) {

        this.tabuleiro.rotacionaCano(selectedColumn, selectedRow);
        this.tabuleiro.verificaAgua();
        if (valida) {
            this.valida(false);
        }
    }

    public void valida() {
        this.valida(true);
    }

    public void valida(boolean exibeMensagem) {

        if (this.tabuleiro.ehMeta()) {
            this.notificaInfo("Tabuleiro resolvido!");
        }
        this.janelaPrincipal.repintaTabuleiro();

    }

    public void buscaLargura() {

        this.janelaPrincipal.bloqueiaJanela();
        System.out.println("Inicia busca");
        Nodo n = new BuscaLargura<PIPES>().busca(this);
        if (n == null) {
            System.out.println("sem solucao!");
        } else {
            this.tabuleiroResolvido(this.tabuleiro, n.getProfundidade());
        }
    }

    public boolean validaTabuleiro() {
        return true;
    }

    public void buscaProfundidade() throws Exception {
        this.janelaPrincipal.bloqueiaJanela();

        System.out.println("Inicia busca");

        Nodo n = new BuscaProfundidade<PIPES>().busca(this);
        if (n == null) {
        } else {
            this.tabuleiroResolvido(this.tabuleiro, n.getProfundidade());
        }
    }

    public String toString() {
        return "\n" + " - " + op;
    }

    public void tabuleiroResolvido(Tabuleiro tabuleiro, int profundidade) {
        PIPES jogoVis = new PIPES();
        jogoVis.inicia();
        JanelaAplicacao janela = jogoVis.getJanelaPrincipal();
        janela.setTitle(NOME_APLICACAO + " - Resultado");
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.bloqueiaJanela(true, "Puzzle resolvido! Profundidade encontrada: " + profundidade);
        jogoVis.carregaTabuleiro(tabuleiro, false);
        this.janelaPrincipal.desbloqueiaJanela();
    }

    @Override
    public String getDescricao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean ehMeta() {
        return this.tabuleiro.ehMeta();
    }

    @Override
    public int custo() {
        return 1;
    }

//<E extends Estado> List<E>
    @Override
    public List<Estado> sucessores() {

        List<Estado> suc = new LinkedList<Estado>(); // a lista de sucessores

        String op = "";

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {

                String position = "" + x + y;

                switch (position) {

                    case "00":

                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("L") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.SUL);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    case "01":
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("T") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.SUL);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("I") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.LESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        break;
                    case "02":
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("L") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.OESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    case "10":
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("I") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.NORTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("T") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.LESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    case "11":

                        break;
                    case "12":

                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("I") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.NORTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("T") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.OESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    case "20":

                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("L") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.LESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    case "21":

                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("T") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.NORTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("I") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.LESTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }

                        break;
                    default:
                        if (tabuleiro.getCelula(y, x).getTipoTexto().equals("L") && !tabuleiro.getCelula(y, x).isCorrectPosition()) {
                            tabuleiro.getCelula(y, x).setOrientacao(Orientacao.NORTE);
                            tabuleiro.getCelula(y, x).setCorrectPosition(true);
                            op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                            System.out.println(op);
                            suc.add(new PIPES(tabuleiro, op));
                        }
                        break;
                }
            }

        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (tabuleiro.getCelula(y, x).isCorrectPosition()) {
                } else {
                    for (int i = 0; i < 4; i++) {

                        this.rotacionaCano(y, x, false);
                        if (tabuleiro.getCelula(y, x).hasAgua()) {
                            break;
                        }
                    }
                    op = "Celula: " + tabuleiro.getCelula(y, x).getTipoTexto() + " | Posição: " + x + y + " | Orientação: " + tabuleiro.getCelula(y, x).getOrientacao();
                    System.out.println(op);
                    suc.add(new PIPES(tabuleiro, op));

                }
            }
        }
        return suc;
    }
}
