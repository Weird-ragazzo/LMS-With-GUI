# 📚 Library Management System with GUI

A simple **Java Swing-based Library Management System** using **SQLite** as the backend database.

> Developed by **Dhruv Raghav**

---

## 🧰 Features

- 📖 Add, delete, issue, and return books through a graphical interface  
- 🧑 Track issued status of books  
- 🗃️ Local SQLite database for data storage  
- 🪟 Built with Java Swing  
- 💡 Lightweight and easy to run

---

## 🔧 Requirements

- **Java JDK 8 or above**
- **Any Java IDE** (like IntelliJ IDEA or Eclipse)
- **SQLite JDBC Driver** included (`sqlite-jdbc-3.44.1.0.jar`)

---

## 📂 Folder Structure

```
📁 LMSWithGUI/
 ├── LibraryGUI.java
 ├── LibraryGUI.class
 ├── sqlite-jdbc-3.44.1.0.jar
 ├── slf4j-api-2.0.17.jar
 ├── slf4j-simple-2.0.17.jar
 └── RunCMDS.txt
📄 library.db
```

---

## 🛠️ Setup Instructions

1. **Clone or Download** this repository
2. **Open in your IDE** (e.g., IntelliJ, Eclipse)

### 🗃️ Add Required JARs
- Right-click your project → `Project Structure` or `Build Path`
- Add the following `.jar` files:
  - `sqlite-jdbc-3.44.1.0.jar`
  - `slf4j-api-2.0.17.jar`
  - `slf4j-simple-2.0.17.jar`

> These libraries are required for connecting to SQLite and logging.

---

## 🧪 Running the Application

### Option 1: From IDE
- Open `LibraryGUI.java`
- Run the file as a Java Application

### Option 2: From Command Line

```bash
cd LMSWithGUI
javac -cp ".;sqlite-jdbc-3.44.1.0.jar;slf4j-api-2.0.17.jar;slf4j-simple-2.0.17.jar" LibraryGUI.java
java -cp ".;sqlite-jdbc-3.44.1.0.jar;slf4j-api-2.0.17.jar;slf4j-simple-2.0.17.jar" LibraryGUI
```

> Replace `;` with `:` if you're on macOS/Linux.

---

## 🗄️ Database

- The SQLite database file is named `library.db` and is located in the root folder.
- No setup is required — the database will be accessed directly through JDBC.

---

## 📌 Notes

- Make sure the `.jar` files are added to your classpath.
- The database is persistent — changes will reflect across sessions.

---

## 📃 License

This project is open-source and free to use for learning and educational purposes.

