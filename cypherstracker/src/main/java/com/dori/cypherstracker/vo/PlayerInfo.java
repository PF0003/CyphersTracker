package com.dori.cypherstracker.vo;

public class PlayerInfo {
	private int rank;
    private String playerId;
    private String nickname;
    private int grade;
    private int ratingPoint;
    private int winCount;
    private int loseCount;
    private int winRate;
    private String clanName;
    private String characterId;
    private String characterName;
    
    public PlayerInfo(int rank, String playerId, String nickname, int grade, int winCount, int loseCount, int winRate, String clanName) {
        this.rank = rank;
        this.playerId = playerId;
        this.nickname = nickname;
        this.grade = grade;
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.winRate = winRate;
        this.clanName = clanName;
    }
    
    public PlayerInfo(int rank, String playerId, String nickname, int grade, int ratingPoint, String clanName, String characterId, String characterName) {
        this.rank = rank;
        this.playerId = playerId;
        this.nickname = nickname;
        this.grade = grade;
        this.ratingPoint = ratingPoint;
        this.clanName = clanName;
        this.characterId = characterId;
        this.characterName = characterName;
    }
    
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getRatingPoint() {
		return ratingPoint;
	}
	public void setRatingPoint(int ratingPoint) {
		this.ratingPoint = ratingPoint;
	}
	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getLoseCount() {
		return loseCount;
	}

	public void setLoseCount(int loseCount) {
		this.loseCount = loseCount;
	}
	
	public int getWinRate() {
		return winRate;
	}

	public void setWinRate(int winRate) {
		this.winRate = winRate;
	}

	public String getClanName() {
		return clanName;
	}
	public void setClanName(String clanName) {
		this.clanName = clanName;
	}
	public String getCharacterId() {
		return characterId;
	}
	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}
	public String getCharacterName() {
		return characterName;
	}
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}
}
