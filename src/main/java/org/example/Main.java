package org.example;

import org.redisson.api.RScoredSortedSet;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();

        RedisStorage redisStorage = new RedisStorage();
        redisStorage.init();
        redisStorage.registerUsers();

        RScoredSortedSet<Integer> datingUsers = redisStorage.getDatingUsers();

        while (true) {
            int currentUser = datingUsers.first();
            double currentUserScore = datingUsers.firstScore();
            System.out.println("— На главной странице показываем пользователя " + currentUser);
            datingUsers.pollFirst();
            double increment = 0.01;
            datingUsers.addScore(currentUser, currentUserScore + increment);

            if (random.nextInt(10) == 5) {
                int randomIndex = random.nextInt(datingUsers.size());
                int paidUser = datingUsers.valueRange(randomIndex, randomIndex).iterator().next();
                double paidUserScore = datingUsers.getScore(paidUser);
                System.out.println("> Пользователь " + paidUser + " оплатил платную услугу");
                System.out.println("— На главной странице показываем пользователя " + paidUser);
                datingUsers.remove(paidUser);
                datingUsers.addScore(paidUser, paidUserScore + increment);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
