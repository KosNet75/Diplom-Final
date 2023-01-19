
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> wordList = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        try (Stream<Path> paths = Files.walk(Path.of(pdfsDir.toString()))) {
            List<Path> fileNameList = paths.filter(Files::isRegularFile).collect(Collectors.toList());

            for (Path path : fileNameList) {
                String name = path.getFileName().toString();

                try (PdfDocument doc = new PdfDocument(new PdfReader(path.toString()))) {

                    for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                        int pageNum = doc.getPageNumber(doc.getPage(i));

                        String text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                        String[] words = text.split("\\P{IsAlphabetic}+");

                        Map<String, Integer> wordCount = new HashMap<>();
                        for (String word : words) {
                            if (word.isEmpty()) {
                                continue;
                            }
                            word = word.toLowerCase();
                            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);

                        }

                        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                            PageEntry pageEntry = new PageEntry(name, pageNum, wordCount.get(entry.getKey()));
                            wordList.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(pageEntry);

                            for (List<PageEntry> list : wordList.values()) {
                                Collections.sort(list);
                            }

                        }

                    }
                }

            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return wordList.get(word) == null ? new ArrayList<>() : wordList.get(word);
    }
}