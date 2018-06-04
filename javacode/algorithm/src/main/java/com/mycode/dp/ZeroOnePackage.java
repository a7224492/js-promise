package com.mycode.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jiangzhen
 */
public class ZeroOnePackage {
    private MemoPackage result;

    /**
     * 解决一个0-1背包问题
     *
     * @param itemList      物品列表
     * @param packageWeight 背包重量
     */
    public void resolve(List<Item> itemList, int packageWeight) {
        if (itemList == null || itemList.isEmpty() || packageWeight < 1) {
            return;
        }

        MemoPackage[] memoPackage = new MemoPackage[itemList.size() + 1];
        memoPackage[0] = new MemoPackage(0, 0, 0);

        for (int currItemNum = 1; currItemNum < itemList.size(); ++currItemNum) {
            Item currItem = itemList.get(currItemNum - 1);
            MemoPackage currMemoPackage = new MemoPackage(packageWeight);
            MemoPackage preMemoPackage = memoPackage[currItemNum - 1];
            currMemoPackage.itemList.addAll(preMemoPackage.itemList);

            if (preMemoPackage.allItemWeight + currItem.getWeight() <= currMemoPackage.weight) {
                // 直接把这个道具加入背包
                currMemoPackage.allItemWeight = preMemoPackage.allItemWeight + currItem.getWeight();
                currMemoPackage.maxEarning = preMemoPackage.maxEarning + currItem.getEarning();
                memoPackage[currItemNum] = currMemoPackage;
            } else {
                // 把当前这个道具和背包里面的所有道具进行比较，选出最好的一，选择最优放法
                int maxEarning = preMemoPackage.maxEarning;
                int replaceItemIndex = -1;                   // 被替换的物品下标
                for (int i = 0; i < preMemoPackage.itemList.size(); ++i) {
                    Item itemInPackage = preMemoPackage.itemList.get(i);

                    // 得到替换之后的收益
                    int earningAfterReplace = preMemoPackage.maxEarning + currItem.getEarning() - itemInPackage.getEarning();
                    if (maxEarning < earningAfterReplace && (preMemoPackage.allItemWeight + currItem.getWeight() - currItem.getWeight()) <= packageWeight) {
                        // 目前的最优方案
                        maxEarning = earningAfterReplace;
                        replaceItemIndex = i;
                    }
                }

                // 得到最终的最优方案
                if (replaceItemIndex != -1) {
                    // 有物品被替换了
                    currMemoPackage.itemList.set(replaceItemIndex, currItem);
                    currMemoPackage.allItemWeight = preMemoPackage.allItemWeight + currItem.getWeight() - preMemoPackage.itemList.get(replaceItemIndex).getWeight();
                } else {
                    currMemoPackage.allItemWeight = preMemoPackage.allItemWeight;
                }
                currMemoPackage.maxEarning = maxEarning;
                memoPackage[currItemNum] = currMemoPackage;
            }
        }

        result = memoPackage[itemList.size()];
    }

    public MemoPackage getResult() {
        return result;
    }

    private static class MemoPackage {
        /**
         * 背包内所有物品的最大收益总和
         */
        private int maxEarning;

        /**
         * 背包内所有物品重量总和
         */
        private int allItemWeight;

        /**
         * 背包容量
         */
        private int weight;

        /**
         * 背包内的物品列表
         */
        private List<Item> itemList = new ArrayList<>();

        private MemoPackage(int maxEarning, int allItemWeight, int weight) {
            this.maxEarning = maxEarning;
            this.allItemWeight = allItemWeight;
            this.weight = weight;
        }

        private MemoPackage(int weight) {
            this.weight = weight;
        }
    }

    public static class Item {
        /**
         * 重量
         */
        private int weight;

        /**
         * 收益
         */
        private int earning;

        /**
         * 平均收益
         */
        private float avgEarning;

        public Item(int weight, int earning) {
            this.weight = weight;
            this.earning = earning;
            this.avgEarning = ((float) earning) / weight;
        }

        public int getWeight() {
            return weight;
        }

        public int getEarning() {
            return earning;
        }

        public float getAvgEarning() {
            return avgEarning;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;

            Item item = (Item) o;

            if (getWeight() != item.getWeight()) return false;
            if (getEarning() != item.getEarning()) return false;
            return Float.compare(item.getAvgEarning(), getAvgEarning()) == 0;
        }

        @Override
        public int hashCode() {
            int result = getWeight();
            result = 31 * result + getEarning();
            result = 31 * result + (getAvgEarning() != +0.0f ? Float.floatToIntBits(getAvgEarning()) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "weight=" + weight +
                    ", earning=" + earning +
                    ", avgEarning=" + avgEarning +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ZeroOnePackage{" +
                "result=" + result +
                '}';
    }

    public static void main(String[] args) {
        List<Item> itemList = Arrays.asList(
                new Item(10, 60),
                new Item(20, 100),
                new Item(30, 120)
        );

        ZeroOnePackage zeroOnePackage = new ZeroOnePackage();
        zeroOnePackage.resolve(itemList, 50);
        System.out.println(zeroOnePackage);
    }
}
