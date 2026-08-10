# 🎧 Echoes

> **Every memory has a soundtrack.**

Echoes is a JavaFX-based digital memory vault that lets users preserve meaningful moments through music.

Create a memory, associate it with a song, record the story behind the moment, capture its emotion, date, and location, and revisit those memories through a personalized music-driven experience.

---

## ✨ Features

- 🔐 User registration and login
- 🧠 Create, edit, and delete memories
- 🎵 Music search using the iTunes Search API
- 🖼️ Album artwork integration
- 🎧 Optional audio file attachment
- ❤️ Favorite and unfavorite memories
- 🔎 Search memories
- 📅 Timeline view
- 📊 Memory statistics
- 🏠 Personalized dashboard
- ⚙️ Profile and settings management
- 🌙 Echoes Midnight theme
- ☀️ Soft Day theme
- 🚪 Session management and logout
- 💾 Persistent MySQL storage

---

## 🏗️ Architecture

Echoes follows a layered architecture based on separation of concerns:

```text
┌──────────────────────────────┐
│        JavaFX UI / FXML      │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│         Controllers          │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│           Services           │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│             DAO              │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│          JDBC / MySQL        │
└──────────────────────────────┘
