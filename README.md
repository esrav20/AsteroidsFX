To launch this project.



Master branch:

Run: mvn clean install

From the root run: mvn exec:exec



Spring branch:

Run: mvn clean install

From the root run: mvn spring-boot:run -pl Core



ScoreSystem branch:

Run: mvn clean install

From the ScoreService module run: mvn spring-boot:run 

then, from root/Core run: mvn spring-boot:run -pl Core 

results will show in the console.
