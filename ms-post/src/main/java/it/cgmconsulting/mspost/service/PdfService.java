package it.cgmconsulting.mspost.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Autowired
    PostService postService;

    @Value("${post.path}")
    private String pathImage;

    public InputStream createPdfFromPost(Post post) throws IOException {

        String title = post.getTitle();
        String content = post.getContent();
        String postImage = post.getImage();
        String updatedAt = post.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String author = "unknown";
        UserResponse user = postService.getUser(post.getAuthor());
        if(user != null)
            author = user.getUsername();

        // Dove il PDF verrà creato
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // GENERAZIONE PDF
        // creazione pdf vuoto e impostazioni di pagina
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        addMetaData(pdf,title,author);
        Document document = new Document(pdf, PageSize.A4, false); // immediateFlush = false così posso aggiungere i numeri di pagina a posteriori;
        // Il formato A4 corrisponde a (595.0F, 842.0F).
        // the 595.0F the coordinate of lower left point
        // the 842.0F - the y coordinate of lower left point

        // Contenuto del PDF
        {
//            Paragraph paragraphTitle = new Paragraph(title);
//            document.add(paragraphTitle);
//
//            Paragraph paragraphContent = new Paragraph(content);
//            document.add(paragraphContent);
//
//            Paragraph paragraphAuthor = new Paragraph(author);
//            document.add(paragraphAuthor);
//
//            if (postImage != null) {
//                ImageData data = ImageDataFactory.create(postImage);
//                document.add(new Image(data));
//            }
//
//            Paragraph paragraphDate = new Paragraph("Post aggioranto al " + updatedAt);
//            document.add(paragraphDate);
//
//            document.close();
        }

        // title, image, content, updatedAt (yyyy-MM-dd), author (username)
        // TITLE
        Paragraph pTitle = new Paragraph(title).setFontSize(20).setBold().setFontColor((new DeviceRgb(100,149,237)), 100);
        document.add(pTitle);

        addEmptyLines(document, 1);

        // IMAGE
        if(postImage != null) {
            ImageData imageData = ImageDataFactory.create(pathImage + post.getImage());
            document.add(new Image(imageData));
            addEmptyLines(document, 1);
        }

        // CONTENT
        Paragraph pContent = new Paragraph(content).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(pContent);

        addEmptyLines(document, 1);

        // UPDATED AT
        Paragraph pUpdtatedAt = new Paragraph("Data: "+updatedAt).setItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pUpdtatedAt);

        // AUTHOR
        Paragraph pAuthor = new Paragraph("Autore: "+author).setItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pAuthor);

        // NUMERI DI PAGINA (bottom/right)
        // genera errore da correggere
        int numberOfPages = pdf.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            document.showTextAligned(new Paragraph(String.format("page %s of %s", i, numberOfPages)).setFontSize(8).setItalic(),
                    560, 20, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
        }

        // CHIUSURA DOCUMENTO
        document.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());

        return in;
    }

    private static void addEmptyLines(Document document, int number) {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph("\n"));
        }
    }

    private void addMetaData(PdfDocument pdf, String title, String author) {
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle(title);
        info.setAuthor(author);
    }
}


    /* Marsela - Tabella

       PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100 f);
        table.setWidths(new int[] {3,3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Title", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Content", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("updaedAt", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("image", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("author", font));
        table.addCell(cell);

        for (PostPdf postPdf: studentList) {

            table.addCell(String.valueOf(postPdf.getTitle()));

            table.addCell(postPdf.getContent());

            table.addCell(postPdf.getUpdatedAt());

     */
