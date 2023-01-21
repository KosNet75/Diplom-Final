import java.io.*;
import java.net.Socket;

public class Client {

  private static final int PORT = 8989;
  private static final String HOST = "127.0.0.1";

  public static void main(String[] args) {
    try (Socket socket = new Socket(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      String request = "бизнес и риски";
      System.out.println("Поиск слов - '" + request + "' в pdf документах с учетом stop листа.");
      out.println(request);

      in.lines().forEach(System.out::println);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
