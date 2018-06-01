package com.mycode.dp;

import java.util.*;

/**
 * Created by jiangzhen
 */
public class ZeroOnePackage {
    private static Item NULL_ITEM = new Item(0, 0);

    /**
     * 解决一个问题后，背包里所有的物品
     */
    private List<Item> packageItemList = new ArrayList<>();
    private List<Item> unmodifiedPackItemList = Collections.unmodifiableList(packageItemList);

    /**
     * 最大收益
     */
    private int maxEarning = 0;

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

        Set<Item> remainItemList = new HashSet<>(itemList);     // 每次选出一个物品后，剩下的那些物品
        KodPackage[] loopPackage = new KodPackage[packageWeight + 1];
        loopPackage[0] = new KodPackage(0, 0, 0);

        for (int loopPackageWeight = 1; loopPackageWeight <= packageWeight; ++loopPackageWeight) {
            KodPackage kodPackage = new KodPackage(loopPackageWeight);
            KodPackage preKodPackage = loopPackage[loopPackageWeight - 1];
            kodPackage.itemList.addAll(preKodPackage.itemList);

            // 从剩下的物品列表中选出既能放到背包，收益又最大的物品
            Optional<Item> maxItem = remainItemList.stream()
                    .filter(item -> (item.getWeight() + preKodPackage.allItemWeight) <= kodPackage.weight)
                    .max(Comparator.comparing(Item::getEarning));

            if (maxItem.isPresent()) {
                // 存在可以放入背包的物品
                kodPackage.allItemWeight = preKodPackage.allItemWeight + maxItem.get().getWeight();
                kodPackage.maxEarning = preKodPackage.maxEarning + maxItem.get().getEarning();
                kodPackage.itemList.add(maxItem.get());
            } else {
                // 不存在可以放入背包的物品
                kodPackage.allItemWeight = preKodPackage.allItemWeight;
                kodPackage.maxEarning = preKodPackage.maxEarning;
            }

            loopPackage[loopPackageWeight] = kodPackage;
        }

        packageItemList.addAll(loopPackage[packageWeight].itemList);
        maxEarning = loopPackage[packageWeight].maxEarning;
    }

    public List<Item> getUnmodifiedPackItemList() {
        return unmodifiedPackItemList;
    }

    public int getMaxEarning() {
        return maxEarning;
    }

    private static class KodPackage {
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

        private KodPackage(int maxEarning, int allItemWeight, int weight) {
            this.maxEarning = maxEarning;
            this.allItemWeight = allItemWeight;
            this.weight = weight;
        }

        private KodPackage(int weight) {
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
            this.avgEarning = (ea)
        }

        public int getWeight() {
            return weight;
        }

        public int getEarning() {
            return earning;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;

            Item item = (Item) o;

            if (getWeight() != item.getWeight()) return false;
            return getEarning() == item.getEarning();
        }

        @Override
        public int hashCode() {
            int result = getWeight();
            result = 31 * result + getEarning();
            return result;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "weight=" + weight +
                    ", earning=" + earning +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ZeroOnePackage{" +
                "unmodifiedPackItemList=" + unmodifiedPackItemList +
                ", maxEarning=" + maxEarning +
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
