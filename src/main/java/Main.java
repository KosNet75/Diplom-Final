import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int PORT = 8989;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File textFile = new File("stop-ru.txt");

    public static void main(String[] args) throws Exception {

        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started");

            while (true) {
                try (Socket socket = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     Scanner scanner = new Scanner(textFile)) {

                    ArrayList<String> wordsList = new ArrayList<>();
                    Set<String> stopWords = new HashSet<>();
                    while (scanner.hasNextLine()) {
                        stopWords.add(scanner.nextLine());
                    }

                    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
                    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

                    System.out.println("Client connection accepted");

                    String[] words = in.readLine().split("\\P{IsAlphabetic}+");

                    for (String word : words) {
                        String wordCompare = word.toLowerCase();
                        if (!stopWords.contains(wordCompare)) {
                            wordsList.add(word);
                        }

                    }

                    for (String result : wordsList) {
                        out.println(
                                mapper
                                        .writer(prettyPrinter)
                                        .writeValueAsString(engine.search(result))
                        );
                    }

                }
            }

        } catch (IOException io) {
            System.out.println("Error. Can't start server.");
            throw new IOException(io);
        }

    }
}
