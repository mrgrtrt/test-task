## Test task

### Used tools
- Rest Assured
- Junit5
- Gradle
- Test containers to automatically run the application under test

### Run tests
./gradlew test

### CI + Report
Github Actions used for CI - on each push main branch test run is triggered.

Gradle report is used for reporting.
You can find test reports as zip artifact in particular build. 

Github Pages is also added, but it seems there are some troubles with it, 
because it does not deploy pages from gh-pages branch into the site :(
