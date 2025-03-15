# sniffkill

### A Clojure-based port sniffer & process killer

---

## 🚀 Overview

**SniffKill** is a simple yet powerful CLI tool that allows you to:

- **List all processes** bound to network ports.
- **Identify rogue applications** hogging your system.
- **Kill processes** consuming unnecessary resources.

Built with **Clojure**, SniffKill is designed to be lightweight, efficient, and easy to use.

---

## 🔍 How It Works

- Uses **`lsof`** (Linux/macOS) or **`netstat`** (Windows) to fetch open ports.
- Parses results into structured **Clojure data maps**.
- Displays process details **in an easy-to-read table**.
- Supports **interactive selection** for killing processes.

---

## 🏗️ Roadmap

- [x] kill for macos
- [x] kill for linux
- [ ] kill for windows
- [x] Interactive mode with fzf
- [ ] Logging & history of killed processes

---

🚀 **Sniff. Detect. Kill.**
