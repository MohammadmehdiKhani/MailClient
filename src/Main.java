import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException, ParseException
    {
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader("config.json"));

        String username = (String) ((JSONObject) object).get("username");
        String password = (String) ((JSONObject) object).get("password");

        String from = (String) ((JSONObject) object).get("from");
        String to = (String) ((JSONObject) object).get("to");
        String subject = (String) ((JSONObject) object).get("subject");
        String body = (String) ((JSONObject) object).get("body");
        String date = (String) ((JSONObject) object).get("date");

        Credentials credentials = new Credentials(username,password);

        Email email = new Email(from, to, subject, body, date);

        MailClient mailClient = new MailClient(credentials);
        mailClient.sendMail(email);
    }
}
