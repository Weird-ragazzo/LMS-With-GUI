package LMSWithGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryGUI extends JFrame {
    static final String DB_URL = "jdbc:sqlite:library.db";
    Connection conn;

    JTextField titleField, authorField, yearField, pagesField, buyPriceField, borrowPriceField;
    JTextArea displayArea;

    public LibraryGUI() {
        setTitle("Library Management System");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel with GridBagLayout for cleaner UI
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Title:", "Author:", "Year:", "Pages:", "Buy Price:", "Borrow Price/Month:"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField(15);
            inputPanel.add(fields[i], gbc);
        }

        titleField = fields[0];
        authorField = fields[1];
        yearField = fields[2];
        pagesField = fields[3];
        buyPriceField = fields[4];
        borrowPriceField = fields[5];

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        String[] btnNames = {"Add Book", "Show Books", "Issue Book", "Return Book", "Delete Book", "Update Book"};
        JButton[] buttons = new JButton[btnNames.length];

        for (int i = 0; i < btnNames.length; i++) {
            buttons[i] = new JButton(btnNames[i]);
            buttonPanel.add(buttons[i]);
        }

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // Connect to DB and create table
        connectDB();
        createTable();

        // Button actions
        buttons[0].addActionListener(e -> addBook());
        buttons[1].addActionListener(e -> showBooks());
        buttons[2].addActionListener(e -> issueBook());
        buttons[3].addActionListener(e -> returnBook());
        buttons[4].addActionListener(e -> deleteBook());
        buttons[5].addActionListener(e -> updateBook());

        setVisible(true);
    }

   void connectDB() {
    try {
        conn = DriverManager.getConnection(DB_URL);
        // System.out.println("DB path: " + new java.io.File("library.db").getAbsolutePath());
    } catch (SQLException e) {
        showError("Database connection failed: " + e.getMessage());
    }
}

    void createTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT, author TEXT, year INTEGER, pages INTEGER, " +
                    "buy_price REAL, borrow_price REAL, issued BOOLEAN DEFAULT 0)");
        } catch (SQLException e) {
            showError("Table creation failed: " + e.getMessage());
        }
    }

    void addBook() {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO books (title, author, year, pages, buy_price, borrow_price, issued) VALUES (?, ?, ?, ?, ?, ?, 0)")) {
            pstmt.setString(1, titleField.getText());
            pstmt.setString(2, authorField.getText());
            pstmt.setString(3, yearField.getText().isEmpty() ? null : yearField.getText());
            pstmt.setString(4, pagesField.getText().isEmpty() ? null : pagesField.getText());
            pstmt.setString(5, buyPriceField.getText().isEmpty() ? null : buyPriceField.getText());
            pstmt.setString(6, borrowPriceField.getText().isEmpty() ? null : borrowPriceField.getText());
            pstmt.executeUpdate();
            showInfo("Book added successfully.");
        } catch (SQLException e) {
            showError("Add book failed: " + e.getMessage());
        }
    }

    void showBooks() {
    displayArea.setText("");
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-20s %-20s %-6s %-6s %-10s %-12s %-10s%n",
                "ID", "Title", "Author", "Year", "Pages", "Buy Price", "Borrow Price", "Status"));
        sb.append("------------------------------------------------------------------------------------------------------------\n");
        while (rs.next()) {
            found = true;
            sb.append(String.format("%-5d %-20s %-20s %-6d %-6d %-10.2f %-12.2f %-10s%n",
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("year"),
                    rs.getInt("pages"),
                    rs.getDouble("buy_price"),
                    rs.getDouble("borrow_price"),
                    rs.getBoolean("issued") ? "Issued" : "Available"));
        }
        if (!found) {
            displayArea.setText("No books found.");
        } else {
            displayArea.setText(sb.toString());
        }
    } catch (SQLException e) {
        showError("Show books failed: " + e.getMessage());
    }
}



    void issueBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Book ID to Issue:");
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE books SET issued=1 WHERE id=?")) {
            pstmt.setInt(1, Integer.parseInt(id));
            int updated = pstmt.executeUpdate();
            showInfo(updated > 0 ? "Book issued." : "Book not found.");
        } catch (SQLException | NumberFormatException e) {
            showError("Issue book failed: " + e.getMessage());
        }
    }

    void returnBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Book ID to Return:");
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE books SET issued=0 WHERE id=?")) {
            pstmt.setInt(1, Integer.parseInt(id));
            int updated = pstmt.executeUpdate();
            showInfo(updated > 0 ? "Book returned." : "Book not found.");
        } catch (SQLException | NumberFormatException e) {
            showError("Return book failed: " + e.getMessage());
        }
    }

    void deleteBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Book ID to Delete:");
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            pstmt.setInt(1, Integer.parseInt(id));
            int deleted = pstmt.executeUpdate();
            showInfo(deleted > 0 ? "Book deleted." : "Book not found.");
        } catch (SQLException | NumberFormatException e) {
            showError("Delete book failed: " + e.getMessage());
        }
    }

    void updateBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Book ID to Update:");
        String newTitle = JOptionPane.showInputDialog(this, "Enter New Title:");
        String newAuthor = JOptionPane.showInputDialog(this, "Enter New Author:");
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE books SET title=?, author=? WHERE id=?")) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newAuthor);
            pstmt.setInt(3, Integer.parseInt(id));
            int updated = pstmt.executeUpdate();
            showInfo(updated > 0 ? "Book updated." : "Book not found.");
        } catch (SQLException | NumberFormatException e) {
            showError("Update book failed: " + e.getMessage());
        }
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}
