package dk.sdu.cbse.scoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api/score")
public class ScoreServiceApplication {

    private int currentScore = 0;
    private int asteroidsDestroyed = 0;
    private int level = 1;

    public static void main(String[] args) {
        SpringApplication.run(ScoreServiceApplication.class, args);
    }

    @PostMapping("/add")
    public ScoreResponse addScore(@RequestBody ScoreRequest request) {
        // Different point values based on asteroid size
        int points = calculatePoints(request.getAsteroidType(), request.getSize());
        currentScore += points;
        asteroidsDestroyed++;

        // Level progression every 10 asteroids
        level = (asteroidsDestroyed / 10) + 1;

        return new ScoreResponse(currentScore, asteroidsDestroyed, level, points);
    }

    @GetMapping("/current")
    public ScoreResponse getCurrentScore() {
        return new ScoreResponse(currentScore, asteroidsDestroyed, level, 0);
    }

    @PostMapping("/reset")
    public ScoreResponse resetScore() {
        currentScore = 0;
        asteroidsDestroyed = 0;
        level = 1;
        return new ScoreResponse(currentScore, asteroidsDestroyed, level, 0);
    }

    private int calculatePoints(String asteroidType, String size) {
        int basePoints;

        // Different points for different sizes
        switch (size.toLowerCase()) {
            case "large":
                basePoints = 20;
                break;
            case "medium":
                basePoints = 50;
                break;
            case "small":
                basePoints = 100;
                break;
            default:
                basePoints = 10;
        }

        // Bonus points for higher levels
        return basePoints + (level - 1) * 5;
    }

    // Request DTO
    public static class ScoreRequest {
        private String asteroidType = "asteroid";
        private String size = "large";

        public ScoreRequest() {}

        public ScoreRequest(String asteroidType, String size) {
            this.asteroidType = asteroidType;
            this.size = size;
        }

        // Getters and setters
        public String getAsteroidType() { return asteroidType; }
        public void setAsteroidType(String asteroidType) { this.asteroidType = asteroidType; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
    }

    // Response DTO
    public static class ScoreResponse {
        private int totalScore;
        private int asteroidsDestroyed;
        private int currentLevel;
        private int pointsAwarded;

        public ScoreResponse() {}

        public ScoreResponse(int totalScore, int asteroidsDestroyed, int currentLevel, int pointsAwarded) {
            this.totalScore = totalScore;
            this.asteroidsDestroyed = asteroidsDestroyed;
            this.currentLevel = currentLevel;
            this.pointsAwarded = pointsAwarded;
        }

        // Getters and setters
        public int getTotalScore() { return totalScore; }
        public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
        public int getAsteroidsDestroyed() { return asteroidsDestroyed; }
        public void setAsteroidsDestroyed(int asteroidsDestroyed) { this.asteroidsDestroyed = asteroidsDestroyed; }
        public int getCurrentLevel() { return currentLevel; }
        public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
        public int getPointsAwarded() { return pointsAwarded; }
        public void setPointsAwarded(int pointsAwarded) { this.pointsAwarded = pointsAwarded; }
    }
}