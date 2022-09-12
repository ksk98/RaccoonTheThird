package com.bots.RaccoonServer.Persistence.UpvotePersistence;

import javax.persistence.*;

@Entity
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String userId;
    private String serverId;

    private int points = 0;

    public UserScore() {

    }

    public UserScore(String userId, String serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }

    public void alterPointsBy(int points) {
        this.points += points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
