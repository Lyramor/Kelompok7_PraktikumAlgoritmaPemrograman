package util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lowagie.text.Font.HELVETICA;

public class PDFGenerator {
    private static final Font TITLE_FONT = new Font(HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(HELVETICA, 10, Font.NORMAL);

    public static void generateUserReport(DefaultTableModel model, String path) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        addTitle(document, "User Report");
        addMetadata(document);

        PdfPTable table = new PdfPTable(6); // 6 columns for user table
        table.setWidthPercentage(100);

        // Add headers
        addTableHeader(table, new String[]{"ID", "Username", "Email", "Phone Number", "Address", "Role"});

        // Add data rows
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                table.addCell(new Phrase(String.valueOf(model.getValueAt(i, j)), NORMAL_FONT));
            }
        }

        document.add(table);
        document.close();
    }

    public static void generateJenisReport(DefaultTableModel model, String path) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        addTitle(document, "Jenis Sampah Report");
        addMetadata(document);

        PdfPTable table = new PdfPTable(3); // 3 columns for jenis table
        table.setWidthPercentage(100);

        // Add headers
        addTableHeader(table, new String[]{"ID", "Nama Jenis", "Kategori"});

        // Add data rows
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                table.addCell(new Phrase(String.valueOf(model.getValueAt(i, j)), NORMAL_FONT));
            }
        }

        document.add(table);
        document.close();
    }

    public static void generateKategoriReport(DefaultTableModel model, String path) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        addTitle(document, "Kategori Sampah Report");
        addMetadata(document);

        PdfPTable table = new PdfPTable(2); // 2 columns for kategori table
        table.setWidthPercentage(100);

        // Add headers
        addTableHeader(table, new String[]{"ID", "Nama Kategori"});

        // Add data rows
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                table.addCell(new Phrase(String.valueOf(model.getValueAt(i, j)), NORMAL_FONT));
            }
        }

        document.add(table);
        document.close();
    }

    private static void addTitle(Document document, String title) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(title, TITLE_FONT);
        titleParagraph.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(20);
        document.add(titleParagraph);
    }

    private static void addMetadata(Document document) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Paragraph metadata = new Paragraph("Generated on: " + sdf.format(new Date()), NORMAL_FONT);
        metadata.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        metadata.setSpacingAfter(20);
        document.add(metadata);
    }

    private static void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }
    }
}