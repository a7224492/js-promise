package com.mycode.mybatis.dao;

import com.mycode.mybatis.model.TurntableActivityRewardBean;
import com.mycode.mybatis.model.TurntableActivityRewardBeanKey;

public interface TurntableActivityRewardBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int deleteByPrimaryKey(TurntableActivityRewardBeanKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insert(TurntableActivityRewardBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insertSelective(TurntableActivityRewardBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    TurntableActivityRewardBean selectByPrimaryKey(TurntableActivityRewardBeanKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKeySelective(TurntableActivityRewardBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_activity_reward
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKey(TurntableActivityRewardBean record);
}