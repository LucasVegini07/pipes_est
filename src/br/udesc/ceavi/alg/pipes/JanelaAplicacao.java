package br.udesc.ceavi.alg.pipes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 */
public class JanelaAplicacao extends JFrame {

    private JPanel painelJogo;
    private JPanel painelBotoes;
    private JFileChooser fileChooser;
    private TabelaTabuleiro tabuleiro;
    private JTextPane textoEstado;

    private final PIPES jogo;

    public JanelaAplicacao(String nome, PIPES jogo) {
        super(nome);
        this.jogo = jogo;
        this.montaAtributos();
        this.montaPaineis();
    }

    private void montaAtributos() {
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(new PIPESFileFilter());
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
    }

    private void montaPaineis() {
        this.montaPainelJogo();
        this.montaPainelBotoes();
    }

    private void montaPainelJogo() {
        JScrollPane pane = new JScrollPane();
        this.painelJogo = new JPanel();
        this.painelJogo.setBackground(Color.LIGHT_GRAY);
        this.tabuleiro = new TabelaTabuleiro(this.jogo, this.painelJogo);
        pane.setViewportView(this.tabuleiro);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pane.setBackground(new Color(0, 0, 0, 0));
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.setViewportBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pane.getViewport().setBackground(new Color(0, 0, 0, 0));
        this.painelJogo.add(pane);
        this.add(this.painelJogo, BorderLayout.CENTER);
        this.painelJogo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                tabuleiro.revalidate();
            }
        });
    }

    private void montaPainelBotoes() {
        JPanel painelInf = new JPanel();
        painelInf.setLayout(new BorderLayout());

        this.painelBotoes = new JPanel();

        JButton botaoCarregar = new JButton("Carregar");
        botaoCarregar.addActionListener((event) -> {
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                jogo.carregaArquivo(file);
            }
        });
        this.painelBotoes.add(botaoCarregar);

        JButton botaoLargura = new JButton("Largura");
        botaoLargura.addActionListener((event) -> {
            jogo.buscaLargura();
        });
        botaoLargura.setEnabled(false);
        this.painelBotoes.add(botaoLargura);

        JButton botaoProfundidade = new JButton("Profundidade");
        botaoProfundidade.addActionListener((event) -> {
            try {
                jogo.buscaProfundidade();
            } catch (Exception ex) {
                Logger.getLogger(JanelaAplicacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        botaoProfundidade.setEnabled(false);
        this.painelBotoes.add(botaoProfundidade);

        JFrame frameSobre = montaFrameSobre();
        JButton botaoSobre = new JButton("Sobre");
        botaoSobre.addActionListener((event) -> {
            frameSobre.setVisible(true);
        });
        this.painelBotoes.add(botaoSobre);

        painelInf.add(this.painelBotoes, BorderLayout.SOUTH);

        this.textoEstado = new JTextPane();
        StyledDocument doc = this.textoEstado.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        this.textoEstado.setEditable(false);
        this.textoEstado.setVisible(false);
        painelInf.add(this.textoEstado, BorderLayout.CENTER);

        this.add(painelInf, BorderLayout.SOUTH);
    }

    public void tabuleiroCarregado() {
        for (Component button : this.painelBotoes.getComponents()) {
            button.setEnabled(true);
        }
    }

    private JFrame montaFrameSobre() {
        JFrame frame = new JFrame("Sobre");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        JTextPane info = new JTextPane();
        info.setText(this.jogo.getTextoSobre());
        StyledDocument doc = info.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        info.setEditable(false);
        frame.add(info, BorderLayout.CENTER);
        return frame;
    }

    public void carregaTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro.setModel(tabuleiro);
        repintaTabuleiro();
    }

    public void repintaTabuleiro() {
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.tabuleiro.repaint();
        });
    }

    public void bloqueiaJanela(boolean visualizacao, String mensagem) {
        SwingUtilities.invokeLater(() -> {
            tabuleiro.setEnabled(false);
            for (Component button : painelBotoes.getComponents()) {
                button.setEnabled(false);
            }
            if (visualizacao) {
                this.painelBotoes.setVisible(false);
            }
            if (!mensagem.isEmpty()) {
                this.textoEstado.setVisible(true);
                textoEstado.setText(mensagem);
            }
        });
    }

    public void bloqueiaJanela() {
        this.bloqueiaJanela(false, "Aguarde...");
    }

    public void desbloqueiaJanela() {
        SwingUtilities.invokeLater(() -> {
            tabuleiro.setEnabled(true);
            for (Component button : painelBotoes.getComponents()) {
                button.setEnabled(true);
            }
        });
    }

}
