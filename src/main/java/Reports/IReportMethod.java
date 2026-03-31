package Reports;

import Entities.Curse;
import Entities.Sorcerer;
import Entities.Technique;

import java.util.List;

public interface IReportMethod {
    void showCurseDialog(Curse curse);

    void showSorcerersDialog(List<Sorcerer> sorcerers);

    void showTechniquesDialog(List<Technique> techniques);
}
