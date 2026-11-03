# MySQL desktop workflow

The desktop application reads and writes persistent data through Hibernate and MySQL.
`DuomBaze` and automatic demo-account creation have been removed.

## Starting the application

1. Start MySQL and verify `src/main/resources/META-INF/persistence.xml` contains the connection settings.
2. Rebuild the project in IntelliJ, then run `main.Launcher`.
3. Open **Register**. If no administrator exists, the form explicitly creates the initial administrator.
4. Sign in as that administrator. Use **Create account** to create clients, veterinarians, groomers or additional administrators.
5. Subsequent public registration creates client accounts only.

The existing `hibernate.hbm2ddl.auto=update` setting adds the nullable `blocked` and `cancelled` columns.
No data is intentionally deleted by application startup. User inheritance remains `SINGLE_TABLE`.

## Access

| Role | Desktop actions |
| --- | --- |
| Admin | Create accounts, block/unblock other accounts, add/assign pets, view all pets and appointments, book/confirm/cancel appointments |
| Client | Add and view own pets, book for own pets, view/cancel own appointments |
| Vet / Groomer | View and confirm/cancel appointments assigned to that specialist |

Database operations re-read the account and check its role/block status inside a transaction.
Signing out clears the in-memory session. Closing the application also closes the Hibernate factory.

## Validation and persistence

- Required fields reject blank values. Usernames have 3–50 letters/digits/dots/underscores/hyphens and are unique.
- New passwords have 6–128 characters, are confirmed in the UI and are stored as salted PBKDF2-HMAC-SHA256 hashes (600,000 iterations).
- Existing plaintext passwords are accepted only for compatibility and upgraded on successful login; existing accounts are not recreated or reset.
- International phone numbers use `+` followed by 7–15 digits; client email addresses are validated.
- Pet species is required; age must be nonnegative and weight positive and finite.
- Bookings require a future `YYYY-MM-DD` / `HH:mm`, an owned pet and an active specialist.
- Row locks protect assignment/booking/state changes. A pet or specialist cannot have two active bookings at the same start time.
- Cancellation is stored separately from confirmation; cancelled bookings cannot be confirmed again.
- A database failure rolls back the transaction and displays an error rather than a false success or invalid-password message.

Bookings currently occupy a single start time. Service durations, business hours, calendars, messaging, reviews,
REST and Android functionality described in the project README remain separate future features.
