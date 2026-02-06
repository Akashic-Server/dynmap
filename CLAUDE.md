# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Dynmap is a dynamic web mapping plugin/mod for Minecraft servers. It's a multi-platform project supporting Spigot/PaperMC, Forge, and Fabric across multiple Minecraft versions (1.12.2 - 1.21.x).

## Build Commands

```bash
# Build all platforms (requires JDK 21 as default)
./gradlew setup build

# Build outputs go to /target directory

# Build specific module (for faster iteration, but NOT for PR submissions)
./gradlew :fabric-1.18:build

# Forge 1.12.2 (requires JDK 8 - set JAVA_HOME accordingly)
cd oldgradle
./gradlew setup build
```

**JDK Requirements:**
- Default: JDK 21
- Forge 1.12.2 (oldgradle): JDK 8 strictly required
- Runtime targets: JDK 8 (1.16-), JDK 16 (1.17.x), JDK 17 (1.18-1.20.4), JDK 21 (1.20.5+)

## Architecture

### Module Structure (71 modules total)

**Core Shared Modules:**
- `DynmapCoreAPI/` - Stable public API for external plugins/mods (markers, mod support, rendering)
- `DynmapCore/` - Internal shared implementation (NOT stable - subject to breaking changes)
- `dynmap-api/` - Bukkit-specific public API

**Platform Implementations:**
- `spigot/` - Bukkit/PaperMC implementation
- `bukkit-helper-*` - Version-specific NMS code (25 versions: 1.13-1.21)
- `fabric-*` - Fabric mod implementations (14 versions: 1.14.4-1.21.11)
- `forge-*` - Forge mod implementations (14 versions: 1.14.4-1.21.11)

### Dependency Flow
```
External Plugins/Mods
    ↓
DynmapCoreAPI (stable, published to repo.mikeprimm.com)
    ↓
DynmapCore (internal, unstable)
    ↓
Platform-specific modules (Spigot, Fabric, Forge)
```

### Key Components in DynmapCore
- `MapManager` - Tile/map rendering orchestration
- `DynmapCore.java` - Main coordination hub (~3,100 lines)
- `storage/` - Storage backends (FileTree, MySQL, PostgreSQL, SQLite, S3)
- `hdmap/` - HD map rendering (block models, shaders, textures)
- `web/` - Embedded Jetty server with servlets
- `markers/` - Marker system implementation

## Critical Contribution Rules

**PRs must build and test on ALL platforms including oldgradle. Changes to DynmapCore/DynmapCoreAPI require testing on all platforms.**

- **Java 8 compatibility required** - Code must compile and run on Java 8
- **Java only** - No Kotlin, Scala, or other JVM languages
- **No dependency updates** - Library versions are tied to platform compatibility
- **No platform-specific code** - Must work on Windows, Linux (x86/ARM), macOS, Docker
- **Small PRs only** - One feature per PR, no style/formatting changes
- **No mod-specific code** - Use Dynmap APIs instead; external mods should depend on DynmapCoreAPI
- **Apache License v2** - All code must be compatible

## Testing

No automated tests exist. Verification is done by:
1. Building all platforms successfully (`./gradlew setup build` AND `cd oldgradle && ./gradlew setup build`)
2. Manual testing on target Minecraft server platforms

## Storage Backends

Dynmap supports: FileTree (default), MySQL/MariaDB, PostgreSQL, SQLite, MS SQL Server, AWS S3
