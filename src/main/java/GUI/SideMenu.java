package GUI;

import Entities.Curse;
import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SideMenu extends JDialog {

    public SideMenu(Mission mission) {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        panel.add(new JLabel("date:"));
        panel.add(new JLabel(mission.getDate()));

        panel.add(new JLabel("location:"));
        panel.add(new JLabel(mission.getLocation()));

        panel.add(new JLabel("outcome:"));
        panel.add(new JLabel(mission.getOutcome()));

        panel.add(new JLabel("damageCost:"));
        panel.add(new JLabel(String.valueOf(mission.getDamageCost())));

        JButton curserButton = new JButton("показать");
        curserButton.addActionListener(_ -> showCurseDialog(mission.getCurse()));
        panel.add(new JLabel("curse:"));
        panel.add(curserButton);

        JButton sorcerersButton = new JButton("показать");
        sorcerersButton.addActionListener(_ -> showSorcerersDialog(mission.getSorcerers()));
        panel.add(new JLabel("sorcerers:"));
        panel.add(sorcerersButton);

        JButton techniquesButton = new JButton("показать");
        techniquesButton.addActionListener(_ -> showTechniquesDialog(mission.getTechniques()));
        panel.add(new JLabel("techniques:"));
        panel.add(techniquesButton);

        if (mission.getComment() != null) {
            panel.add(new JLabel("comment:"));
            panel.add(new JLabel(mission.getComment()));
        }

        getContentPane().add(panel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showCurseDialog(Curse curse) {
        Object[][] data = new Object[1][2];
        String[] columns = {"name", "threatlevel"};
        data[0][0] = curse.getName();
        data[0][1] = curse.getThreatLevel();
        showTableDialog("Проклятие", data, columns);
    }

    private void showSorcerersDialog(List<Sorcerer> sorcerers) {
        Object[][] data = new Object[sorcerers.size()][2];
        String[] columns = {"name", "rank"};
        for (int i = 0; i < sorcerers.size(); i++) {
            data[i][0] = sorcerers.get(i).getName();
            data[i][1] = sorcerers.get(i).getRank();
        }
        showTableDialog("Список магов", data, columns);
    }

    private void showTechniquesDialog(List<Technique> techniques) {
        Object[][] data;
        String[] columns = {"name", "type", "owner", "damage"};
        data = new Object[techniques.size()][4];
        for (int i = 0; i < techniques.size(); i++) {
            data[i][0] = techniques.get(i).getName();
            data[i][1] = techniques.get(i).getType();
            data[i][2] = techniques.get(i).getOwner();
            data[i][3] = techniques.get(i).getDamage();
        }
        showTableDialog("Список техник", data, columns);
    }

    private void showTableDialog(String title, Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        table.setEnabled(false);

        JDialog dialog = new JDialog(this);
        dialog.getContentPane().add(new JScrollPane(table));

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setTitle(title);
        dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
