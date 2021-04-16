package br.udesc.ceavi.alg.pipes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 */
public class TabelaTabuleiro extends JTable {

    private PIPES jogo;
    private JPanel parentPanel;

    public TabelaTabuleiro(PIPES jogo, JPanel parentPanel) {
        this.parentPanel = parentPanel;
        this.jogo = jogo;
        this.initializeProperties();
    }

    private class BoardTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                BufferedImage img = (BufferedImage) value;
                AffineTransform transform = AffineTransform.getScaleInstance((float) table.getColumnModel().getColumn(column).getWidth() / img.getWidth(),
                        (float) table.getRowHeight() / img.getHeight());
                AffineTransformOp operator = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                this.setIcon(new ImageIcon(operator.filter(img, null)));
            }
            return this;
        }
    }

    private void initializeProperties() {
        this.setDefaultRenderer(Object.class, new BoardTableRenderer());
        this.setBackground(new Color(0, 0, 0, 0));
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(true);
        this.setDragEnabled(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setTableHeader(null);
        this.setFillsViewportHeight(true);
        this.setOpaque(false);
        this.setShowGrid(false);
        this.setRowMargin(0);
        this.getColumnModel().setColumnMargin(-1);

        this.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (getSelectedColumn() > -1 && getSelectedRow() > -1) {
                jogo.rotacionaCano(getSelectedColumn(), getSelectedRow(), true);
                this.getSelectionModel().clearSelection();
            }
        });
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = parentPanel.getSize();
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            return new Dimension(0, 0);
        }
        size.height -= 20;
        size.width -= 20;
        float scaleX = (float) size.getWidth();
        float scaleY = (float) size.getHeight();
        if (scaleX > scaleY) {
            int width = (int) (scaleY / scaleX * size.getWidth());
            size = new Dimension(width, (int) size.getHeight());
        } else {
            int height = (int) (scaleX / scaleY * size.getHeight());
            size = new Dimension((int) size.getWidth(), height);
        }
        if (this.getModel().getRowCount() > 0) {
            this.setRowHeight((int) size.getHeight() / this.getModel().getRowCount());
        }
        return size;
    }

}
