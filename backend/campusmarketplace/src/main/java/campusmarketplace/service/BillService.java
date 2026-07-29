package campusmarketplace.service;

import campusmarketplace.dto.OrderDetailResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class BillService {

    private final OrderService orderService;

    public BillService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Builds a PDF invoice for one order and returns it as raw bytes,
     * ready to be streamed to the browser as a download.
     */
    public byte[] generateBill(Long orderId) {

        OrderDetailResponse order = orderService.getOrderDetail(orderId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 40, 40, 50, 40);

        try {

            PdfWriter.getInstance(doc, out);
            doc.open();

            Color indigo = new Color(99, 102, 241);
            Color darkText = new Color(30, 30, 40);

            // ---- Header ----
            Font titleFont = new Font(Font.HELVETICA, 22, Font.BOLD, indigo);
            Paragraph title = new Paragraph("Campus Marketplace", titleFont);
            doc.add(title);

            Font subFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.GRAY);
            doc.add(new Paragraph("Tax Invoice / Bill of Supply", subFont));

            doc.add(Chunk.NEWLINE);

            // ---- Order meta ----
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            Font metaFont = new Font(Font.HELVETICA, 11, Font.NORMAL, darkText);

            doc.add(new Paragraph("Order ID: #" + order.getOrderId(), metaFont));
            doc.add(new Paragraph(
                    "Date: " + (order.getCreatedAt() != null
                            ? order.getCreatedAt().format(fmt) : "-"), metaFont));
            doc.add(new Paragraph("Billed To: " + order.getUserEmail(), metaFont));
            doc.add(new Paragraph(
                    "Transaction ID: " + (order.getTransactionId() != null
                            ? order.getTransactionId() : "-"), metaFont));

            doc.add(Chunk.NEWLINE);

            // ---- Items table ----
            PdfPTable table = new PdfPTable(new float[]{4, 1.2f, 1.5f, 1.5f});
            table.setWidthPercentage(100);

            Font headFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
            String[] headers = {"Item", "Qty", "Price", "Subtotal"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setBackgroundColor(indigo);
                cell.setPadding(8);
                cell.setBorderColor(indigo);
                table.addCell(cell);
            }

            Font bodyFont = new Font(Font.HELVETICA, 10, Font.NORMAL, darkText);
            for (OrderDetailResponse.Item item : order.getItems()) {

                double subtotal = item.getQuantity() * item.getPriceAtPurchase();

                table.addCell(cellOf(item.getTitle(), bodyFont, false));
                table.addCell(cellOf(String.valueOf(item.getQuantity()), bodyFont, true));
                table.addCell(cellOf("Rs. " + fmt2(item.getPriceAtPurchase()), bodyFont, true));
                table.addCell(cellOf("Rs. " + fmt2(subtotal), bodyFont, true));
            }

            doc.add(table);
            doc.add(Chunk.NEWLINE);

            // ---- Total ----
            Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD, indigo);
            Paragraph total = new Paragraph(
                    "Total: Rs. " + fmt2(order.getTotalAmount()), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.add(Chunk.NEWLINE);
            Font footFont = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);
            Paragraph foot = new Paragraph(
                    "Thank you for shopping with Campus Marketplace.", footFont);
            foot.setAlignment(Element.ALIGN_CENTER);
            doc.add(foot);

            doc.close();

        } catch (Exception e) {
            throw new RuntimeException("Could not generate bill", e);
        }

        return out.toByteArray();
    }

    private PdfPCell cellOf(String text, Font font, boolean center) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(7);
        cell.setBorderColor(new Color(220, 220, 220));
        if (center) cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private String fmt2(double v) {
        return String.format("%.2f", v);
    }
}