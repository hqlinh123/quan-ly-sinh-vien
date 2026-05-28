package ui;

import service.StudentService;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField txtId, txtName, txtClass, txtAge, txtSearch;
    private JTable table;
    private DefaultTableModel model;

    private StudentService service = new StudentService();

    public MainFrame() {
        setTitle("Quản lý sinh viên");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // TOP FORM
        JPanel form = new JPanel(new GridLayout(2, 5));
        txtId = new JTextField();
        txtName = new JTextField();
        txtClass = new JTextField();
        txtAge = new JTextField();

        form.add(new JLabel("Mã SV"));
        form.add(new JLabel("Tên"));
        form.add(new JLabel("Lớp"));
        form.add(new JLabel("Tuổi"));
        form.add(new JLabel(""));

        form.add(txtId);
        form.add(txtName);
        form.add(txtClass);
        form.add(txtAge);

        JButton btnAdd = new JButton("Thêm");
        form.add(btnAdd);

        panel.add(form, BorderLayout.NORTH);

        // TABLE
        model = new DefaultTableModel(new String[]{"ID", "Tên", "Lớp", "Tuổi"}, 0);
        table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // BOTTOM
        JPanel bottom = new JPanel();
        JButton btnDelete = new JButton("Xoá");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDetail = new JButton("Chi tiết");

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Tìm");

        bottom.add(btnDelete);
        bottom.add(btnUpdate);
        bottom.add(btnDetail);
        bottom.add(txtSearch);
        bottom.add(btnSearch);

        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);

        // ===== EVENT =====
        btnAdd.addActionListener(e -> {
            Student s = new Student(
                    txtId.getText(),
                    txtName.getText(),
                    txtClass.getText(),
                    Integer.parseInt(txtAge.getText())
            );
            service.add(s);
            refreshTable(service.getAll());
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = model.getValueAt(row, 0).toString();
                service.delete(id);
                refreshTable(service.getAll());
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = model.getValueAt(row, 0).toString();
                Student s = new Student(
                        id,
                        txtName.getText(),
                        txtClass.getText(),
                        Integer.parseInt(txtAge.getText())
                );
                service.update(id, s);
                refreshTable(service.getAll());
            }
        });

        btnSearch.addActionListener(e -> {
            refreshTable(service.search(txtSearch.getText()));
        });

        btnDetail.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = model.getValueAt(row, 0).toString();
                Student s = service.findById(id);
                JOptionPane.showMessageDialog(this,
                        "ID: " + s.getId() +
                        "\nTên: " + s.getName() +
                        "\nLớp: " + s.getClassName() +
                        "\nTuổi: " + s.getAge()
                );
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtClass.setText(model.getValueAt(row, 2).toString());
                txtAge.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void refreshTable(java.util.List<Student> list) {
        model.setRowCount(0);
        for (Student s : list) {
            model.addRow(new Object[]{
                    s.getId(), s.getName(), s.getClassName(), s.getAge()
            });
        }
    }
}