package com.mycode.mybatis.model;

public class PlayerHistoryRank extends PlayerHistoryRankKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player_history_rank.score
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    private Integer score;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player_history_rank.nickname
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    private String nickname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column player_history_rank.rankOrder
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    private Integer rankorder;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player_history_rank.score
     *
     * @return the value of player_history_rank.score
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public Integer getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player_history_rank.score
     *
     * @param score the value for player_history_rank.score
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player_history_rank.nickname
     *
     * @return the value of player_history_rank.nickname
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player_history_rank.nickname
     *
     * @param nickname the value for player_history_rank.nickname
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column player_history_rank.rankOrder
     *
     * @return the value of player_history_rank.rankOrder
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public Integer getRankorder() {
        return rankorder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column player_history_rank.rankOrder
     *
     * @param rankorder the value for player_history_rank.rankOrder
     *
     * @mbg.generated Wed Aug 16 11:11:22 CST 2017
     */
    public void setRankorder(Integer rankorder) {
        this.rankorder = rankorder;
    }
}