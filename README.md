# Casino Slot Video Poker Engine

A Spring Boot-based Video Poker game engine service. The system is designed to support multiple video poker variants (Jacks or Better, Deuces Wild) with a microservices-ready containerized deployment model. The engine provides RESTful APIs for game initialization, card dealing, hand evaluation, and outcome calculations.

The Video Poker Engine is a backend service that manages the core game logic for video poker games. It handles:

Game initialization and session management
Card shuffling and dealing mechanisms
Hand evaluation and ranking
Payout calculation based on winning combinations
Persistent storage of game states and results

## Key Features
* Multi-Variant Support: Supports different poker variants (Jacks or Better, Deuces Wild)
* RESTful API: Complete REST API for game operations
* Persistent Storage: MongoDB integration for game state and historical data
* Docker Ready: Containerized deployment with Docker and Docker Compose
* Monitoring: Spring Boot Actuator for health checks and metrics
* API Documentation: Swagger/OpenAPI integration via SpringDoc

## Technology Stack
| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| Framework | Spring Boot | 3.5.0 | Application framework |
| Language | Java | 21 | Primary language |
| Database | MongoDB | Latest | NoSQL data persistence |
| Web | Spring Web | Latest | REST API support |
| Data | Spring Data MongoDB | Latest | MongoDB integration |
| Documentation | SpringDoc OpenAPI | 2.2.0 | Swagger/OpenAPI docs |
| Testing | JUnit 5 + Mockito | 5.2.0 | Unit testing |
| Build | Maven | Latest | Dependency management |
| Monitoring | Spring Actuator | Latest | Health & metrics |


## Non-Functional Requirements

### Performance

Sub-second response times for game operations
Support for concurrent player sessions
MongoDB indexing for query optimization

### Availability

Health check endpoints via Actuator
Graceful shutdown support
Container restart policies

### Scalability

Stateless API design enabling horizontal scaling
MongoDB replica sets for data redundancy
Load balancing ready

### Maintainability

Modular architecture by game variant
Clear separation of concerns (MVC pattern)
Comprehensive API documentation via Swagger


## Development Tools
- **Build Tool:** Maven (mvnw wrapper included)
- **Container:** Docker & Docker Compose
- **CI/CD Ready:** Dockerfile provided for containerizatio

## REST Endpoints (Expected)
The service exposes RESTful endpoints for game operations:

**Game Initialization**
- `POST /api/game/start` - Initialize a new game session

**Game Actions**
- `POST /api/game/{sessionId}/deal` - Deal initial 5 cards
- `POST /api/game/{sessionId}/hold` - Select cards to hold
- `POST /api/game/{sessionId}/draw` - Draw replacement cards
- `GET /api/game/{sessionId}/status` - Get current game state

**Results & History**
- `GET /api/game/{sessionId}/result` - Get game outcome
- `GET /api/player/{playerId}/history` - Get game history


## Data Model

### Core Entities

**Game Session**
- Session ID (unique identifier)
- Player ID
- Game Status (in-progress, completed, pending)
- Current Hand
- Balance/Credits
- Timestamp

```java
    /**
     * Internal state for each game session.
     * - Only the primary hand (index 0) is drawn at deal; others are empty.
     * - All decks are shuffled and stored; only the primary deck has 47 cards after deal, others still have 52.
     */
    private static class GameState {
        List<List<CardDto>> hands;      // Only primary hand populated in deal, others empty
        List<Deque<CardDto>> decks;     // One deck per hand
        String egmId;
        String uid;
        int coin;
        int numberOfHands;
        double wallet;
        double oldCredit;
        String id;
        String gameStart;
        double betAmount;
        long lastAccessed = System.currentTimeMillis();
    }

    // In-memory storage for active games, keyed by sessionId
    private final Map<String, GameState> games = new java.util.concurrent.ConcurrentHashMap<>();

```

**Card**
- Suit (Hearts, Diamonds, Clubs, Spades)
- Rank (2-10, J, Q, K, A)

```java
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for a playing card")
public class CardDto {
    private String rank;
    private String suit;

    public CardDto() {}
    public CardDto(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getSuit() { return suit; }
    public void setSuit(String suit) { this.suit = suit; }
    
    @Override
    public String toString() {
    	//Take suit's first character and concatenate with rank
		return suit.toLowerCase().charAt(0) + rank;
	}
}
```

**Hand**
- Dealt Cards (5 cards)
- Held Cards
- Discarded Cards
- Final Cards (after discard)
- Hand Rank (Royal Flush, Straight Flush, Four of a Kind, etc.)

**Payout**
- Hand Type
- Multiplier
- Amount
- Bet Amount

## Data Persistence

### 8.1 Database Technology
**MongoDB**
- NoSQL document database
- Flexible schema for game states
- Horizontal scalability

### 8.2 Collections
- `games` - Game session records
- `players` - Player profiles and statistics
- `transactions` - Game transactions and results
- `hand_history` - Detailed hand records

## Service Components

### 7.1 Jacks or Better Module
**Rules Implementation:**
- Minimum hand to win: Pair of Jacks or better
- Payout structure based on hand rankings
- Standard 5-card poker hand evaluation

### 7.2 Deuces Wild Module
**Rules Implementation:**
- Deuces (2s) act as wild cards
- Modified hand rankings
- Adjusted payout tables

**Key Services:**
- `GameService` - Manages game flow and states
- `CardService` - Handles card deck, shuffling, dealing
- `HandEvaluationService` - Evaluates poker hands
- `PayoutService` - Calculates winnings based on hand

## Glossary

* Hand:	The best 5-card poker combination from available cards
* Hold:	Player's selection of which cards to keep in draw phase
* Draw:	Phase where player receives replacement cards
* Payout:	Winnings based on final hand ranking
* Royal Flush:	A, K, Q, J, 10 of same suit - highest hand
* Wild Card:	Card that can substitute for any other card (Deuces Wild)
* Session:	Single game instance from start to completion

## Docker Network Setup for Container Communication

Manually Create Docker Network for Communication Between Containers (One-Time Setup)
Run the following command in the terminal to create a Docker network:

command - docker network create gaming-network
Update the configuration file to ensure the network is mentioned in each docker compose file :

networks:
  gaming-network:
    external: true
    name: gaming-network

```
# Check Docker network
docker network ls | grep gaming-network

# Create if missing
docker network create gaming-network

# Microservices
lsof -i :9080 # Video Poker Engine

# MongoDB
lsof -i :8081 # Mongo Express
lsof -i :27017

# Redis
lsof -i :8082 # Redis Commander
lsof -i :6379

# RabbitMQ
lsof -i :15672 # RabbitMQ Management UI
lsof -i :5672

lsof -i :8080 # Kafaka UI
lsof -i :9090 # Prometheus
lsof -i :3000 # Grafana


```