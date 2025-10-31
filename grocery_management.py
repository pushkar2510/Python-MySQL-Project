import mysql.connector

# Connect to the database
cnx = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="grocery_management")
cursor = cnx.cursor()

#Updates the credit of a customer in the Credit table.
def update_customer_credit(customer_id, amount):
    query = "UPDATE Credit SET credit = credit + {} WHERE customer_id = {}".format(amount, customer_id)
    cursor.execute(query)
    cnx.commit()
 
 
 #Updates the quantity of a product in the Products table.
def update_product_quantity(product_id, quantity):
    query = "UPDATE Products SET quantity = quantity - {} WHERE product_id = {}".format(quantity, product_id)
    cursor.execute(query)
    cnx.commit()

 
 #Makes a transaction and updates the relevant tables.
def make_transaction(customer_id, product_id, Transaction_type, quantity,price):
   
    # Insert the transaction into the Transactions table
    query = "INSERT INTO Transactions (customer_id, product_id, Transaction_type,Amount) VALUES ({}, {}, '{}',{})".format(customer_id, product_id, Transaction_type,price)
    cursor.execute(query)
    cnx.commit()
   
    # Update the customer's credit if the transaction was made on credit
    if Transaction_type == "credit":
        # Get the price of the product
        query = "SELECT price FROM Products WHERE product_id = {}".format(product_id)
        cursor.execute(query)
        result = cursor.fetchone()
        price = result[0]
        
        # Calculate the amount to add to the customer's credit
        amount = price * quantity
        update_customer_credit(customer_id, amount)
    
    # Update the product's quantity
    update_product_quantity(product_id, quantity)


 #Prints all records from a given table.
def display_records(table_name,cd1=None,cd2=None):
    if cd1==None:
      query = "SELECT * FROM {}".format(table_name.capitalize())
    else:
      query= "SELECT * FROM {} WHERE {}={}".format(table_name.capitalize(),cd1,cd2)
    cursor.execute(query)
    results = cursor.fetchall()
    if cd1==None:
    # Print the table name
     print("\n{}:".format(table_name))
    # Print the column names
    column_names = [col[0].capitalize() for col in cursor.description]
    print("  ".join(column_names))
    # Print the records
    for result in results:
        print("     ".join([str(field) for field in result]))


#Updates the rewards points of a customer in the Rewards table
def update_rewards(customer_id,reward_id, points):
   a=customer_id
   if("SELECTS EXISTS(SELECT * FROM Rewards where customer_id=a);"==1):
     query = "UPDATE Rewards SET points = points + {} WHERE customer_id = {}".format(points, customer_id)
     cursor.execute(query)
     cnx.commit()
   else:
     query="INSERT INTO Rewards(customer_id,reward_id,points) VALUES ({},{},{})".format(customer_id,reward_id,points)
     cursor.execute(query)
     cnx.commit()

#Adds a customer to the Customers table.
def add_customer():
  #Get the customer's first and last name
  first_name = input("Enter the customer's first name: ")
  last_name = input("Enter the customer's last name: ")
  # Get the customer's email and phone number
  email = input("Enter the customer's email: ")
  phone = input("Enter the customer's phone number: ")
  # Get the customer's address
  address = input("Enter the customer's address: ")
  print("Customer added")
  query = "INSERT INTO Customers (first_name, last_name, email, phone, address) VALUES ('{}', '{}', '{}', '{}', '{}')".format(first_name, last_name, email, phone, address)
  cursor.execute(query)
  cnx.commit()


#Adds a vendor to the Vendors table.#
def add_vendor():
  name = input("Enter the vendor's name: ")
  # Get the vendor's phone number and email
  phone = input("Enter the vendor's phone number: ")
  email = input("Enter the vendor's email: ")
  # Get the vendor's address
  address = input("Enter the vendor's address: ")
  # Add the vendor
  query = "INSERT INTO Vendors (name, phone, email, address) VALUES ('{}', '{}', '{}', '{}')".format(name, phone, email, address)
  cursor.execute(query)
  cnx.commit()
  print("Vendor added")


 #Adds a product to the Products table.
def add_product(name, vendor_id, price, quantity):
 query = "INSERT INTO Products (name, vendor_id, price, quantity) VALUES ('{}', {}, {}, {})".format(name, vendor_id, price, quantity)
 cursor.execute(query)
 cnx.commit()


#Adds a coupon to the Coupons table.
def add_coupon(code, discount, expiration_date):
 query = "INSERT INTO Coupons (code, discount, expiration_date) VALUES ('{}', {}, '{}')".format(code, discount, expiration_date)
 cursor.execute(query)
 cnx.commit()


 #Updates a field in the Customers table for a given custome.
def update_customer(customer_id, field, new_value):
 query = "UPDATE Customers SET {} = '{}' WHERE customer_id = {}".format(field, new_value, customer_id)
 cursor.execute(query)
 cnx.commit()


#Updates a field in the Vendors table for a given vendor.
def update_vendor(vendor_id, field, new_value):
 query = "UPDATE Vendors SET {} = '{}' WHERE vendor_id = {}".format(field, new_value, vendor_id)
 cursor.execute(query)
 cnx.commit()


#Updates a field in the Products table for a given product.#
def update_product(product_id, field, new_value):
 query = "UPDATE Products SET {} = '{}' WHERE product_id = {}".format(field, new_value, product_id)
 cursor.execute(query)
 cnx.commit()


 #Updates a field in the Coupons table for a given coupon.
def update_coupon(coupon_id, field, new_value):
 query = "UPDATE Coupons SET {} = '{}' WHERE coupon_id = {}".format(field, new_value, coupon_id)
 cursor.execute(query)
 cnx.commit()


#Deletes a customer from the Customers table.
def delete_customer(customer_id):
 query = "DELETE FROM Customers WHERE customer_id = {}".format(customer_id)
 cursor.execute(query)
 cnx.commit()


#Deletes a vendor from the Vendors table.
def delete_vendor(vendor_id):
 query = "DELETE FROM Vendors WHERE vendor_id = {}".format(vendor_id)
 cursor.execute(query)
 cnx.commit()


#Deletes a product from the Products table.
def delete_product(product_id):
 query = "DELETE FROM Products WHERE product_id = {}".format(product_id)
 cursor.execute(query)
 cnx.commit()


#Deletes a coupon from the Coupons table.
def delete_coupon(coupon_id):
 query = "DELETE FROM Coupons WHERE coupon_id = {}".format(coupon_id)
 cursor.execute(query)
 cnx.commit()
 


 #Displays the main menu.
def display_menu():
 print("\nMenu:")
 print("1.Make a transaction",end="\t"*2)
 print("2.Display records")
 print("3.Update rewards points",end="\t"*2)
 print("4.Add a customer")
 print("5.Add a vendor",end="\t"*3)
 print("6.Add a product")
 print("7.Add a coupon",end="\t"*3)
 print("8.Update a customer")
 print("9.Update a vendor",end="\t"*2)
 print("10.Update a product")
 print("11.Update a coupon",end="\t"*2)
 print("12.Delete a customer")
 print("13.Delete a vendor",end="\t"*2)
 print("14.Delete a product")
 print("15.Delete a coupon",end="\t"*2)
 print("16.Quit",end="\n"*2)


def main():
 while True:
    display_menu()
    choice = input("Enter your choice: ")
   #If-else ladder
    if choice == "1":
        display_records("Customers")
        print()
        k=(input("Does the customer already exist (Y/N) ").upper()=='N')
        print()
        if(k):
           add_customer()
           print()
           display_records("customers")
           print()
        customer_id = input("Enter the customer ID: ")
        # Get the product ID
        display_records("Products")
        print()
        product_id=int(input("Enter the product ID: "))
        # Get the transaction type
        Transaction_type = input("Enter the transaction type (credit or other): ")
        # Get the quantity
        quantity =int(input("Enter the quantity: "))
        q="SELECT quantity from Products WHERE product_id={}".format(product_id)
        cursor.execute(q)
        q=(cursor.fetchall())
        if(quantity>int(q[0][0])):
            print("Sorry the avaiable quanity is not sufficient ")
        else :
          # Make the transaction
          query = "SELECT price FROM Products WHERE product_id = {}".format(product_id)
          cursor.execute(query)
          result = cursor.fetchone()
          price = int(result[0])
          make_transaction(customer_id, product_id, Transaction_type, quantity,price)
          other="SELECT Transaction_id FROM Transactions WHERE customer_id={} AND Amount={}".format(customer_id,price)
          cursor.execute(other)
          other=cursor.fetchall()
          display_records("Transactions",int(other[0][0]),"Transaction_id")
          print("Receipt",end="\n"*2)
 

    elif choice == "2":
        # Get the table names
        print("Available Tables:")
        print("1.Customers",end="\t"*3)
        print("2.Credit")
        print("3.Vendors",end="\t"*3)
        print("4.Products")
        print("5.Transactions",end="\t"*3)
        print("6.Coupons")
        print("7.Rewards")
        table_name = input("Enter the table name: ").capitalize()
        # Display the records
        display_records(table_name)
    
    elif choice == "3":
         #Get the customer id
        display_records("Customers")
        print()
        customer_id = input("Enter the customer ID: ")
        # Get the reward ID
        reward_id=input("Enter the reward ID:")
        # Get the points to add
        points = input("Enter the points to add: ")
        # Update the rewards points
        update_rewards(customer_id,reward_id, points)
        print("Rewards points updated")
    
 
    elif choice == "4":
      add_customer()
      
    
    elif choice == "5":
      add_vendor()


    elif choice == "6":
        # Get the product's name
        name = input("Enter the product's name: ")
        #Display Available vendors
        display_records("Vendors")
        print()
        #Check if vendors exists
        k=(input("Does the product vendor already exist (Y/N) ").upper()=='N')
        print()
        if(k):
             add_vendor()
             display_records("Vendors")
             print()
        vendor_id = input("Enter the ID of the product Vendor: ")
        # Get the product's price and quantity
        price = int(input("Enter the product's price: "))
        quantity = int(input("Enter the product's quantity:"))
        # Add the product
        add_product(name, vendor_id, price, quantity)
        print("Product added")
 

    elif choice == "7":
        # Get the coupon code
        code = input("Enter the coupon code: ")
        # Get the discount amount
        discount = input("Enter the discount amount: ")
        # Get the expiration date
        expiration_date = input("Enter the expiration date(YYYY-MM-DD): ")
        # Add the coupon
        add_coupon(code, discount, expiration_date)
        print("Coupon added")
    
    elif choice == "8":
       #Display available records
        print("Customers")
        display_records("Customers")
        # Get the customer ID
        customer_id = input("Enter the customer ID: ")
        # Get the field to update
        field = input("Enter the field to update: ")
        # Get the new value
        new_value = input("Enter the new value: ")
        # Update the customer
        update_customer(customer_id, field, new_value)
        print("Customer updated")
    
   
    elif choice == "9":
        #Display available records
        print("Vendors")
        display_records("Vendors")
        # Get the vendor ID
        vendor_id = input("Enter the vendor ID: ")
        # Get the field to update
        field = input("Enter the field to update: ")
        # Get the new value
        new_value = input("Enter the new value: ")
        # Update the vendor
        update_vendor(vendor_id, field, new_value)
        print("Vendor updated")
    
    
    elif choice == "10":
       #Display available records
        print("Products")
        display_records("Products")
        # Get the product ID
        product_id = input("Enter the product ID: ")
        # Get the field to update
        field = input("Enter the field to update: ")
        # Get the new value
        new_value = input("Enter the new value: ")
        # Update the product
        update_product(product_id, field, new_value)
        print("Product updated")
    
    
    elif choice == "11":
       #Display available records
        print("Coupons")
        display_records("Coupon")
        # Get the coupon ID
        coupon_id = input("Enter the coupon ID: ")
        # Get the field to update
        field = input("Enter the field to update: ")
        # Get the new value
        new_value = input("Enter the updated coupan")
        update_coupon(coupon_id, field, new_value)
        print("Coupon updated")
    
   
    elif choice == "12":
       #Display available records
        print("Customers")
        display_records("Customers")
        # Get the customer ID
        customer_id = input("Enter the customer ID: ")
        # Delete the customer
        delete_customer(customer_id)
        print("Customer deleted")
    
    
    elif choice == "13":
       #Display available records
        print("Vendors")
        display_records("Vendors")
        # Get the vendor ID
        vendor_id = input("Enter the vendor ID: ")
        # Delete the vendor
        delete_vendor(vendor_id)
        print("Vendor deleted")
    
    
    elif choice == "14":
        #Display available records
        print("Products")
        display_records("Products")
        # Get the product ID
        product_id = input("Enter the product ID: ")
        # Delete the product
        delete_product(product_id)
        print("Product deleted")
        
   
    elif choice == "15":
        #Display available records
        print("Coupons")
        display_records("Coupons")
        # Get the coupon ID
        coupon_id = input("Enter the coupon ID: ")        
        # Delete the coupon
        delete_coupon(coupon_id)
        print("Coupon deleted")   
   
   
    elif choice == "16":
        # Quit the program
           break
    else:
        print("Invalid choice")

main()
#Close the cursor and connection
cursor.close()
cnx.close()
