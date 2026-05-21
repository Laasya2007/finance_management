Finance Management System

A Java-based desktop application for managing personal finances with features like transaction tracking, reporting, and data visualization.

Features
- User authentication (Login & Registration)
- Add and manage financial transactions
- Monthly reports generation
- Data visualization (Bar charts, Pie charts)
- CSV import functionality
- SMS parsing for transaction extraction

Tech Stack
- Java (Swing)
- JDBC for database connectivity

Project Structure
- UI: Dashboard, Login, Register, AddTransactionUI
- Reports: MonthlyReport, PieChartReport, BarChartReport
- Utilities: DBConnection, CSVImporter, SMSParser

How to Run
1. Compile all files:
   javac *.java

2. Run the application:
   java Login
