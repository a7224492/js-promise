package com.mycode.mybatis.dao;

import com.mycode.mybatis.model.TurntableRewardConfigBean;
import com.mycode.mybatis.model.TurntableRewardConfigBeanKey;

public interface TurntableRewardConfigBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int deleteByPrimaryKey(TurntableRewardConfigBeanKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insert(TurntableRewardConfigBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insertSelective(TurntableRewardConfigBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    TurntableRewardConfigBean selectByPrimaryKey(TurntableRewardConfigBeanKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKeySelective(TurntableRewardConfigBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_reward_config
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKey(TurntableRewardConfigBean record);
}