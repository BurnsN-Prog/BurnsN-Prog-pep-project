package Service;
import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;

import java.util.List;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        this.socialMediaDAO = new SocialMediaDAO();
    }
    

    public Account createAccount(Account account){
        return socialMediaDAO.createAccount(account);
    }

    public Account login( String username, String password){
        return socialMediaDAO.login(username, password);
    }

    public Message createMessage(Message message){
        return socialMediaDAO.createMessage(message);
    }

    public List<Message>getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    public Message retrieveMessageById(int message_id){
        return socialMediaDAO.retrieveMessageById(message_id);
    }

    public Message deleteMessageById(int message_id){
        return socialMediaDAO.deleteMessageById(message_id);
    }

    public Message updateMessageByMessageId(int message_id, String newMessage){
        return socialMediaDAO.updateMessageByMessageId(message_id, newMessage);
    }

    public List<Message>getMessagesByUser(int posted_by){
        return socialMediaDAO.getAllMessagesByUser(posted_by);
    }
    public Account retrieveUser(int account_id){
        return socialMediaDAO.retrieveUser(account_id);
    }
    public Account doesUsernameExist(String username){
        return socialMediaDAO.doesUsernameExist(username);
        
    }
}
