package qlvt.connect;

import qlvt.model.Warehouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {

    private DistributedDatabaseConnection dbConnection;

    public WarehouseDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Lấy kho dựa trên mã kho
    public Warehouse getWarehouseById(int maKho) throws SQLException {
        String query = "SELECT * FROM Kho WHERE MaKho = ?";
        Warehouse warehouse = null;



        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maKho);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                warehouse = new Warehouse(
                        rs.getInt("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("DiaChi"),
                        rs.getInt("MaChiNhanh")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy kho: " + e.getMessage());
            throw e;
        }
        return warehouse; // Trả về đối tượng Warehouse hoặc null nếu không tìm thấy
    }

    // Lấy danh sách tất cả các kho theo mã chi nhánh
    public List<Warehouse> getAllWarehouses(int maChiNhanh) throws SQLException {
        List<Warehouse> warehouses = new ArrayList<>();
        String query = "SELECT * FROM Kho WHERE MaChiNhanh = ?";



        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maChiNhanh); // Set branch ID in the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                warehouses.add(new Warehouse(
                        rs.getInt("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("DiaChi"),
                        rs.getInt("MaChiNhanh")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách kho: " + e.getMessage());
            throw e;
        }
        return warehouses; // Trả về danh sách kho
    }

    // Thêm kho mới
    public void addWarehouse(Warehouse warehouse) throws SQLException {
        String queryCheck = "SELECT COUNT(*) FROM Kho WHERE MaKho = ?";
        String queryInsert = "INSERT INTO Kho (MaKho, TenKho, DiaChi, MaChiNhanh) VALUES (?, ?, ?, ?)";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(queryCheck)) {

            // Kiểm tra xem MaKho đã tồn tại chưa
            checkStatement.setInt(1, warehouse.getMaKho());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.err.println("Kho với MaKho " + warehouse.getMaKho() + " đã tồn tại.");
                return; // Bỏ qua chèn nếu MaKho đã tồn tại
            }

            // Tiếp tục chèn nếu MaKho là duy nhất
            try (PreparedStatement insertStatement = connection.prepareStatement(queryInsert)) {
                insertStatement.setInt(1, warehouse.getMaKho());
                insertStatement.setString(2, warehouse.getTenKho());
                insertStatement.setString(3, warehouse.getDiaChi());
                insertStatement.setInt(4, warehouse.getMaChiNhanh());
                insertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm kho: " + e.getMessage());
            throw e;
        }
    }

    // Cập nhật thông tin kho
    public void updateWarehouse(Warehouse warehouse) throws SQLException {
        String query = "UPDATE Kho SET TenKho = ?, DiaChi = ?, MaChiNhanh = ? WHERE MaKho = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, warehouse.getTenKho());
            preparedStatement.setString(2, warehouse.getDiaChi());
            preparedStatement.setInt(3, warehouse.getMaChiNhanh());
            preparedStatement.setInt(4, warehouse.getMaKho());
            preparedStatement.executeUpdate(); // Thực hiện cập nhật
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật kho: " + e.getMessage());
            throw e;
        }
    }

    // Xóa kho
    public void deleteWarehouse(int maKho, int maChiNhanh) throws SQLException {
        String query = "DELETE FROM Kho WHERE MaKho = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maKho);
            int rowsAffected = preparedStatement.executeUpdate(); // Perform the delete

            if (rowsAffected > 0) {
                System.out.println("Xóa kho thành công.");
            } else {
                System.out.println("Không tìm thấy kho với mã: " + maKho);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa kho: " + e.getMessage());
            throw e;
        }
    }


    public List<Warehouse> getWarehousesByBranch(int maChiNhanh) throws SQLException {
        List<Warehouse> warehouses = new ArrayList<>();
        String query = "SELECT * FROM Kho WHERE MaChiNhanh = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maChiNhanh); // Set branch ID in the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                warehouses.add(new Warehouse(
                        rs.getInt("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("DiaChi"),
                        rs.getInt("MaChiNhanh")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách kho: " + e.getMessage());
            throw e;
        }
        return warehouses; // Trả về danh sách kho
    }

    public List<Warehouse> getWarehousesByBranchTong() throws SQLException {
        List<Warehouse> warehouses = new ArrayList<>();
        String query = "SELECT * FROM Kho ";



        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

           // stmt.setInt(1, maChiNhanh); // Set branch ID in the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                warehouses.add(new Warehouse(
                        rs.getInt("MaKho"),
                        rs.getString("TenKho"),
                        rs.getString("DiaChi"),
                        rs.getInt("MaChiNhanh")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách kho: " + e.getMessage());
            throw e;
        }
        return warehouses; // Trả về danh sách kho
    }



}
