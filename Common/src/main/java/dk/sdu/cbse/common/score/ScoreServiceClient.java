package dk.sdu.cbse.common.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

@Service
public class ScoreServiceClient {

    private final RestTemplate restTemplate;
    private final String scoreServiceUrl;

    @Autowired
    public ScoreServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.scoreServiceUrl = "http://localhost:8081/api/score";
    }

    public ScoreData addScore(String asteroidSize) {
        try {
            ScoreRequest request = new ScoreRequest("asteroid", asteroidSize);
            ScoreResponse response = restTemplate.postForObject(
                    scoreServiceUrl + "/add",
                    request,
                    ScoreResponse.class
            );

            if (response != null) {
                return new ScoreData(
                        response.getTotalScore(),
                        response.getAsteroidsDestroyed(),
                        response.getCurrentLevel(),
                        response.getPointsAwarded()
                );
            }
        } catch (ResourceAccessException e) {
            // Score service might not be running - that's okay, game should continue
            System.out.println("Score service not available: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error communicating with score service: " + e.getMessage());
        }

        return new ScoreData(0, 0, 1, 0); // Default values if service unavailable
    }

    public ScoreData getCurrentScore() {
        try {
            ScoreResponse response = restTemplate.getForObject(
                    scoreServiceUrl + "/current",
                    ScoreResponse.class
            );

            if (response != null) {
                return new ScoreData(
                        response.getTotalScore(),
                        response.getAsteroidsDestroyed(),
                        response.getCurrentLevel(),
                        0
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting current score: " + e.getMessage());
        }

        return new ScoreData(0, 0, 1, 0);
    }

    public void resetScore() {
        try {
            restTemplate.postForObject(scoreServiceUrl + "/reset", null, ScoreResponse.class);
        } catch (Exception e) {
            System.err.println("Error resetting score: " + e.getMessage());
        }
    }

    // Request DTO
    public static class ScoreRequest {
        private String asteroidType;
        private String size;

        public ScoreRequest() {}

        public ScoreRequest(String asteroidType, String size) {
            this.asteroidType = asteroidType;
            this.size = size;
        }

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

        public int getTotalScore() { return totalScore; }
        public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
        public int getAsteroidsDestroyed() { return asteroidsDestroyed; }
        public void setAsteroidsDestroyed(int asteroidsDestroyed) { this.asteroidsDestroyed = asteroidsDestroyed; }
        public int getCurrentLevel() { return currentLevel; }
        public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
        public int getPointsAwarded() { return pointsAwarded; }
        public void setPointsAwarded(int pointsAwarded) { this.pointsAwarded = pointsAwarded; }
    }

    // Simple data holder
    public static class ScoreData {
        private final int totalScore;
        private final int asteroidsDestroyed;
        private final int level;
        private final int pointsAwarded;

        public ScoreData(int totalScore, int asteroidsDestroyed, int level, int pointsAwarded) {
            this.totalScore = totalScore;
            this.asteroidsDestroyed = asteroidsDestroyed;
            this.level = level;
            this.pointsAwarded = pointsAwarded;
        }

        public int getTotalScore() { return totalScore; }
        public int getAsteroidsDestroyed() { return asteroidsDestroyed; }
        public int getLevel() { return level; }
        public int getPointsAwarded() { return pointsAwarded; }

        public String getDisplayString() {
            return String.format("Score: %d | Destroyed: %d | Level: %d",
                    totalScore, asteroidsDestroyed, level);
        }
    }
}