## Contributors

- Balutoiu Ana-Maria
- Marinescu Teodor
- Radu George-Alexandru

## PokéVault

Welcome to PokéVault, the ultimate Pokémon card auction platform! Buy, sell, and bid on rare and valuable Pokémon cards in real time. Track live auctions, place competitive bids, and watch prices update instantly. Whether you're a collector or an investor, find the perfect card to add to your collection!


## Figma Wireframe
https://www.figma.com/design/P6w4GojQWri6ov1JwVw0By/Wireframe?node-id=0-1&t=kQbDhdp4MGavD2DM-1

## Trello
https://trello.com/invite/b/6817b5b05ed6e0ae381d9cfc/ATTI383f3953768b9cb253d7af4cc41a5ed29E2C37E1/user-stories


## Features

- **User Authentication**: Secure registration and login system
- **Card Management**: Add, view, edit, and delete your Pokémon cards
- **Auction System**: Create auctions for your cards and set starting prices
- **Bidding**: Place bids on active auctions with real-time updates
- **Collection Management**: Track your card collection and manage your inventory
- **Search & Filter**: Find cards by name, type, or rarity
- **Responsive Design**: Accessible from desktop and mobile devices

## Tech Stack

- **Backend**: Spring Boot 3.4.5, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf templates with HTML/CSS
- **Database**: MariaDB 10.11
- **Containerization**: Docker and Docker Compose
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- Docker and Docker Compose (for containerized deployment)
- MariaDB (for local development)

### Local Development

1. Clone the repository
```
git clone https://github.com/your-username/pokevault.git
cd pokevault
```

2. Configure the database connection in `src/main/resources/application.properties` if needed

3. Build and run the application
```
./mvnw spring-boot:run
```

4. Access the application at `http://localhost:8080`

### Docker Deployment

1. Build and start the containers
```
docker-compose up -d
```

2. Access the application at `http://localhost:8080`

## Usage Guide

1. **Registration**: Create an account using the registration form
2. **Login**: Access your account using your credentials
3. **Add Cards**: Populate your collection by adding your Pokémon cards
4. **Create Auctions**: Select cards from your collection to put up for auction
5. **Browse Auctions**: Explore active auctions to find cards to bid on
6. **Place Bids**: Participate in auctions by placing bids

## Project Structure

- `src/main/java/com/example/pokevault`: Main application code
  - `config`: Configuration classes for security, database, and scheduling
  - `controller`: MVC controllers handling HTTP requests
  - `model`: Domain entities (User, Card, Auction, Bid)
  - `repository`: Database access interfaces
  - `service`: Business logic implementations
- `src/main/resources`: Configuration and templates
  - `templates`: Thymeleaf HTML templates for the UI
