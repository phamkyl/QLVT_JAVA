package qlvt.connect;

import qlvt.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static qlvt.connect.DistributedDatabaseConnection.SERVER1_URL;

public class EmployeeDAO {

    private DistributedDatabaseConnection dbConnection;

    public EmployeeDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Phương thức để tìm nhân viên theo mã nhân viên và mật khẩu
    public Employee getEmployeeByCredentials(String maNhanVien, String matKhau,String role) throws SQLException {
        String query = "SELECT * FROM NhanVien WHERE MaNhanVien = ? AND MatKhau = ? AND PhanQuyen = ? ";
        Employee employee = null;





        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, maNhanVien);
            stmt.setString(2, matKhau);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employee = new Employee(
                        rs.getInt("MaNhanVien"),
                        rs.getString("HoTen"),
                        rs.getString("ChucVu"),
                        rs.getInt("MaChiNhanh"),
                        rs.getString("PhanQuyen"),
                        rs.getString("MatKhau")
                );
            }
        }
        return employee; // Trả về đối tượng Employee hoặc null nếu không tìm thấy
    }

    // Lấy mã chi nhánh dựa trên mã nhân viên
    private int getBranchByEmployeeId(String maNhanVien) throws SQLException {
        String query = "SELECT MaChiNhanh FROM NhanVien WHERE MaNhanVien = ?";
        int maChiNhanh = -1; // Khởi tạo với mã chi nhánh không hợp lệ


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                maChiNhanh = rs.getInt("MaChiNhanh");
            }
        }
        return maChiNhanh; // Trả về mã chi nhánh
    }

    // Lấy danh sách tất cả nhân viên dựa trên mã chi nhánh
    public List<Employee> getAllEmployees(int maChiNhanh) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM NhanVien WHERE MaChiNhanh = ?"; // Lọc theo mã chi nhánh


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maChiNhanh); // Thiết lập mã chi nhánh trong truy vấn
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("MaNhanVien"),
                        rs.getString("HoTen"),
                        rs.getString("ChucVu"),
                        rs.getInt("MaChiNhanh"),
                        rs.getString("PhanQuyen"),
                        rs.getString("MatKhau")
                ));
            }
        }
        return employees; // Trả về danh sách nhân viên
    }

    // Lấy danh sách tất cả nhân viên dựa trên mã chi nhánh
    public List<Employee> getAllEmployeesTong() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM NhanVien "; // Lọc theo mã chi nhánh

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            //stmt.setInt(1, maChiNhanh); // Thiết lập mã chi nhánh trong truy vấn
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("MaNhanVien"),
                        rs.getString("HoTen"),
                        rs.getString("ChucVu"),
                        rs.getInt("MaChiNhanh"),
                        rs.getString("PhanQuyen"),
                        rs.getString("MatKhau")
                ));
            }
        }
        return employees; // Trả về danh sách nhân viên
    }

    // Thêm một nhân viên mới
    public void addEmployee(Employee employee) throws SQLException {
        String query = "INSERT INTO NhanVien (MaNhanVien, HoTen, ChucVu, MaChiNhanh, PhanQuyen, MatKhau) VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employee.getMaNhanVien());
            preparedStatement.setString(2, employee.getHoTen());
            preparedStatement.setString(3, employee.getChucVu());
            preparedStatement.setInt(4, employee.getMaChiNhanh());
            preparedStatement.setString(5, employee.getPhanQuyen());
            preparedStatement.setString(6, employee.getMatKhau());
            preparedStatement.executeUpdate(); // Thực hiện chèn dữ liệu
        }
    }

    // Cập nhật một nhân viên đã tồn tại
    public void updateEmployee(Employee employee) throws SQLException {
        String query = "UPDATE NhanVien SET HoTen = ?, ChucVu = ?, PhanQuyen = ?, MatKhau = ? WHERE MaNhanVien = ? AND MaChiNhanh = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getHoTen());
            preparedStatement.setString(2, employee.getChucVu());
            preparedStatement.setString(3, employee.getPhanQuyen());
            preparedStatement.setString(4, employee.getMatKhau());
            preparedStatement.setInt(5, employee.getMaNhanVien());
            preparedStatement.setInt(6, employee.getMaChiNhanh());
            preparedStatement.executeUpdate(); // Thực hiện cập nhật
        }
    }

    // Xóa một nhân viên
    public void deleteEmployee(int maNhanVien, int maChiNhanh) throws SQLException {
        String query = "DELETE FROM NhanVien WHERE MaNhanVien = ? AND MaChiNhanh = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maNhanVien);
            preparedStatement.setInt(2, maChiNhanh);
            preparedStatement.executeUpdate(); // Thực hiện xóa
        }
    }

    // Xóa một nhân viên
    public void deleteEmployeeTong(int maNhanVien) throws SQLException {
        String query = "DELETE FROM NhanVien WHERE MaNhanVien = ? ";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maNhanVien);
            //preparedStatement.setInt(2, maChiNhanh);
            preparedStatement.executeUpdate(); // Thực hiện xóa
        }
    }


}
