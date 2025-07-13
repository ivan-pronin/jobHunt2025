package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class DesignTwitter355 {

    /*
     * 355. Design Twitter
     * Design a simplified version of Twitter where users can post tweets, follow/unfollow other users, and get the 10 most recent tweets in the user's feed.
     * * Implement the Twitter class:
     * * Twitter() Initializes your twitter object.
     * * * void postTweet(int userId, int tweetId) Composes a new tweet with ID tweetId by the user userId. Each call to this function will be a unique tweet.
     * * * List<Integer> getNewsFeed(int userId) Retrieves the 10 most recent tweet IDs in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user themselves. Tweets must be ordered from most recent to least recent.
     * * * void follow(int followerId, int followeeId) The user with ID followerId now follows the user with ID followeeId. If the operation is invalid, it should be a no-op.
     * * * void unfollow(int followerId, int followeeId) The user with ID followerId unfollows the user with ID followeeId. If the operation is invalid, it should be a no-op.
     * * Constraints:
     * * 1 <= userId, followerId, followeeId <= 500
     * * 0 <= tweetId <= 10^4
     * * At most 3 * 10^4 calls will be made to postTweet, getNewsFeed, follow, and unfollow.
     */
    private static class Twitter {

        private static int timestamp = 0;

        private record Tweet(int id, int time) {

        }

        private final Map<Integer, Set<Integer>> followees = new HashMap<>();

        private final Map<Integer, List<Tweet>> tweets = new HashMap<>();

        public void postTweet(int userId, int tweetId) {
            tweets.putIfAbsent(userId, new ArrayList<>());
            tweets.get(userId).add(new Tweet(tweetId, timestamp++));
        }

        public List<Integer> getNewsFeed(int userId) {
            PriorityQueue<Tweet> minHeap = new PriorityQueue<>(Collections.reverseOrder(Comparator.comparingInt(t -> t.time)));
            followees.computeIfAbsent(userId, (k) -> new HashSet<>()).add(userId);

            for (int followeeId : followees.get(userId)) {
                List<Tweet> tweetList = tweets.getOrDefault(followeeId, new ArrayList<>());
                for (int i = tweetList.size() - 1; i >= 0 && i >= tweetList.size() - 10; i--) {
                    minHeap.offer(tweetList.get(i));
                    if (minHeap.size() > 10) minHeap.poll(); // keep top 10
                }
            }

            List<Integer> result = new LinkedList<>();
            while (!minHeap.isEmpty()) result.add(minHeap.poll().id); // reverse
            return result;
        }

        public void follow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            followees.putIfAbsent(followerId, new HashSet<>());
            followees.get(followerId).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            followees.getOrDefault(followerId, new HashSet<>()).remove(followeeId);
        }

    }

    @Test
    public void testTwitterBasicFlow() {
        Semaphore ss = new Semaphore(1);
        CountDownLatch f = new CountDownLatch(1);
        Phaser ph = new Phaser(1);
        ReentrantLock ll = new ReentrantLock();

        Twitter twitter = new Twitter();

        twitter.postTweet(1, 5);
        assertEquals(List.of(5), twitter.getNewsFeed(1));

        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        List<Integer> feed = twitter.getNewsFeed(1);
        assertEquals(List.of(6, 5), feed);

        twitter.unfollow(1, 2);
        assertEquals(List.of(5), twitter.getNewsFeed(1));
    }

    @Test
    public void testMultipleTweetsAndFollowees() {
        Twitter twitter = new Twitter();
        twitter.postTweet(1, 101);
        twitter.postTweet(1, 102);
        twitter.postTweet(2, 201);
        twitter.postTweet(2, 202);
        twitter.postTweet(3, 301);
        twitter.follow(1, 2);
        twitter.follow(1, 3);

        List<Integer> newsFeed = twitter.getNewsFeed(1);
        assertEquals(5, newsFeed.size());
        assertTrue(newsFeed.contains(101));
        assertTrue(newsFeed.contains(201));
        assertTrue(newsFeed.contains(301));
    }

}
