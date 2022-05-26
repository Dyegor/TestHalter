To run the application use following steps:

1. cd in cloned directory
2. Build jar using command: `./mvnw clean package -DskipTests`
3. Copy jar in docker directory: `cp target/herdManagement-0.0.1-SNAPSHOT.jar src/main/docker`
4. `cd src/main/docker`
5. start docker compose: `docker compose up`

The application will start on port 8080 and become available to receive requests from user.

A few notes to add:
1. Technically collar and cow are different entities and should be stored 
in different tables. However, since we are only storing the collarId, and 
getting the rest of its data from an external API, I decided to store it 
as a column in the "Cows" table.
2. For the getAll endpoint, we have to make an external call for each collar,
and we can get into a situation where we get several successful responses, 
and then one of them fails. This can be handled in several ways depending 
on the requirements of the frontend. For this test task, I decided to throw
an exception and fail as the most straightforward solution.
3. Redis cache set to 60000 milliseconds (1 min) for each call by collarId.