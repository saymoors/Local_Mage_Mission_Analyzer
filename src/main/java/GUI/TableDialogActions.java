package GUI;

import Entities.Curse;
import Entities.Sorcerer;
import Entities.Technique;
import Reports.IReportActions;

import java.util.List;

public class TableDialogActions implements IReportActions {
    private final TableDialogFactory tableDialogFactory;
    private final SideMenu owner;

    public TableDialogActions(SideMenu owner) {
        this.owner = owner;
        this.tableDialogFactory = new TableDialogFactory();
    }

    @Override
    public void showCurseDialog(Curse curse) {
        Object[][] data = new Object[1][2];
        String[] columns = {"name", "threatlevel"};
        data[0][0] = curse.getName();
        data[0][1] = curse.getThreatLevel();
        tableDialogFactory.create(owner, "Проклятие", data, columns);
    }

    @Override
    public void showSorcerersDialog(List<Sorcerer> sorcerers) {
        Object[][] data = new Object[sorcerers.size()][2];
        String[] columns = {"name", "rank"};
        for (int i = 0; i < sorcerers.size(); i++) {
            data[i][0] = sorcerers.get(i).getName();
            data[i][1] = sorcerers.get(i).getRank();
        }
        tableDialogFactory.create(owner, "Список магов", data, columns);
    }

    @Override
    public void showTechniquesDialog(List<Technique> techniques) {
        Object[][] data;
        String[] columns = {"name", "type", "owner", "damage"};
        data = new Object[techniques.size()][4];
        for (int i = 0; i < techniques.size(); i++) {
            data[i][0] = techniques.get(i).getName();
            data[i][1] = techniques.get(i).getType();
            data[i][2] = techniques.get(i).getOwner();
            data[i][3] = techniques.get(i).getDamage();
        }
        tableDialogFactory.create(owner, "Список техник", data, columns);
    }
}
