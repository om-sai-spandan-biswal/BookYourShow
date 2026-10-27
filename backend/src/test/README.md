Run the complete suite from `backend`:

```bash
mvn test
```

The build loads Mockito as a Java agent so tests work on Java 25 without dynamic self-attachment. H2 is a test-only dependency. The `test` Spring profile uses an in-memory database and a test JWT secret; the application smoke test explicitly activates this profile.

Service unit tests use JUnit 5 and Mockito for all implementations in the auth, booking, movie, show, theater, and user modules, plus `SecurityUserDetailsService`. They exercise successful operations, missing resources, rejected state changes, ownership checks, token validation, and refresh token rotation. The payment module currently has no service implementation or custom repository method.

Repository tests execute actual Spring Data queries using `@DataJpaTest` with rollback after each test. They cover every declared custom or derived repository method; inherited `JpaRepository` operations are exercised through fixtures rather than separately tested. Show tests include overlap boundaries, case-insensitive search, distinct results, pagination, filtering, and ordering. Show-seat tests cover status filtering, requested IDs, and pessimistic locking. `ShowSeatLockingTest` uses two independent transactions to verify blocking and exclusion of an already booked seat.

The method `findAndLockAvailableShowSeats` currently queries **LOCKED** seats, matching the confirmation flow. Tests preserve that predicate. Database tests run against H2; PostgreSQL-specific locking behavior still requires a PostgreSQL integration environment.

Existing behavior is preserved: `deleteScreen` sets `active` to true, and `clearResponseCookie` empties the cookie value while retaining its configured lifetime. Tests document these behaviors without changing production code.
