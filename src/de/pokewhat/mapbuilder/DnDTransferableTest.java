package de.pokewhat.mapbuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;

/**
 * Created by dwenders on 03.05.2016.
 */
public class DnDTransferableTest {


    private static final int WINDOW_SIZE_X = 750, WINDOW_SIZE_Y = 750, ICON_SIZE = 20, MAP_SIZE_X = 50, MAP_SIZE_Y = 50;

    public static void main(String[] args) {
        new DnDTransferableTest();
    }

    public DnDTransferableTest() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("PokeWhat - Map Editor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private JList list;

        public TestPane() {

            list = new JList();
            list.setDragEnabled(true);
            list.setTransferHandler(new ListTransferHandler());

            DefaultListModel model = new DefaultListModel();
            for (int index = 0; index < 20; index++) {

                model.addElement(new ListItem("" + index, "C:\\Users\\dwenders\\Desktop\\Anya.jpg")); //Hier muss noch richtig eingelesen werden

            }
            list.setModel(model);

            setLayout(new BorderLayout(0, 0));

            JPanel left = new JPanel();
            add(left, BorderLayout.WEST);
            left.setLayout(new BorderLayout(0, 0));
            left.add(new JScrollPane(list), BorderLayout.CENTER);


            JPanel center = new JPanel();
            add(new JScrollPane(center), BorderLayout.CENTER);
            center.setLayout(new GridLayout(MAP_SIZE_X, MAP_SIZE_Y));

            PlaceHolderLabel label;

            for (int i = 0; i < 2500; i++) {
                label = new PlaceHolderLabel("", ICON_SIZE);
                center.add(label);
                label.setTransferHandler(new ListTransferHandler());
                label.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
            }


            /*setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 1;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.BOTH;
            add(new JScrollPane(list), gbc);

            label = new JLabel("Drag on me...");
            gbc.gridx++;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.NONE;
            add(label, gbc);

            label.setTransferHandler(new ListTransferHandler());*/

        }
    }

    public class ListTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport support) {
            return (support.getComponent() instanceof JLabel) && support.isDataFlavorSupported(ListItemTransferable.LIST_ITEM_DATA_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(ListItemTransferable.LIST_ITEM_DATA_FLAVOR);
                    if (value instanceof ListItem) {
                        Component component = support.getComponent();
                        if (component instanceof JLabel) {
                            //((JLabel) component).setText(((ListItem) value).getText());
                            ((PlaceHolderLabel) component).setIcon(((ListItem) value).getImage());
                            accept = true;
                        }
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = null;
            if (c instanceof JList) {
                JList list = (JList) c;
                Object value = list.getSelectedValue();
                if (value instanceof ListItem) {
                    ListItem li = (ListItem) value;
                    t = new ListItemTransferable(li);
                }
            }
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            System.out.println("ExportDone");
            // Here you need to decide how to handle the completion of the transfer,
            // should you remove the item from the list or not...
        }
    }

    public static class ListItemTransferable implements Transferable {

        public static final DataFlavor LIST_ITEM_DATA_FLAVOR = new DataFlavor(ListItem.class, "java/ListItem");
        private ListItem listItem;

        public ListItemTransferable(ListItem listItem) {
            this.listItem = listItem;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{LIST_ITEM_DATA_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(LIST_ITEM_DATA_FLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {

            return listItem;

        }
    }

    public static class ListItem {

        private String text;
        private ImageIcon image;

        public ListItem(String text) {
            this.text = text;
        }

        public ListItem(String text, String path) {
            this.text = text;

            ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            this.image = new ImageIcon(newimg);
        }

        public String getText() {
            return text;
        }

        public ImageIcon getImage() {
            return this.image;
        }

        @Override
        public String toString() {
            return getText();
        }
    }
}