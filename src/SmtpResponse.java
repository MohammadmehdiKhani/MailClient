public class SmtpResponse
{
    int StatusCode;
    String Message;

    public SmtpResponse(String response)
    {
        StatusCode = Integer.parseInt(response.substring(0, 3));
        Message = response.substring(3);
    }
}
