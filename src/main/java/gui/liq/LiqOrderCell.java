package gui.liq;

import utils.Formatter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LiqOrderCell extends AbstractCellEditor implements TableCellRenderer {

    private JPanel panel;
    private JLabel size;
    private JLabel instrument;
    private JLabel slip;
    private JLabel btcAmt;

    LiqOrderCell() {

        size = new JLabel();

        instrument = new JLabel();
        instrument.setForeground(Color.WHITE);
        instrument.setFont(new Font(null, Font.ITALIC, 12));

        slip = new JLabel();
        slip.setForeground(Color.BLACK);
        slip.setFont(new Font(null, Font.PLAIN, 11));

        btcAmt = new JLabel();
        btcAmt.setForeground(Color.DARK_GRAY);
        btcAmt.setFont(new Font(null, Font.ITALIC, 11));

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(size);
        panel.add(instrument);
    }


    private void updateData(LiqOrder order) {

        size.setText(Formatter.kFormat((double) Math.abs(order.getAmt()), 0) + (order.getSide() ? " short liq'd " : " long liq'd "));
        size.setIcon(getIcon(order));
        setInstrument(order);

        panel.setBackground(getColor(order.getSide(), order.getAmt()));

        int orderAmt = Math.abs(order.getAmt());

        //do this better
        if (orderAmt < 1000) {
            panel.setBorder(null);
            size.setForeground(Color.GRAY);
            size.setFont(new Font(null, Font.ITALIC, 12));

        } else if (orderAmt < 10000) {
            panel.setBorder(null);
            size.setForeground(Color.DARK_GRAY);
            size.setFont(new Font(null, Font.ITALIC, 13));

        } else if (orderAmt < 100000) {
            panel.setBorder(null);
            size.setForeground(Color.BLACK);
            size.setFont(new Font(null, Font.PLAIN, 15));

        } else if (orderAmt < 500000) {
            panel.setBorder(null);
            size.setForeground(Color.BLACK);
            size.setFont(new Font(null, Font.PLAIN, 17));

        } else if (orderAmt < 1000000) {
            panel.setBorder(null);
            size.setForeground(Color.WHITE);
            size.setFont(new Font(null, Font.BOLD, 17));

        } else { //over 1mil
            panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            size.setForeground(Color.YELLOW);
            size.setFont(new Font(null, Font.BOLD, 17));
        }

    }


    private void setSlip(LiqOrder order) {

        int slipInt = order.getSlip();

        if (Math.abs(slipInt) >= 1) {

            slip.setText(String.valueOf(Math.abs(slipInt)));

            if (slipInt > 0) {
                slip.setIcon(new ImageIcon(getClass().getResource("/uparrow.png")));
            } else {
                slip.setIcon(new ImageIcon(getClass().getResource("/downarrow.png")));
            }

        } else {
            slip.setText("");
            slip.setIcon(null);
        }
    }

    private void setInstrument(LiqOrder order) {

        String in = order.getInstrument();

        instrument.setText(in);
    }

    private Icon getIcon(LiqOrder order) {

        ImageIcon icon = null;

        switch (order.getExchange()) {
            case "bitmex":
                if (order.isActive()) {
                    icon = new ImageIcon(getClass().getResource("/alert.png"));
                } else {
                    icon = new ImageIcon(getClass().getResource("/bitmex22.png"));
                }

                break;
            case "bitfinex":
                icon = new ImageIcon(getClass().getResource("/bitfinex22.png"));
                break;
            case "okex":
                icon = new ImageIcon(getClass().getResource("/okex22.png"));
                break;
            case "binance":
                icon = new ImageIcon(getClass().getResource("/binance.png"));
                break;
            case "gdax":
                icon = new ImageIcon(getClass().getResource("/gdax25.png"));
                break;
        }

        return icon;
    }

    private static Color getColor(boolean side, int amt) {

        int intensity = Math.abs(amt) / 4200;

        if (intensity > 165) {
            intensity = 165;
        } else if (intensity < 1) {
            intensity = 1;
        }

        if (side) {
            return new Color(170 - intensity, 255 - intensity / 2, 170 - intensity);
        } else {
            return new Color(255 - intensity / 2, 170 - intensity, 170 - intensity);
        }

    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        LiqOrder order = (LiqOrder) value;
        updateData(order);
        return panel;
    }


}
