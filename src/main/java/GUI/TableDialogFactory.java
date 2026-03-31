package GUI;

import javax.swing.*;
import java.awt.*;

public class TableDialogFactory {
    public void create(JDialog owner, String title, Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        table.setEnabled(false);

        JDialog dialog = new JDialog(owner);
        dialog.getContentPane().add(new JScrollPane(table));

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setTitle(title);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}
