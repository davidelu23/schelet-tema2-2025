# Ticket Management System

A command-line based ticket management system that simulates the workflow of a software development team. It processes commands from a JSON file to manage users (Managers, Developers, Reporters), tickets (Bugs, Feature Requests, UI Feedback), and project milestones.

## Features

*   **User Roles:** Supports different user roles (Manager, Developer, Reporter) with specific permissions.
*   **Ticket Management:** Create, assign, and change the status of various ticket types.
*   **Milestones:** Group tickets into milestones with due dates and track their progress.
*   **Command-based:** Operates by processing a sequence of commands from an input file.

## Project Structure

The project is organized into the following main packages:

*   `main`: Contains the entry point of the application.
*   `commands`: Defines the command objects and the logic for their execution.
*   `models`: Contains the data models for users, tickets, and milestones.
*   `services`: Provides services for managing users, tickets, and milestones.
*   `observers`: Contains the observer interfaces for the observer pattern implementation.

## Implemented Design Patterns

This project leverages four key design patterns to ensure a modular, scalable, and maintainable architecture.

### 1. Singleton Pattern
The Singleton pattern ensures that a class has only one instance and provides a global point of access to it. This is useful for managing shared resources or services.

*   **Implementation:** The core services (`AppService`, `UserService`, `TicketService`, `MilestoneService`, `MapperService`) are implemented as singletons. This guarantees a single, globally accessible instance for each service, providing a centralized point for state management and business logic throughout the application's lifecycle.

### 2. Factory Pattern
The Factory pattern provides an interface for creating objects in a superclass but allows subclasses to alter the type of objects that will be created. It's used to create objects without exposing the complex creation logic to the client.

*   **Implementation:**
    *   `CommandFactory`: Decouples the command parsing logic from the main application. It reads a JSON input and returns the appropriate concrete `BaseCommand` object, abstracting away the details of which command to instantiate.
    *   `TicketFactory` and `MilestoneFactory`: Abstract the creation of different ticket types (e.g., `Bug`, `FeatureRequest`) and milestones. This makes the system easily extensible to new types of tickets or milestones in the future.

### 3. Command Pattern
The Command pattern turns a request into a stand-alone object that contains all information about the request. This transformation lets you parameterize methods with different requests, delay or queue a request's execution, and support undoable operations.

*   **Implementation:** Each action, such as `reportTicket` or `assignTicket`, is a concrete command class that implements a common `Command` interface. This allows the system to queue commands, validate them, and execute them sequentially. It also separates the object that invokes an operation from the one that knows how to perform it.

### 4. Observer Pattern
The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

*   **Implementation:**
    *   `TicketObserver` and `MilestoneObserver`: `User` objects act as observers that subscribe to notifications from the `TicketService` and `MilestoneService` (the subjects).
    *   When a ticket or milestone is created or modified, the corresponding service notifies all registered observers. This keeps the user models updated with the latest system state (e.g., adding a new ticket to a reporter's list) without tightly coupling the user models to the services.

---
Use of AI: Pretify/rearenge readme for firendlier reading experience, debug and test features to catch edge cases and fix checkstyle problems such as magic numbers(im not very creating when it comes to variable names).