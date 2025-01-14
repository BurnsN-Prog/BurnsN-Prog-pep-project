package DAO;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO{

// method to create an account
public Account createAccount(Account account){
    Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "INSERT INTO Account(username, password) VALUES(?,?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());

        int rowsAffected = ps.executeUpdate();
        if(rowsAffected == 1){
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                account.setAccount_id(rs.getInt(1));
                return account;
            }
        }

        
    }catch(SQLException e){
        e.printStackTrace();

    }
    return null;
}

// method to verify login
public Account login(String username, String password){
    Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
            return account;
        }
    
    
    }catch(SQLException e){
        e.printStackTrace();
    }
    return null;
}

//method to create new messages
public Message createMessage(Message message){
    Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());

        int rowsAffected = ps.executeUpdate();
        if( rowsAffected == 1){
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                message.setMessage_id(rs.getInt(1));
                   return message;
            }
        }

    }catch(SQLException e){
        e.printStackTrace();
    }
    return null;
}
//method to retrieve all messages
public List<Message> getAllMessages(){
    Connection connection = ConnectionUtil.getConnection();

    List<Message> messages = new ArrayList<>();
    try{
        String sql = "SELECT * FROM Message";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return messages;

}

// method to retrieve a message by id
public Message retrieveMessageById(int message_id){
    Connection connection = ConnectionUtil.getConnection();

    try{
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, message_id);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Message message = new Message();
            message.setMessage_id(rs.getInt("message_id"));
            message.setPosted_by(rs.getInt("posted_by"));
            message.setMessage_text(rs.getString("message_text"));
            message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            return message;
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return null;
}

//method to delete message by id
public Message deleteMessageById(int message_id){
    Connection connection = ConnectionUtil.getConnection();
    Message deletedM = null;
    try{
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        

        ps.setInt(1, message_id);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            deletedM = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }

        String deletedsql = "DELETE FROM Message WHERE message_id =?";
        PreparedStatement deletePs = connection.prepareStatement(deletedsql);
        deletePs.setInt(1, message_id);
        deletePs.executeUpdate();


    }catch(SQLException e){
        e.printStackTrace();
    }
    return deletedM;
}

//method to update a message by id
public Message updateMessageByMessageId(int message_id, String newMessage){
    Connection connection = ConnectionUtil.getConnection();
    Message updatedMessage = null;

    try{
        String updateSql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
    
        PreparedStatement updatePs = connection.prepareStatement(updateSql);
        updatePs.setString(1, newMessage);
        updatePs.setInt(2, message_id);
        int rowsAffected = updatePs.executeUpdate();
        if(rowsAffected == 0){
            return null;
        }

        String sql = "SELECT * FROM Message WHERE message_id =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, message_id);

        
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
            updatedMessage = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
        
    }catch(SQLException e){

        e.printStackTrace();
    }
    return updatedMessage;
}

//method to retrieve all messages by a  paticular user
public List<Message> getAllMessagesByUser(int posted_by){
    List<Message>messages = new ArrayList<>();

    Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, posted_by);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Message message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch") );
            messages.add(message);
        }
    
    }catch(SQLException e){
        e.printStackTrace();
    }
    
    return messages;
}
// method to check if a user exist. This will be needed for the createMessageHandler
public Account retrieveUser(int account_id){
    Connection connection = ConnectionUtil.getConnection();

    try{
        String sql = "SELECT * FROM Account WHERE account_id =?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, account_id);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Account account = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
            return account;
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return null;
}
//method to check if a username exists
public Account doesUsernameExist(String username) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
            return account;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}
