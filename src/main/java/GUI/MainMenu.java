package GUI;

import Entities.Mission;
import Filtering.FilterFactory;
import Filtering.IFilter;
import Filtering.Rules.DateFilter;
import Filtering.Rules.OutcomeFilter;
import Filtering.Rules.ThreatLevelFilter;
import Logging.LogPublisher;
import Logging.Rules.ConsoleLogger;
import Parsers.IParser;
import Parsers.ParserFactory;
import Reports.IReportFormat;
import Reports.ReportFormatFactory;
import Validation.IValidator;
import Validation.ValidatorFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MainMenu extends JFrame {
    private final ParserFactory parserFactory;
    private final ReportFormatFactory reportFormatFactory;
    private final FilterFactory filterFactory;
    private final ValidatorFactory validatorFactory;
    private final LogPublisher logPublisher;

    public MainMenu() {
        parserFactory = new ParserFactory();
        reportFormatFactory = new ReportFormatFactory();
        filterFactory = new FilterFactory();
        validatorFactory = new ValidatorFactory();
        logPublisher = new LogPublisher();

        registerLoggers();

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите миссию:");
        JButton button = new JButton("Открыть магический поисковик");
        JCheckBox checkBox = new JCheckBox("Включить фильтр");

        ButtonGroup buttonGroup = new ButtonGroup();
        JFileChooser chooser = getJFileChooser();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setPreferredSize(new Dimension(240, 40));
        button.setMaximumSize(new Dimension(240, 40));

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addActionListener(_ -> {
            try {
                int choice = chooser.showOpenDialog(this);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    logPublisher.publish("GUI", "Выбран файл: " + file.getAbsolutePath());

                    Mission mission = parseSelectedFile(file, checkBox.isSelected());
                    IReportFormat format = returnSelectedFormat(buttonGroup);

                    logPublisher.publish("REPORT", "Выбран формат отчета: " + buttonGroup.getSelection().getActionCommand());
                    new SideMenu(mission, format);
                    logPublisher.publish("REPORT", "Отчет успешно открыт");
                } else {
                    throw new Exception("Вы не выбрали миссию!");
                }
            } catch (Exception exception) {
                logPublisher.publish("ERROR", exception.getMessage());
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Предупреждение",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
        panel.add(checkBox);

        for (String reportFormat : reportFormatFactory.getReportFormats().keySet()) {
            JRadioButton radioButton = new JRadioButton(reportFormat);
            radioButton.setActionCommand(reportFormat);
            radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (reportFormat.equals(reportFormatFactory.getDefaultReportType())) {
                radioButton.setSelected(true);
            }

            buttonGroup.add(radioButton);
            panel.add(Box.createVerticalStrut(10));
            panel.add(radioButton);
        }

        getContentPane().add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private Mission parseSelectedFile(File file, boolean isFilterOn) throws Exception {
        String extension = getExtension(file);
        IParser parser = parserFactory.createParser(extension);
        logPublisher.publish("PARSER", "Выбран парсер для расширения: " + extension);

        Mission mission = parser.parse(file.getAbsolutePath());
        logPublisher.publish("PARSER", "Миссия успешно прочитана");

        IValidator missionValidationChain = validatorFactory.createValidationChain();
        if (missionValidationChain != null) {
            missionValidationChain.validate(mission);
            logPublisher.publish("VALIDATION", "Миссия отвалидирована");
        }

        IFilter missionFilterChain = createFilterChain(isFilterOn);
        if (missionFilterChain != null) {
            missionFilterChain.filter(mission);
            logPublisher.publish("FILTER", "Миссия отфильтрована");
        } else {
            logPublisher.publish("FILTER", "Фильтрация отключена");
        }

        return mission;
    }

    private IFilter createFilterChain(boolean isFilterOn) throws Exception {
        if (!isFilterOn) {
            return null;
        }

        registerFilters();
        logPublisher.publish("FILTER", "Фильтры подключены");
        return filterFactory.createFilterChain();
    }

    private void registerFilters() {
        filterFactory.register("DateFilter", new DateFilter("2024-10-12"));
        filterFactory.register("OutcomeFilter", new OutcomeFilter("SUCCESS"));
        filterFactory.register("ThreatLevelFilter", new ThreatLevelFilter("HIGH"));
    }

    private void registerLoggers() {
        logPublisher.register("ConsoleLogger", new ConsoleLogger());
    }

    private JFileChooser getJFileChooser() {
        JFileChooser chooser = new JFileChooser("C://Users//HONOR//Documents//Теория и технология программирования//Лабораторная работа 2//Тестовые файлы");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileFilter() {
            final ArrayList<String> extensions = new ArrayList<>(parserFactory.getParsers().keySet());

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                String extension = getExtension(file);
                return extensions.contains(extension);
            }

            @Override
            public String getDescription() {
                StringBuilder description = new StringBuilder();
                description.append("Доступные руны:");
                for(String extension : extensions) {
                    if(extension.isEmpty()) {
                        description.append(" без расширения");
                    } else {
                        description.append(", ").append(extension);
                    }
                }
                return description.toString();
            }
        });
        return chooser;
    }

    private IReportFormat returnSelectedFormat(ButtonGroup buttonGroup) throws Exception {
        ButtonModel selection = buttonGroup.getSelection();
        return reportFormatFactory.createReportFormat(selection.getActionCommand());
    }

    private String getExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex < 0 ? "" : fileName.substring(dotIndex + 1);
    }
}
