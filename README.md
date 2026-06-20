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

## Docker Network Setup 

Manually Create Docker Network for Communication Between Containers (One-Time Setup)
Run the following command in the terminal to create a Docker network:

command - docker network create wildace-network
Update the configuration file to ensure the network is mentioned in each docker compose file :

networks:
  wildace-network:
    external: true
    name: wildace-network
