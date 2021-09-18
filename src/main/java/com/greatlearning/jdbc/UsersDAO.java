package com.greatlearning.jdbc;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UsersDAO {

    public void saveData(Connection con, User user) throws SQLException {
        if (con != null && !con.isClosed()) {
            String str = "INSERT into users(user_id, first_name, last_name, email) values(? , ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(str);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            int row = preparedStatement.executeUpdate();
        }
    }


    public int updateData(Connection con, User user) throws SQLException {
        if (con != null && !con.isClosed()) {
            StringBuilder update = new StringBuilder();
            Class userClass = user.getClass();
            Map<Integer, String> map = new HashMap<>();
            int count = 0;
            for(Field f : userClass.getDeclaredFields()){
                if(!f.getName().equals("id")){
                    f.setAccessible(true);
                    try {
                        if(f.get(user) != null) {
                            count++;
                            if(f.getName().equals("firstName")){
                                update.append("first_name=?,");
                                map.put(count, user.getFirstName());
                            }
                            if(f.getName().equals("lastName")){
                                update.append("last_name=?,");
                                map.put(count, user.getLastName());
                            }
                            if(f.getName().equals("email")){
                                update.append("email=?,");
                                map.put(count, user.getEmail());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            String parameters = "";
            if(count > 0){
                parameters = update.substring(0, update.length()-1);
            }
            String str = "UPDATE users set "+parameters+" where user_id=?";
            PreparedStatement preparedStatement = con.prepareStatement(str);
            for (Map.Entry<Integer, String> entry :map.entrySet())
            {
                Integer key = (Integer)entry.getKey();
                String value = entry.getValue();
                preparedStatement.setString(key, value);
            }
            preparedStatement.setInt(count+1, user.getId());
            return preparedStatement.executeUpdate();
        }
        return -1;
    }

    public void getData(Connection connection) throws SQLException {
        String str = "select * from users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(str);
        int rowCounter = 0;
        while (rs.next()) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            int id = rs.getInt("user_id");
            System.out.println("User id = " +id);
            System.out.println("First name = " + firstName);
            System.out.println("Last name =" + lastName);
            System.out.println("email = " + email);
            System.out.println("**************");
            ++rowCounter;
        }
        System.out.println("Count of records: " + rowCounter);
    }

    public int delete(Connection con , Integer id) throws SQLException {
        if (con != null && !con.isClosed()) {
            String str = "DELETE from users where user_id=?";
            PreparedStatement preparedStatement = con.prepareStatement(str);
            preparedStatement.setInt(1, id);
            int row = preparedStatement.executeUpdate();
            return row;
        }
        return -1;
    }
}
