# PokéVault - Pokémon Card Auction Platform

Welcome to PokéVault, the ultimate Pokémon card auction platform! Buy, sell, and bid on rare and valuable Pokémon cards in real time. Track live auctions, place competitive bids, and watch prices update instantly. Whether you're a collector or an investor, find the perfect card to add to your collection!

## Contributors

- Balutoiu Ana-Maria
- Marinescu Teodor
- Radu George-Alexandru

## Features

- **User Authentication**: Secure login and registration system
- **Card Management**: Add, view, and manage your Pokémon card collection
- **Live Auctions**: Create auctions for your cards with custom starting prices and durations
- **Real-time Bidding**: Place bids and see bid updates instantly
- **WebSocket Integration**: Experience real-time updates without page refreshes
- **Responsive Design**: Works seamlessly on both desktop and mobile devices

## Tech Stack

- **Backend**: Spring Boot, Spring Security, Spring Data JPA, WebSocket
- **Frontend**: React, Bootstrap, SockJS, STOMP
- **Database**: MariaDB
- **Authentication**: JWT-based authentication

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Node.js and npm
- MariaDB server

### Database Setup

1. Create a MariaDB database named `pokevault`:

```sql
CREATE DATABASE pokevault;
```

2. Create a database user (or use an existing one) with permissions to the database:

```sql
CREATE USER 'pokevault_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON pokevault.* TO 'pokevault_user'@'localhost';
FLUSH PRIVILEGES;
```

### Backend Setup

1. Navigate to the backend directory:

```bash
cd d:\wp02\wp02_project\pokevault
```

2. Update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/pokevault
spring.datasource.username=pokevault_user
spring.datasource.password=your_password
```

3. Build and run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The backend server will start on port 8080.

### Frontend Setup

1. Navigate to the frontend directory:

```bash
cd d:\wp02\wp02_project\pokevault-frontend
```

2. Install dependencies:

```bash
npm install
```

3. Start the React development server:

```bash
npm start
```

The frontend application will run on port 3000. Access it at http://localhost:3000.

## Usage Guide

### Registration and Login

1. Access the application at http://localhost:3000
2. Click "Sign Up" to create a new account
3. Fill in your username, email, and password
4. After registration, log in with your credentials

### Managing Your Card Collection

1. After logging in, navigate to "My Cards" 
2. Click "Add New Card" to add a Pokémon card to your collection
3. Fill in the card details including name, type, rarity, and image URL
4. View your card collection and card details

### Creating Auctions

1. From your card collection, select a card and click "Create Auction"
2. Set a starting price and auction end time
3. Submit the form to create your auction
4. Your auction will be visible to all users

### Bidding on Auctions

1. Browse active auctions on the "Auctions" page
2. Click on an auction to view details
3. Enter your bid amount (must be higher than the current price)
4. Submit your bid
5. Watch for real-time updates as others bid

### Tracking Auction Results

1. Check "My Auctions" to view your created auctions
2. Completed auctions will show the winning bidder
3. If you win an auction, the card will be added to your collection

## API Documentation

### Authentication Endpoints

- `POST /api/auth/signup`: Register a new user
- `POST /api/auth/signin`: Authenticate and receive JWT token

### Card Endpoints

- `GET /api/cards`: Get all cards
- `GET /api/cards/user`: Get current user's cards
- `GET /api/cards/{id}`: Get card by ID
- `POST /api/cards`: Create a new card
- `PUT /api/cards/{id}`: Update a card
- `DELETE /api/cards/{id}`: Delete a card

### Auction Endpoints

- `GET /api/auctions`: Get all auctions
- `GET /api/auctions/active`: Get active auctions
- `GET /api/auctions/user`: Get current user's auctions
- `GET /api/auctions/{id}`: Get auction by ID
- `POST /api/auctions`: Create a new auction
- `POST /api/auctions/{id}/bid`: Place a bid on an auction
- `GET /api/auctions/{id}/bids`: Get auction bids

### WebSocket Endpoints

- `/ws`: WebSocket connection endpoint
- `/topic/auctions`: Topic for auction updates
- `/topic/auctions/{id}`: Topic for specific auction updates
- `/topic/auctions/{id}/bids`: Topic for specific auction's bids
