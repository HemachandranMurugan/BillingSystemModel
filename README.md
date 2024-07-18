# BillingSystemModel
This system integrates sales tracking, invoice generation, payment monitoring and stock management by maintaining precise and current product records into a unified, user-friendly platform. It also integrates customer management with detailed profiles including contact details, purchase histories, and preferences, thereby enabling personalized service and strengthened customer relations.Together, it offers a cohesive system that enhances efficiency, accuracy, and customer satisfaction in store management.

## Features

- **Admin Frame:**
  - Add new product
  - Update product quantity
  - Fetch item sales analysis
  - Get BIll Summary by date
  - Get Customer Report by Customer Id
 
- **Employee Frame:**
  - Generate invoices
  - Fetch all invoices
  - Check customer balance
  - Check paid and unpaid invoices


## Installation

1. Clone the repository:

2. Set up your MySQL database and configure JDBC connection details in the project.

3. Compile and run the application.

## Database Schema
The system uses three main tables: Customer, Product, and Bill.

**Customer Table**
Column Name	Data Type
customer_phoneNo	VARCHAR
customer_name	VARCHAR
membership_points	DOUBLE
customer_balance	DOUBLE

**Product Table**
Column Name	Data Type
product_id	VARCHAR
product_name	VARCHAR
price	DOUBLE
quantity	INT

**Bill Table**
Column Name	Data Type
bill_no	VARCHAR
bill_date	DATE
bill_amt	DOUBLE
bill_discount	DOUBLE
customer_id	VARCHAR


**ItemSales Table**
Column Name	Data Type
product_id	VARCHAR
bill_no	VARCHAR
bill_quantity	INT
  










