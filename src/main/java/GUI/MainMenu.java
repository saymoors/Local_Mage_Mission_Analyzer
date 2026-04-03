package GUI;

import Entities.Mission;
import Factories.ParserFactory;
import Factories.ReportFormatFactory;
import Parsers.IParser;
import Reports.IReportFormat;
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
    private final ValidatorFactory validatorFactory;
    private final IValidator missionValidationChain;

    public MainMenu() {
        parserFactory = new ParserFactory();
        reportFormatFactory = new ReportFormatFactory();
        validatorFactory = new ValidatorFactory();
        missionValidationChain = validatorFactory.createValidationChain();

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите миссию:");
        JButton button = new JButton("Открыть магический поисковик");

        ButtonGroup buttonGroup = new ButtonGroup();
        JFileChooser chooser = getJFileChooser();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setPreferredSize(new Dimension(240, 40));
        button.setMaximumSize(new Dimension(240, 40));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(_ -> {
            try {
                int choice = chooser.showOpenDialog(this);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    Mission mission = parseSelectedFile(file);
                    IReportFormat format = returnSelectedFormat(buttonGroup);
                    new SideMenu(mission, format);
                } else {
                    throw new Exception("Вы не выбрали миссию!");
                }
            } catch (Exception exception) {
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

        for (String reportFormat : reportFormatFactory.getReportFormats().keySet()) {
            JRadioButton radioButton = new JRadioButton(reportFormat);
            radioButton.setActionCommand(reportFormat);
            radioButton.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    private Mission parseSelectedFile(File file) throws Exception {
        String extension = getExtension(file);
        IParser parser = parserFactory.createParser(extension);
        Mission mission = parser.parse(file.getAbsolutePath());
        missionValidationChain.validate(mission);
        return mission;
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
