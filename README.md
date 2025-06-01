To launch this project.


______________________
Master branch:

Run: mvn clean install

From the root run: mvn exec:exec

______________________
Spring branch:

Run: mvn clean install

From the root run: mvn spring-boot:run -pl Core

______________________
ScoreSystem branch:

Run: mvn clean install

From the ScoreService module run: mvn spring-boot:run 

then, from root/Core run: mvn spring-boot:run -pl Core 

results will show in the console.
