package GUI;

import Entities.Mission;
import Parsers.IParser;
import Parsers.ParserJSON;
import Parsers.ParserXML;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainMenu extends JFrame {
    JPanel panel;
    JLabel label;
    JButton button;
    JFileChooser chooser;
    FileNameExtensionFilter filter;
    File file;

    public MainMenu() {
        panel = new JPanel();
        label = new JLabel("Выберите миссию:");
        button = new JButton("Открыть магический поисковик");
        chooser = new JFileChooser();
        filter = new FileNameExtensionFilter("Доступные руны: json, txt, xml", "json", "txt", "xml");

        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setPreferredSize(new Dimension(260, 90));
        button.setPreferredSize(new Dimension(240, 40));

        button.addActionListener(e -> {
            try {
                int choice = chooser.showOpenDialog(this);

                if (choice == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                    Mission mission = parseSelectedFile(file);
                    SideMenu sideMenu = new SideMenu(mission);
                } else {
                    throw new Exception("Вы не выбрали миссию!");
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(label);
        panel.add(button);
        getContentPane().add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Локальный анализатор миссий");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private Mission parseSelectedFile(File file) throws Exception {
        IParser parser;
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        switch(extension) {
            case "json":
                parser = new ParserJSON();
                return parser.parse(file.getAbsolutePath());
            case "xml":
                parser = new ParserXML();
                return parser.parse(file.getAbsolutePath());
            default:
                throw new Exception("Вы выбрали иную руну!");
        }
    }
}



