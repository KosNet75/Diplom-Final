import com.fasterxml.jackson.annotation.JsonProperty;

public class PageEntry implements Comparable<PageEntry> {
    @JsonProperty("pdf") private final String pdfName;
    @JsonProperty("page") private final int page;
    @JsonProperty("count") private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    protected String getPdfName() {
    return pdfName;
}
    protected int getPage() {
        return page;
    }

    protected int getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return o.getCount() - this.getCount();
    }

    @Override
    public String toString() {
        return String.format("PageEntry{pdfName=%s, page=%d, count=%d}", pdfName, page, count);
    }
}
