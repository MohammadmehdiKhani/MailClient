import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class MailClient
{
    Credentials Credentials;
    SSLSocket SslSocket;
    InputStreamReader InputStreamReader;
    OutputStreamWriter OutputStreamWriter;


    public MailClient(Credentials credentials) throws IOException
    {
        Credentials = credentials;

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) factory.createSocket(Gmail.ADDRESS, Gmail.PORT);
        InputStreamReader = new InputStreamReader(sslsocket.getInputStream());
        OutputStreamWriter = new OutputStreamWriter(sslsocket.getOutputStream());
    }

    public void sendMail(Email email) throws IOException
    {
        SmtpResponse handShakeRes = getResponse();
        if (handShakeRes.StatusCode != 220)
            return;

        sendRequest(SMTP.HELO, ClientSetting.LOCAL_IP, SMTP.EndLine);
        SmtpResponse heloResponse = getResponse();
        if (heloResponse.StatusCode != 250)
            return;

        sendRequest( SMTP.AUTH_LOGIN, SMTP.EndLine);
        SmtpResponse authResponse = getResponse();
        sendRequest( Credentials.Username, SMTP.EndLine);
        SmtpResponse usernameResponse = getResponse();
        sendRequest( Credentials.Password, SMTP.EndLine);
        SmtpResponse passwordResponse = getResponse();

        if (passwordResponse.StatusCode != 235)
            return;

        sendRequest( SMTP.MAIL_FROM, email.From, SMTP.EndLine);
        SmtpResponse fromResponse = getResponse();
        if (fromResponse.StatusCode != 250)
            return;

        sendRequest( SMTP.RCPT_TO, email.To, SMTP.EndLine);
        SmtpResponse rcptResponse = getResponse();
        if (rcptResponse.StatusCode != 250)
            return;

        sendRequest( SMTP.DATA, SMTP.EndLine);
        SmtpResponse dataResponse = getResponse();
        if (dataResponse.StatusCode != 354)
            return;

        sendRequest( SMTP.From, email.From, SMTP.EndLine);
        sendRequest( SMTP.To, email.To, SMTP.EndLine);
        sendRequest( SMTP.Date, email.Date, SMTP.EndLine);
        sendRequest( SMTP.Subject, email.Subject, SMTP.EndLine);
        sendRequest( SMTP.EndLine);

        String[] lines = email.Body.split("\n");

        for (int i = 0; i < lines.length; i++)
            sendRequest( lines[i], SMTP.EndLine);

        sendRequest( ".", SMTP.EndLine);
        SmtpResponse dataEndResponse = getResponse();
        if (dataEndResponse.StatusCode != 250)
            return;

        sendRequest(SMTP.QUIT, SMTP.EndLine);
        SmtpResponse quitResponse = getResponse();
        if (quitResponse.StatusCode != 221)
            return;
    }

    private SmtpResponse getResponse() throws IOException
    {
        char[] response = new char[1000];
        InputStreamReader.read(response);
        SmtpResponse smtpResponse = new SmtpResponse(new String(response));
        System.out.print(new String(response));
        return smtpResponse;
    }

    private void sendRequest(String... args) throws IOException
    {
        String command = "";

        for (String arg : args)
            command += arg;

        System.out.print(command);
        OutputStreamWriter.write(command);
        OutputStreamWriter.flush();
    }
}

