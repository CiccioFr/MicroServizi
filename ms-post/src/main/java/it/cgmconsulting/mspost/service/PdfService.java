package it.cgmconsulting.mspost.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.repository.PostRepository;
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
    PostRepository postRepository;
    @Autowired
    PostService postService;

    @Value("${postImage.path}")
    private String pathImage;

    public InputStream createPdfFromPost(Post post) throws IOException {

        String title = post.getTitle();
        String content = post.getContent();
        String image = post.getImage();
        String author = postService.getUser(post.getAuthor()).getUsername();
        String updateAt = post.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Dove il PDF verrà creato
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // GENERAZIONE PDF
        // creazione pdf vuoto e impostazioni di pagina
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document document = new Document(pdf, PageSize.A4); // Il formato A4 corrisponde a (595.0F, 842.0F).
        // the 595.0F the coordinate of lower left point
        // the 842.0F - the y coordinate of lower left point

        // Contenuto del PDF
        {
            Paragraph paragraphText = new Paragraph(title + content);
            document.add(paragraphText);

            Paragraph paragraphAuthor = new Paragraph(author);
            document.add(paragraphAuthor);

            ImageData data = ImageDataFactory.create(image);
            Image img = new Image(data);
            document.add(img);

            Paragraph paragraphDate = new Paragraph("Post aggioranto al "+updateAt);
            document.add(paragraphDate);

            document.close();
        }

        InputStream in = new ByteArrayInputStream(out.toByteArray());

        return in;
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
}
