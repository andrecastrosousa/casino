# MindSwap Academy Casino
Welcome to the MindSwap Academy Casino! This project aims to create an interactive and enjoyable online casino experience. Players can engage in various mini-games such as poker, blackjack, and roulette. The games will commence when a waitingRoom has at least three clients, who will then be presented with three mini-games to select from.

## Authors

- [@ateresa-silva](https://github.com/ateresa-silva)
- [@duartenunocosta](https://github.com/duartenunocosta)
- [@andrecastrosousa](https://github.com/andrecastrosousa)


## Project overview
The MindSwap Academy Casino is built using Java programming language and incorporates concepts of network programming, concurrent programming, and documentation using JavaDocs.

## Mini Games
The casino offers the following mini-games for players to enjoy:

1. ***Poker***: A classic card game where players bet on their hand's strength and attempt to create the best possible combination of cards to win the pot.

2. ***Blackjack***: Also known as "21," this game requires players to compete against the pokerDealer by reaching a hand value as close to 21 as possible without exceeding it.

3. ***Roulette***: A game of chance where players place bets on a number, color, or group of numbers and wait for the roulette wheel to determine the winner.

## Room Creation and Gameplay
To start a mini-game, a waitingRoom must have a minimum of three clients. Once three clients have joined a waitingRoom, they will be presented with the option to choose one of the available mini-games.

Upon selecting a mini-game, the game will start, and the players will be able to interact with each other and the game interface. The outcome of each game will be determined based on the rules of the specific mini-game chosen.

## Network Programming
The project incorporates network programming concepts to facilitate communication between clients and the server. The server will handle the game logic, manage rooms, and ensure fair gameplay. Clients will connect to the server to participate in the mini-games and exchange information.

## Concurrent Programming
Concurrent programming techniques are employed to ensure smooth and responsive gameplay in a multi-client environment. The project utilizes threads and synchronization mechanisms to handle multiple client requests simultaneously and provide a seamless gaming experience.

## Documentation Generation
The codebase is well-documented using JavaDocs, a documentation generation tool for Java. The JavaDocs provide detailed explanations of classes, methods, and variables, helping developers understand the code and promoting maintainability and collaboration within the development team.

## Getting Started
To get started with the MindSwap Academy Casino, please follow these steps:

- Clone the project repository from GitHub.

- Install the required dependencies and libraries as specified in the project's documentation.

- Build and compile the project using your preferred Java development environment.

- Run the server application and ensure it is successfully listening for client connections.

- Start the client application on multiple devices or within multiple instances to simulate concurrent gameplay.

- Connect the clients to the server and follow the on-screen instructions to join a waitingRoom and select a mini-game.

- Enjoy playing the exciting mini-games at the MindSwap Academy Casino!