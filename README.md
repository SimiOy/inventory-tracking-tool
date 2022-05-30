Inventory tracking web application with backend Spring Boot
===

## Run the application on Repl.it:
https://replit.com/@SimiSemi/inventory-tracking-tool#README.md
## How to run
1) The application runs from the built .jar file. In the shell type:
```$ java -jar target/inventoryTrackingTool-0.0.1-SNAPSHOT.jar```
2) The web app is now live on localhost:8080. It is accessible from replit's built in Mozzilla browser.
## If this doesn't directly work, then:
1) Sometimes replit won't open the browser by default so what I found to be useful is the following: First cd into ```inventory_tracking_frontend```, and run ```$ npm start```. This only runs the front-end but it will make replit open up the mozzilla browser. Keep it open, go back to the shell and interrupt the current process. $```cd ..``` back into the main folder and run the same command as in 1), and the webb app should be live on localhost:8080.
2) If by any chance the built .jar file is missing from the target folder, then (in the main project directory) type in the shell: ```$ mvn clean install```, and it will generate it (this might take a few mins). After it finished, type again the same command as in 1) to start the application.

## About the app

# Structure
1) The backend is built using Spring Boot in Java and the database is a simple local storage H2 database. The frontend is built using React and typescript.

# Design choices
# Checkstyle
