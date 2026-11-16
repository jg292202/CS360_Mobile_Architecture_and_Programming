# Android Inventory Management Application

**Java • Android Studio • SQLite • RecyclerView • SMS Notifications**

An Android inventory-management application developed as part of **CS 360: Mobile Architecture and Programming** at Southern New Hampshire University.

This repository preserves the application substantially as it was completed during the course. Rather than retroactively rebuilding the project around skills I developed later, I am presenting it as a learning artifact that demonstrates my mobile-development foundation and documents how I would improve the application today.

## Project Overview

The application provides a mobile interface for maintaining a locally persisted inventory.

Users can:

- Create a local account and log in
- View inventory stored in SQLite
- Add inventory items
- Delete inventory items
- Increase and decrease item quantities
- Prevent quantities from being decremented below zero
- Receive an SMS low-stock notification when an item's quantity reaches zero

The application uses multiple Android activities, XML layouts, a RecyclerView-based inventory interface, and a local SQLite database.

## Application Architecture

The application separates major responsibilities across several Java classes.

### `LoginActivity`

Handles user login and registration workflows and delegates account persistence and credential validation to the database helper.

### `InventoryActivity`

Displays inventory through a `RecyclerView`, loads persisted inventory from SQLite, and provides navigation to the item-creation screen.

### `AddItemActivity`

Provides the interface for creating inventory records and performs basic validation before saving inventory data.

### `ItemAdapter`

Connects inventory data to the RecyclerView interface and handles item-level interactions, including:

- Item deletion
- Quantity increases
- Quantity decreases
- Low-stock detection
- SMS notification behavior

### `AppDatabaseHelper`

Extends Android's `SQLiteOpenHelper` and manages local persistence for:

- User accounts
- Inventory items
- User registration
- Credential validation
- Item creation
- Quantity updates
- Item deletion
- Inventory retrieval

### `SMSPermissionActivity`

Supports the application's SMS permission workflow for low-stock notifications.

## Data Persistence

The application uses a local SQLite database containing separate tables for user accounts and inventory items.

Inventory records contain:

- Item ID
- Name
- SKU
- Category
- Quantity

Database operations are separated from the main Activities through `AppDatabaseHelper`, allowing UI components to interact with persisted data through a dedicated data-access class.

## Inventory Workflow

A typical inventory interaction follows this flow:

1. The user logs in to the application.
2. `InventoryActivity` retrieves persisted inventory from SQLite.
3. Inventory records are displayed through a `RecyclerView` and `ItemAdapter`.
4. Users can add, delete, increase, or decrease inventory.
5. Database changes are persisted through `AppDatabaseHelper`.
6. When an item's quantity reaches zero, the application can trigger an SMS low-stock notification when the required permission is available.

## Input Validation

The original application includes basic validation for:

- Required login and registration fields
- Required inventory fields
- Numeric quantity input
- Prevention of decrementing inventory below zero

These checks provided an introduction to maintaining valid application state at the UI and persistence boundaries.

## Security & Production Considerations

This application was created as an academic mobile-development project and **should not be considered production-ready**.

Reviewing the original implementation today with a stronger software-engineering and security perspective reveals several areas I would address before developing a production version.

### Credential Storage

The academic implementation stores account passwords directly in the local SQLite database.

A production application should **never store plaintext passwords**.

A future implementation would replace this design with an appropriate credential-protection strategy, such as secure password hashing and verification or a dedicated authentication/identity solution appropriate to the application's architecture.

### Input and Domain Validation

The application validates required fields and numeric quantity input, but additional validation should enforce stronger domain rules.

Examples include:

- Rejecting negative starting quantities
- Applying appropriate SKU constraints
- Defining length and format requirements
- Validating inventory data beyond basic field presence

### Notification Configuration

The academic application uses a predefined emulator/test SMS destination for demonstrating low-stock notifications.

A production implementation should move notification configuration outside application logic and allow the destination and notification behavior to be configured appropriately.

### Database Migrations

The initial database implementation uses a simple drop-and-recreate strategy when upgrading the schema.

A production application should use explicit schema migrations capable of preserving user and inventory data across application versions.

### Application Architecture

A future version would further separate:

- UI concerns
- Business/domain logic
- Persistence
- Authentication
- Notification behavior

This would improve maintainability, testability, and long-term scalability.

### Automated Testing

The project contains the default Android test structure, but the original application does not contain the level of automated testing I would expect from a production application.

Future development would include dedicated tests for:

- Database operations
- Authentication behavior
- Input validation
- Inventory state transitions
- Failure conditions
- Permission-dependent behavior

## What I Learned

This project introduced me to building a complete mobile application whose interface, application logic, persistence layer, and Android platform capabilities must work together.

The project gave me practical experience with:

- Android application structure
- Activity lifecycle concepts
- Java-based Android development
- XML interface layouts
- RecyclerView and adapter patterns
- SQLite persistence
- CRUD operations
- Input validation
- Runtime permissions
- SMS integration
- Navigation between activities

Just as importantly, revisiting this project later demonstrates how my engineering perspective has evolved.

I can now identify design, security, testing, and maintainability decisions that I would approach differently in a newer application. That ability to evaluate previous work and identify concrete improvements is an important part of my continued development as a software engineer.

## Technologies & Concepts

- Java
- Android Studio
- Android SDK
- SQLite
- `SQLiteOpenHelper`
- RecyclerView
- XML Layouts
- Activities
- CRUD Operations
- Local Data Persistence
- Runtime Permissions
- SMS Notifications
- Input Validation
- Object-Oriented Programming

## Repository Structure

The repository contains the complete Android Studio project rather than a compiled application or packaged archive.

Key source files include:

- `AddItemActivity.java`
- `AppDatabaseHelper.java`
- `InventoryActivity.java`
- `Item.java`
- `ItemAdapter.java`
- `LoginActivity.java`
- `SMSPermissionActivity.java`
- `AndroidManifest.xml`
- Android XML layouts and application resources

## Academic Context

Completed as part of **CS 360: Mobile Architecture and Programming** at Southern New Hampshire University.

This repository is presented as a learning and portfolio artifact demonstrating my foundation in Android application development and providing a transparent example of how I evaluate earlier work using the engineering practices I have developed since completing the project.
