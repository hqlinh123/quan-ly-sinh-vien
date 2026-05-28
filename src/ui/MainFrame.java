package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Student;
import service.StudentService;

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
        model = new DefaultTableModel(new String[] { "ID", "Tên", "Lớp", "Tuổi" }, 0);
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
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String className = txtClass.getText().trim();
            int age;
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống!");
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên sinh viên không được để trống!");
                return;
            }

           

            if (className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lớp không được để trống!");
                return;
            }

            if (txtAge.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tuổi không được để trống!");
                return;
            }

            if (!name.matches("[a-zA-ZÀ-ỹ\\s]+")) {
                JOptionPane.showMessageDialog(this, "Tên chỉ được chứa chữ!");
                return;
            }

            if (!className.matches("[a-zA-Z0-9\\s]+")) {
                JOptionPane.showMessageDialog(this, "Lớp không hợp lệ!");
                return;
            }

            try {
                age = Integer.parseInt(txtAge.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tuổi phải là số!");
                return;
            }
            Student s = new Student(
                    txtId.getText(),
                    txtName.getText(),
                    txtClass.getText(),
                    age);
            boolean added = service.add(s);
            if (!added) {
                JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
                return;
            }
            refreshTable(service.getAll());
            clearForm();
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để xoá!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xoá sinh viên này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            String id = model.getValueAt(row, 0).toString();

            service.delete(id);
            refreshTable(service.getAll());
            clearForm();
            JOptionPane.showMessageDialog(this, "🗑️ Xoá sinh viên thành công!");
        });
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để sửa!");
                return;
            }

            String id = model.getValueAt(row, 0).toString();
            String name = txtName.getText().trim();
            String className = txtClass.getText().trim();

             if (!name.matches("[a-zA-ZÀ-ỹ\\s]+")) {
                JOptionPane.showMessageDialog(this, "Tên chỉ được chứa chữ!");
                return;
            }

            if (!className.matches("[a-zA-Z0-9\\s]+")) {
                JOptionPane.showMessageDialog(this, "Lớp không hợp lệ!");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(txtAge.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tuổi phải là số!");
                return;
            }

            Student s = new Student(
                    id,
                    txtName.getText(),
                    txtClass.getText(),
                    age);

            service.update(id, s);
            refreshTable(service.getAll());
            clearForm();
            JOptionPane.showMessageDialog(this, "✅ Cập nhật sinh viên thành công!");
        });

        btnSearch.addActionListener(e -> {
            if (txtSearch.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khoá để tìm kiếm!");
                return;
            }
            java.util.List<Student> result = service.search(txtSearch.getText());
            refreshTable(result);
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
                                "\nTuổi: " + s.getAge());
            }else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để xem chi tiết!");
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

        if (list.isEmpty()) {
            model.addRow(new Object[] { "", "Không tìm thấy sinh viên", "", "" });
            return;
        }

        for (Student s : list) {
            model.addRow(new Object[] {
                    s.getId(), s.getName(), s.getClassName(), s.getAge()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtClass.setText("");
        txtAge.setText("");
    }
}