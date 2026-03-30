# LittleTrips

LittleTrips is a full-stack trip history simulation application built with a **Spring Boot** backend and a **React + TypeScript** frontend.

The frontend is built with Vite and bundled into the Spring Boot app, so the project can run as a single web application.

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.0.5
- Spring MVC
- Spring REST client
- Lombok
- Jakarta APIs

### Frontend
- React 18
- TypeScript 5.2
- Vite 4
- Redux Toolkit
- React Redux
- React Router
- MUI
- Axios
- Sass

## Project Structure

```plain text
.
├── src/
│   ├── main/
│   │   ├── java/               # Spring Boot backend
│   │   └── resources/
│   │       ├── json/           # Local data files
│   │       └── static/         # Built frontend output
│   └── test/                   # Backend tests
└── tripsim-client/             # React + TypeScript frontend
```


## Features

- Spring Boot backend with MVC controllers and services
- React + TypeScript frontend
- Local JSON-based data loading for users, transactions, and fare tables
- Frontend build integrated into the backend build
- Single application deployment through Spring Boot static resources

## Requirements

- **Java 21**
- **Node.js 18 or later**
- **npm 9 or later**
- **Gradle** via the included wrapper

## Getting Started

### 1. Clone the repository

```shell script
git clone git@github.com:stellarie/littletrips.git
cd tripsim
```


### 2. Run the application

#### Option A: Run the full app through Spring Boot

This builds the frontend and starts the backend:

```shell script
./gradlew bootRun
```


The application is typically available at:

```plain text
http://localhost:8080
```


#### Option B: Run the frontend separately during development

```shell script
cd tripsim-client
npm install
npm run start
```


This starts the Vite development server at
```plain text
http://localhost:5173
```

## Build

To build both backend and frontend:

```shell script
./gradlew build
```

The Gradle build will:

1. Install frontend dependencies
2. Build the frontend
3. Copy the frontend output into Spring Boot static resources
4. Run backend tests

## Frontend Scripts

Inside `tripsim-client/`, the available npm scripts are:

```shell script
npm run start    # Start Vite dev server
npm run lint     # Run ESLint
npm run format   # Format TypeScript, TSX, CSS, and HTML files
npm run build    # Build frontend and copy output into backend static resources
npm run buildWin # Build frontend and copy output into backend static resources (Windows-only)
npm run preview  # Preview production build
```


## Data Files

The app uses JSON files stored in:

```plain text
src/main/resources/json/
```


Included data files:

- `users.json`
- `transactions.json`
- `fare.json`

## Main Backend Components

### Controllers
- `TripController`
- `UserController`
- `FareTableController`
- `PageController`

### Services
- `TripService`
- `UserService`
- `FareService`

### Data Loaders
- `UserLoader`
- `TransactionLoader`
- `FareTableLoader`
- `LocalDBLoader`

## Testing

Run the backend test suite with:

```shell script
./gradlew test
```


## License

This project is licensed under the MIT License.
