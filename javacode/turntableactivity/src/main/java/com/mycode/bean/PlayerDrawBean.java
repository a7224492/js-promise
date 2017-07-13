package com.mycode.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jz on 2017/7/13.
 */
public class PlayerDrawBean {
    private int itemCount;
    private List<TurnTableReward> rewards = new ArrayList<TurnTableReward>();

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<TurnTableReward> getRewards() {
        return rewards;
    }
}
