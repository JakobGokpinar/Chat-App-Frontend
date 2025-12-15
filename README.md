<p align="center">
    <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" align="center" width="30%">
</p>
<p align="center"><h1 align="center">CHAT-APP-FRONTEND</h1></p>
<p align="center">
	<img src="https://img.shields.io/github/license/JakobGokpinar/Chat-App-Frontend?style=default&logo=opensourceinitiative&logoColor=white&color=0080ff" alt="license">
	<img src="https://img.shields.io/github/last-commit/JakobGokpinar/Chat-App-Frontend?style=default&logo=git&logoColor=white&color=0080ff" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/JakobGokpinar/Chat-App-Frontend?style=default&color=0080ff" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/JakobGokpinar/Chat-App-Frontend?style=default&color=0080ff" alt="repo-language-count">
</p>
<br>

## Chat App Frontend

# ⚠️ ARCHIVED - Legacy Frontend

> **This repository is archived and maintained for historical reference only.**
> 
> **🚀 For the active, modernized version, see:** [Java-ChatApp](https://github.com/JakobGokpinar/Java-ChatApp)
> 
> This legacy frontend connects to the old PHP backend. The new version includes:
> - Modern Spring Boot REST API integration
> - Improved code structure
> - Better dependency management
> - Active development and improvements
> 
> ---
> 

### Original README (JavaFX Frontend - 2020)
This is a messaging application built with Java and JavaFX. Users can register themselves and add their friends on the platform, exactly like any other messaging app like WhatsApp and Messenger. This repository contains the frontend code of the application, aimed for desktop usage purposes.

## Screenshots

### Login Screen
![Login Screen](src/goksoft/chat/app/images/ss/login-screen.png)

### Main Chat Interface
![Main Chat](src/goksoft/chat/app/images/ss/main-chat.png)

### Settings
![Settings](src/goksoft/chat/app/images/ss/settings.png)

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Setup and Installation](#project-setup-and-installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact Information](#contact-information)

## Features
- User Registration and Login
- Adding Friends
- Changing Profile Picture
- Messaging
- Register Controllers

## Technologies Used
- Java
- JavaFX

## Project Structure
```sh
└── Chat-App-Frontend/
    ├── README.md
    ├── chatapp.iml
    ├── out
    │   ├── artifacts
    │   │   ├── chatapp_jar
    │   │   │   └── chatapp.jar
    │   │   └── chatapplication
    │   │       └── chatapplication.jar
    │   └── production
    │       └── chatapp
    │           ├── META-INF
    │           │   └── MANIFEST.MF
    │           └── goksoft
    │               └── chat
    └── src
        ├── META-INF
        │   └── MANIFEST.MF
        └── goksoft
            └── chat
                └── app
                    ├── ContactPanelController.java
                    ├── ControllerRules.java
                    ├── ErrorClass
                    ├── Function.java
                    ├── GUIComponents.java
                    ├── GlobalVariables.java
                    ├── Launcher.java
                    ├── LoginController.java
                    ├── Main.java
                    ├── MainPanelController.java
                    ├── RegisterController.java
                    ├── ServerFunctions.java
                    ├── WarningWindowController.java
                    ├── images
                    ├── stylesheets
                    └── userinterfaces
```

## Project Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JavaFX SDK
- An IDE such as IntelliJ IDEA or Eclipse

### Installation Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/chat-app-frontend.git
    ```
2. Open the project in your IDE.
3. Set up JavaFX SDK in your IDE.
4. Build and run the project.

## Usage
1. Launch the application.
2. Register a new user or log in with existing credentials.
3. Add friends using their username.
4. Start messaging your friends.
5. Change your profile picture from the settings menu.

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact Information
For any questions or support, please send an email to my address.
