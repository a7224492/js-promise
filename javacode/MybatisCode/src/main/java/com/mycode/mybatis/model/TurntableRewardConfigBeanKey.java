package com.mycode.mybatis.model;

import java.util.Date;

public class TurntableRewardConfigBeanKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column turntable_reward_config.rewardId
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    private Integer rewardid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column turntable_reward_config.date
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    private Date date;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column turntable_reward_config.rewardId
     *
     * @return the value of turntable_reward_config.rewardId
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    public Integer getRewardid() {
        return rewardid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column turntable_reward_config.rewardId
     *
     * @param rewardid the value for turntable_reward_config.rewardId
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    public void setRewardid(Integer rewardid) {
        this.rewardid = rewardid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column turntable_reward_config.date
     *
     * @return the value of turntable_reward_config.date
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column turntable_reward_config.date
     *
     * @param date the value for turntable_reward_config.date
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }
}