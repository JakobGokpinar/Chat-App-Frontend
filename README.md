Chat-App-Frontend
Overview
Chat-App-Frontend is the user interface for a desktop-based chat application, built with Java and JavaFX. This project provides an interactive and visually appealing experience for users to register, log in, manage friends, send instant messages, and more. It connects to a PHP-based backend server (hosted in a separate repository) to handle data storage, user authentication, and real-time messaging functionality.

Features
User Authentication: Register and log in with a username and password, with an option to remember login details.
Friend Management: Search for users, send/accept friend requests, and block unwanted users.
Instant Messaging: Send and receive messages in real-time with friends.
Profile Customization: Upload and display profile photos.
Notifications: View friend requests and unread message alerts.
Multilingual Support: Choose between languages like English, Turkish, and Norwegian.
Responsive UI: A clean, desktop-optimized interface with animations and theme options (e.g., dark mode in development).
Technologies Used
Java: Core programming language for the application logic.
JavaFX: Framework for building the graphical user interface.
PHP Backend: Communicates with the frontend via HTTP requests (backend code in a separate repository).
JSON: Used for data exchange between frontend and backend.
HTTP Requests: Facilitated by HttpURLConnection for server communication.
Getting Started
Prerequisites
Java Development Kit (JDK): Version 8 or higher.
JavaFX: Included with JDK 8, or configure separately for later versions (e.g., OpenJFX).
IDE: Recommended tools like IntelliJ IDEA or Eclipse with JavaFX support.
Backend Server: A running PHP server (e.g., at https://calm-mountain-05477.herokuapp.com/) with the corresponding backend repository deployed.
Installation
Clone the Repository:
bash

Collapse

Wrap

Copy
git clone https://github.com/JakobGokpinar/Chat-App-Frontend.git
cd Chat-App-Frontend
Set Up JavaFX:
If using JDK 8, JavaFX is included. For JDK 11+, download OpenJFX and configure your IDE/project:
Add JavaFX libraries to your project’s module path.
Update VM options: --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml.
