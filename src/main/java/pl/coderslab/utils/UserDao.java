package pl.coderslab.utils;

import pl.coderslab.entity.User;
import pl.coderslab.utils.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao  {

    public User create(User user) {
        try (Connection connection = DbUtil.getConnection();) {

            String sqlQuery = "INSERT INTO user(name, email, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement =   connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

        /*public User create(User user) {
    try (Connection conn = DbUtil.getConnection()) {
        PreparedStatement statement =
                conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        statement.setString(3, hashPassword(user.getPassword()));
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            user.setId(resultSet.getInt(1));
        }
        return user;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}*/

    public User read(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            User user;
            String sqlQuery = "SELECT * FROM user WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("name");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                user = new User(userId, userName, userEmail, userPassword);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void update(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            String sqlQuery = "UPDATE  user SET name = ?, email = ?, password = ? WHERE id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(int id) {
        try (Connection connection = DbUtil.getConnection()) {
            String sqlQuery = "DELETE FROM user WHERE id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> findAll() {
        try (Connection connection = DbUtil.getConnection()) {
            List<User> userList = new ArrayList<>();
            User user;
            String sqlQuery = "SELECT * FROM user";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("name");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                user = new User(userId, userName, userEmail, userPassword);
                userList.add(user);
            }
            preparedStatement.close();
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeALL() {
        try (Connection connection = DbUtil.getConnection()) {
            String sqlQuery = "DELETE FROM user";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}






//package pl.coderslab.utils;
//
//import org.mindrot.jbcrypt.BCrypt;
//import pl.coderslab.entity.User;
//
//import java.sql.*;
//import java.util.Arrays;
//
//public class UserDao {
//
//    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
//
//    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?";
//
//    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
//
//    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
//
//    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
//
//
//    public String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//    public User create(User user) {
//
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
//            statement.setString(1, user.getUserName());
//            statement.setString(2, user.getEmail());
//            statement.setString(3, hashPassword(user.getPassword()));
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//            if (resultSet.next()) {
//                user.setId(resultSet.getInt(1));
//            }
//            return user;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public User read(int userId) {
//
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
//            statement.setInt(1, userId);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                User user = new User();
//                user.setId(resultSet.getInt("id"));
//                user.setUserName(resultSet.getString("username"));
//                user.setEmail(resultSet.getString("email"));
//                user.setPassword(resultSet.getString("password"));
//                return user;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void update(User user) {
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
//            statement.setString(1, user.getUserName());
//            statement.setString(2, user.getEmail());
//            statement.setString(3, this.hashPassword(user.getPassword()));
//            statement.setInt(4, user.getId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private User[] addToArray(User u, User[] users) {
//        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
//        tmpUsers[users.length] = u;
//        return tmpUsers;
//    }
//
//    public User[] findAll() {
//
//        try (Connection conn = DbUtil.getConnection()) {
//            User[] users = new User[0];
//            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                User user = new User();
//                user.setId(resultSet.getInt("id"));
//                user.setUserName(resultSet.getString("username"));
//                user.setEmail(resultSet.getString("email"));
//                user.setPassword(resultSet.getString("password"));
//                users = addToArray(user, users);
//            }
//            return users;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void delete(int userId) {
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
//            statement.setInt(1, userId);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
