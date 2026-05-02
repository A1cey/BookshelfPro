# Bookshelf Pro

## Run the application

This project uses Spring Boot and relies on Docker for its PostgreSQL database.
Thanks to the `spring-boot-docker-compose` dependency, you do not need to manually start the Docker containers.
Spring Boot will start them automatically if they aren't already running.

The main entry point is `BookshelfProApplication` in the `bookshelf-pro-plugins` module.

### Using Maven (Command Line)

It is recommend to use the included Maven Wrapper (`mvnw` for Linux/macOS, `mvnw.cmd` for Windows) instead of a globally installed `mvn`.

**Prerequisites:** Ensure Docker is running on your machine.

#### Step 1 — Build and install all modules

This is required on first checkout and after any changes to the `bookshelf-pro-domain` or `bookshelf-pro-application` modules.
The flags skip tests and static analysis for a faster local build.

**Linux/macOS:**

```bash
./mvnw clean install -DskipTests -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

**Windows (CMD):**

```cmd
mvnw.cmd clean install -DskipTests -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean install -DskipTests "-Dcheckstyle.skip=true" "-Dpmd.skip=true" "-Dspotbugs.skip=true"
```

#### Step 2 — Run the application

**Linux/macOS:**

```bash
./mvnw spring-boot:run -pl bookshelf-pro-plugins -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

**Windows (CMD):**

```cmd
mvnw.cmd spring-boot:run -pl bookshelf-pro-plugins -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run -pl bookshelf-pro-plugins "-Dcheckstyle.skip=true" "-Dpmd.skip=true" "-Dspotbugs.skip=true"
```

After the initial install, you only need to re-run Step 1 if you change code in the `domain` or `application` modules.
Changes to `plugins` alone are picked up by Step 2.

### Using an IDE (e.g., IntelliJ IDEA)

1. Ensure Docker is running on your machine.
2. Open the project as a Maven project.
3. Run or Debug `BookshelfProApplication` (in `bookshelf-pro-plugins`) directly.

#### Important IDE Configuration (jMolecules & ByteBuddy)

This project uses `jmolecules-bytebuddy-nodep` via the `byte-buddy-maven-plugin` in the domain layer to automatically generate Spring
annotations from jMolecules annotations (like adding the Spring `@Repository` annotation to classes using the jMolecule `@Repository`
annotation).

IDEs do not execute Maven plugins during their internal builds, so skipping this step causes missing Spring beans at startup.

**To fix this in IntelliJ IDEA:**

1. Open the Maven tool window (View → Tool Windows → Maven).
2. Expand `bookshelf-pro-domain` → Plugins → `byte-buddy` → `byte-buddy:transform-extended`.
3. Right-click `byte-buddy:transform-extended` and select **Execute After Build** and
   **Execute After Rebuild**.

See the [IntelliJ guide](https://www.jetbrains.com/help/idea/work-with-maven-goals.html#trigger_goal)
and the [jMolecules ByteBuddy page](https://github.com/xmolecules/jmolecules-integrations/tree/main/jmolecules-bytebuddy)
for more details.
