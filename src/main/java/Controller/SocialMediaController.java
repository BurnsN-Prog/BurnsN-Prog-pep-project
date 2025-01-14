package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;

import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    SocialMediaService socialMediaService;

    public SocialMediaController(){
        this.socialMediaService = new SocialMediaService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccountHandler(Context context)throws JsonProcessingException{ 
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        if(account.getUsername() ==null|| account.getUsername().isBlank()){
            context.status(400).result();
            return;
        }
        if(account.getPassword()==null|| account.getPassword().length() < 4 ){
            context.status(400).result();
            return;
        }
        Account existingAccount = socialMediaService.doesUsernameExist(account.getUsername());
        if(existingAccount !=null){
            context.status(400).result();
            return;
        }
        Account addAccount = socialMediaService.createAccount(account);
        if(addAccount != null){
            context.status(200).json(addAccount);
        }else{
            context.status(400).result();
        }
    
    }
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
       Account loginAttempt = mapper.readValue(ctx.body(), Account.class);
       if (loginAttempt.getUsername()==null|| loginAttempt.getUsername().isBlank()){
        ctx.status(401);
        return;
       }
       if(loginAttempt.getPassword()==null|| loginAttempt.getPassword().isBlank()){
        ctx.status(401);
        return;
       }
       Account accountVerified = socialMediaService.login(loginAttempt.getUsername(), loginAttempt.getPassword());
       if (accountVerified != null){
        ctx.status(200).json(accountVerified);

       }else{
        ctx.status(401);
       }

    }
    private void createMessageHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message createMessage = mapper.readValue(ctx.body(), Message.class);
        if (createMessage.getMessage_text().isBlank()|| createMessage.getMessage_text().length()>255){
            ctx.status(400);
            return;
        }
        Account userExists = socialMediaService.retrieveUser(createMessage.getPosted_by());
        if (userExists == null){
            ctx.status(400);
            return;
        }
        Message createdMessage = socialMediaService.createMessage(createMessage);
        if(createdMessage != null){
            ctx.status(200).json(createMessage);
        }else{
            ctx.status(400);
        }


    }
    private void getAllMessagesHandler(Context ctx){
        List<Message> getAllMessages = socialMediaService.getAllMessages();
        if(getAllMessages != null) {

            ctx.status(200).json(getAllMessages);
        }

    }
    private void getMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageExists = socialMediaService.retrieveMessageById(message_id);
        if(messageExists != null){
            ctx.status(200).json(messageExists);
        }else{
            ctx.result("");
        }


    }
    private void deleteMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = socialMediaService.deleteMessageById(messageId);
        if(deletedMessage != null){
            ctx.status(200).json(deletedMessage);
        }else{
            ctx.result("");
        }

    }
    private void updateMessageByMessageIdHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message updateMessage = mapper.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessage = updateMessage.getMessage_text();

        if(newMessage==null|| newMessage.isBlank()||newMessage.length()> 255){
            ctx.status(400);
            return;
        }
        Message messageIdExists = socialMediaService.retrieveMessageById(messageId);
        if(messageIdExists == null){
            ctx.status(400);
            return;
        }
        Message updatedMessage = socialMediaService.updateMessageByMessageId(
            messageId,
            newMessage
        );

       if(updatedMessage != null){
        ctx.status(200).json(updatedMessage);
    }else{
        ctx.status(400);
    }
}
    private void getMessagesByUserHandler(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> getAllMessages = socialMediaService.getMessagesByUser(accountId);

        ctx.json(getAllMessages);
        
    
    }


}