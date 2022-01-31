import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Credentials{
    String Username;
    String Password;

    public Credentials(String username, String password)
    {
        Username = Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8));
        Password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }
}
