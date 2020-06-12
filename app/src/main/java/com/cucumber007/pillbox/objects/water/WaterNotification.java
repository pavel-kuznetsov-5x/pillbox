package com.cucumber007.pillbox.objects.water;

import java.util.ArrayList;
import java.util.List;

public class WaterNotification {

    private static List<String> phrases = new ArrayList<>();

    public static String getRandomOne() {
        return phrases.get((int)(Math.random()*phrases.size()));
    }

    static {
        phrases.add("You look thirsty. Take a glass of water.");
        phrases.add("Drink! Drink! Drink!");
        phrases.add("Good time to drink a water.");
        phrases.add("You're such dried up. You need some water");
        phrases.add("Just drink!");
        phrases.add("Let’s drink.");
        phrases.add("Drink more water");
        phrases.add("Motivate! Hydrate! Feel great!");
        phrases.add("Water is your best friend.");
        phrases.add("Drink water, stay alive.");
        phrases.add("Be beautiful. Be healthy. Drink water.");
        phrases.add("Water suppresses appetite.");
        phrases.add("Water helps to reduce cholesterol.");
        phrases.add("Stay young. Stay strong. Drink water.");
        phrases.add("Water helps to tone muscles.");
        phrases.add("Water is necessary for proper digestion.");
        phrases.add("Water helps the liver to function.");
        phrases.add("Water helps soothe digestive problems.");
        phrases.add("The right amount of water causes the body to stop retaining water");
        phrases.add("Water stops you confusing hunger and thirst.");
        phrases.add("Water moistures the skin and makes it radiant and supple.");
        phrases.add("Water washes toxins. Drink it.");
        phrases.add("Stay strong. Drink water.");
        phrases.add("WATER. DRINK IT.");
        phrases.add("Keep calm and drink water.");
        phrases.add("Don’t think - Drink!");
        phrases.add("Hey you, drink some water.");
        phrases.add("Roses are red, violets are blue, drink glass of water, it’s good for you.");
        phrases.add("No money? Drink water. It’s free.");
        phrases.add("Water is the only drink for a wise man.");
        phrases.add("Are you hungry? Drink water.");
        phrases.add("How about a glass of water?");
        phrases.add("Drink water. Surprise your liver.");
        phrases.add("Don't forget drink enough water.");
        phrases.add("Stay fit. Drink water. ");
        phrases.add("Make your skin glow. Drink water.");
        phrases.add("Want to lose weight? Drink water.");
        phrases.add("Stay hydrated, stay healthy, drink water.");
        phrases.add("Trust me, you can drink. © Water");
        phrases.add("Water. Just drink it!");
        phrases.add("You are not hungry. You are just bored. Drink a glass of water.");
        phrases.add("Did you drink in last hour? Do it!");
        phrases.add("Drink much water - feel much power!");
        phrases.add("Don’t miss a moment to have a glass of water");
        phrases.add("What are you waiting for? Drink water.");
        phrases.add("You can watch running water forever, but better drink it");
        phrases.add("Low human fuel.Fill your tank of water.");
        phrases.add("Don’t worry! Drink Water!");
        phrases.add("Be cool as cucumber! Just drink water!");
        phrases.add("3 days without water can kill you. Hurry up to drink.");
        phrases.add("It’s hot out here. Let’s drink some water.");
        phrases.add("Want to be healthy and stay fit? Drink water.");
        phrases.add("For being younger you should drink water now!");
        phrases.add("Make your body happier - Drink water!");
        phrases.add("Go over to your kitchen right now and fill up a glass of water.");
        phrases.add("Water your brain: thirst doth cloud the mind.");
        phrases.add("Drink some water to wash your body.");
        phrases.add("Сarry a water bottle with you as a reminder to drink up.");
        phrases.add("Start your day by drinking one or two glasses of water. Start early, feel better, set the trend for the day.");
    }

}
