package GUI;

import Entities.Curse;
import Entities.Sorcerer;
import Entities.Technique;
import Reports.IReportActions;

import javax.swing.*;
import java.util.List;

public class TableReportActions implements IReportActions {
    private final TableReportDialog tableReportDialog;

    public TableReportActions(JDialog owner) {
        this.tableReportDialog = new TableReportDialog(owner);
    }

    @Override
    public void showCurseDialog(Curse curse) {
        Object[][] data = {
                {curse.getName(), curse.getThreatLevel()}
        };
        String[] columns = {"name", "threatlevel"};
        tableReportDialog.show("Проклятие", data, columns);
    }

    @Override
    public void showSorcerersDialog(List<Sorcerer> sorcerers) {
        Object[][] data = new Object[sorcerers.size()][2];
        String[] columns = {"name", "rank"};

        for (int i = 0; i < sorcerers.size(); i++) {
            data[i][0] = sorcerers.get(i).getName();
            data[i][1] = sorcerers.get(i).getRank();
        }

        tableReportDialog.show("Список магов", data, columns);
    }

    @Override
    public void showTechniquesDialog(List<Technique> techniques) {
        Object[][] data = new Object[techniques.size()][4];
        String[] columns = {"name", "type", "owner", "damage"};

        for (int i = 0; i < techniques.size(); i++) {
            data[i][0] = techniques.get(i).getName();
            data[i][1] = techniques.get(i).getType();
            data[i][2] = techniques.get(i).getOwner();
            data[i][3] = techniques.get(i).getDamage();
        }

        tableReportDialog.show("Список техник", data, columns);
    }
}
