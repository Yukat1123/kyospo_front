package com.example.kyoto;

import java.util.List;
import java.util.Random;

public class RandomSpot {

    public Spot getRandomSpot(List<Spot> spots) {

        if (spots == null || spots.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return spots.get(random.nextInt(spots.size()));
    }
}
