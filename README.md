# 🃏 Duo Card Game - CENG431 Homework 1 IZTECH

<div align="center">
  <img src="https://img.icons8.com/color/96/000000/cards.png" alt="Card Game Icon"/>
  <br/>
  A Java implementation of the Duo Card Game with OOP principles, Mediator Pattern, and CSV logging.
</div>

---

## 📋 Table of Contents
- [Game Overview](#-game-overview)
- [Key Features](#-key-features)
- [Project Structure](#-project-structure)
- [Class Structure Overview](#-class-structure-overview)
- [How to Run](#-how-to-run)

---

## 🎮 Game Overview
A multiplayer card game where 2-4 players compete to discard all cards first. The game features:
- **Dynamic player count** (randomly chosen at start).
- **Special action cards** (Reverse, Skip, Wild, etc.) with unique effects.
- **Points system**: First player to reach **500 points** wins.
- **Round-based gameplay** with score tracking via CSV files.

---

## 🔑 Key Features
- **OOP Fundamentals**: Inheritance, polymorphism, abstract classes, and interfaces.
- **Design Patterns**: 
  - **Mediator Pattern** for handling player/card interactions.
- **CSV I/O**: Automatic logging of round results to `Files/game_log.csv`.
- **UML Diagram**: Includes a class diagram in the submission ZIP.
- **Turkish Character Support**: UTF-8 encoding enabled.

---
## Project Structure
- **src/**: Contains all Java source files.
- **docs/**: Contains the Javadoc documentation.
- **Files/**: Folder for CSV log files.
- **.vscode/**: VSCode settings (e.g., UTF8 encoding).
  
---

## 🧩 Class Structure Overview
| Class/Component          | Description                                                                 |
|--------------------------|-----------------------------------------------------------------------------|
| `Card`, `ActionCard`     | Base card classes with color/number attributes. Subclasses for action cards.|
| `Player`                 | Handles player actions (play/draw cards) and hand management.              |
| `Deck`                   | Manages draw/discard piles and card shuffling.                             |
| `GameMediator`           | Mediates interactions between players and game state (turn order, effects).|
| `CSVLogger`              | Writes round results to CSV with columns: Round, Player1, Player2, etc.    |
| `GameRules`              | Validates card plays and enforces game logic.                              |

---

## 🚀 How to Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/G04_CENG431_HW1.git
2. **Run DuoCardGameMain file**
