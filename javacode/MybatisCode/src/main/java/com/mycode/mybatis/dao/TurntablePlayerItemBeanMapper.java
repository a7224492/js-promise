package com.mycode.mybatis.dao;

import com.mycode.mybatis.model.TurntablePlayerItemBean;

public interface TurntablePlayerItemBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int deleteByPrimaryKey(Integer roleid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insert(TurntablePlayerItemBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int insertSelective(TurntablePlayerItemBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    TurntablePlayerItemBean selectByPrimaryKey(Integer roleid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKeySelective(TurntablePlayerItemBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table turntable_player_item
     *
     * @mbg.generated Wed Aug 16 13:37:17 CST 2017
     */
    int updateByPrimaryKey(TurntablePlayerItemBean record);
}