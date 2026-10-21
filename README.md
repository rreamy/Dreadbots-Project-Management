# Dreadbots Project Management

A lightweight project management system for robotics teams, inspired by Jira but intentionally simplified.

This repository contains the initial MariaDB schema for tracking team work, sprint tasks, labels, and users.

## Goals
- Track team stories/tasks for a robotics season
- Assign work to students and mentors
- Organize work by label and status
- Reset the database each season without a complex project system
- Keep setup simple, maintainable, and easy to host

## Core entities
- Teams
- Seasons
- Users
- Stories
- Labels
- Story-to-label links
- Story comments

## Schema overview

The schema is designed for:
- one team or one season per database instance
- easier reset between competition years
- status tracking for backlog, in-progress, blocked, and completed work
- label-based filtering such as CAD, Electrical, Software, Drivetrain, Testing, Safety, and Competition

## Database technology
- MariaDB
- SQL schema with foreign keys and indexes

## Quick start

1. Create the database:
   ```sql
   CREATE DATABASE dreadbots_pm;
   USE dreadbots_pm;
   ```

2. Run the schema:
   ```bash
   mysql -u root -p dreadbots_pm < database/schema.sql
   ```

3. Optionally load sample data:
   ```bash
   mysql -u root -p dreadbots_pm < database/seed.sql
   ```

## Notes
- The schema assumes one team per database instance for simplicity.
- Passwords are stored as hashed values in the `users` table.
- `story_comments` and `story_history` are included so the app can support lightweight collaboration without full Jira complexity.
