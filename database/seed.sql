-- Optional sample data for local development or demo use
-- This file is meant to be run after `database/schema.sql`.

USE dreadbots_pm;

INSERT INTO teams (name, slug) VALUES
    ('Dreadbots', 'dreadbots');

INSERT INTO seasons (team_id, name, start_date, end_date, is_active)
VALUES
    (1, '2026 Build Season', '2026-01-05', '2026-03-15', TRUE);

INSERT INTO users (team_id, username, email, password_hash, full_name, role)
VALUES
    (1, 'admin', 'admin@dreadbots.local', '$2a$10$abcdefghijklmnopqrstuv', 'Team Admin', 'ADMIN'),
    (1, 'jdoe', 'jdoe@dreadbots.local', '$2a$10$abcdefghijklmnopqrstuv', 'Jane Doe', 'MEMBER'),
    (1, 'msmith', 'msmith@dreadbots.local', '$2a$10$abcdefghijklmnopqrstuv', 'Michael Smith', 'MEMBER');

INSERT INTO labels (team_id, name, color)
VALUES
    (1, 'CAD', '#8B5CF6'),
    (1, 'Drivetrain', '#F59E0B'),
    (1, 'Software', '#10B981'),
    (1, 'Electrical', '#EF4444'),
    (1, 'Testing', '#06B6D4'),
    (1, 'Safety', '#F43F5E'),
    (1, 'Competition', '#6366F1');

INSERT INTO stories (
    team_id,
    season_id,
    summary,
    description,
    status,
    priority,
    assignee_id,
    reporter_id,
    due_date
)
VALUES
    (
        1,
        1,
        'Design robot intake prototype',
        'Build and test an intake concept that can reliably pick up game pieces in the first field test.',
        'IN_PROGRESS',
        'HIGH',
        2,
        1,
        '2026-02-10'
    ),
    (
        1,
        1,
        'Create autonomous drive routine',
        'Write the basic autonomous path and validate it on the field before the first competition practice.',
        'TO_DO',
        'HIGH',
        3,
        1,
        '2026-02-15'
    ),
    (
        1,
        1,
        'Inventory battery pack components',
        'Verify all battery pack parts are present and documented before the next testing event.',
        'BLOCKED',
        'MEDIUM',
        3,
        1,
        '2026-02-12'
    );

INSERT INTO story_labels (story_id, label_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 7),
    (3, 4),
    (3, 5),
    (3, 6);

INSERT INTO story_comments (story_id, user_id, comment)
VALUES
    (1, 1, 'Need to validate the intake width with actual game pieces before finalizing.');

INSERT INTO story_history (story_id, changed_by, field_name, old_value, new_value)
VALUES
    (1, 1, 'status', 'BACKLOG', 'IN_PROGRESS');

-- NOTE:
-- Replace the example password hashes with real bcrypt hashes before production use.
-- The example values here are placeholders and should not be used in a real deployment.
