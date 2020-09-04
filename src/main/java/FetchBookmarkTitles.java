

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FetchBookmarkTitles {
    public static final String DEST = "./target/txt/bookmarks.txt";

    public static final String SRC = "C:\\Users\\Zheng\\Desktop\\MySQL 8 Query Performance Tuning.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FetchBookmarkTitles().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        // This method returns a complete outline tree of the whole document.
        // If the flag is false, the method gets cached outline tree (if it was cached
        // via calling getOutlines method before).
        PdfOutline outlines = pdfDoc.getOutlines(false);
        List<PdfOutline> bookmarks = outlines.getAllChildren();

        pdfDoc.close();

        List<Title> titles = new ArrayList<>();
        for (PdfOutline bookmark : bookmarks) {
            addTitle(1, bookmark, titles);
        }

        // See title's names in the console
        // for (String title : titles) {
        //     System.out.println(title);
        // }

        createResultTxt(dest, titles);
    }

    /*
     * This recursive method calls itself if an examined bookmark entry has kids.
     * The method writes bookmark title to the passed list
     */
    private void addTitle(int level, PdfOutline outline, List<Title> result) {
        String bookmarkTitle = outline.getTitle();

        result.add(new Title(level, bookmarkTitle));

        List<PdfOutline> kids = outline.getAllChildren();
        System.out.println(kids);
        if (kids != null) {
            for (PdfOutline kid : kids) {
                int nextLevel = level + 1;
                addTitle(nextLevel, kid, result);
            }
        }
    }

    private void createResultTxt(String dest, List<Title> titles) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)))) {
            for (int i = 0; i < titles.size(); i++) {
                Title title = titles.get(i);
                int num = (title.level -1) *2;
                String line = String.valueOf("    ").repeat(Math.max(0, num))+title.Content.replace(" ", " ")+"\n";
                writer.write(line);
            }
        }
    }
}


class Title{
    int level;
    String Content;

    public Title(int level, String content) {
        this.level = level;
        Content = content;
    }
}