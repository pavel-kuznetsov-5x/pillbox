package com.cucumber007.pillbox.objects.gym;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Training {

    public static final int SCOPE_OPEN = 0;
    public static final int SCOPE_HIDDEN = 1;
    public static final int SCOPE_PURCHASED = 2;
    //public static final int SCOPE_PURCHASED = 99;

    public static final int SORT_NEWEST = 0;
    public static final int SORT_RATING = 1;
    public static final int SORT_DOWNLOADS = 2;
    public static final int SORT_PRICE = 3;

    private int id;
    private boolean isPurchased;
    private String title;
    private String previewImageUrl;
    private int typeId;
    private List<Integer> goalIds = new ArrayList<>();
    private String description;
    private int levelId;
    private String duration;
    private int placeId;
    private String producerName;
    private float price;
    private float rating;
    private int ratedQuantity;
    private int purchaseQuantity;
    private boolean forShare;
    private ProducerData producerData;
    private List<TrainingDay> days = new ArrayList<>();

    private boolean isDownloaded = false;
    private List<String> goals = new ArrayList<>();

    private static List<String> possibleGoals = new ArrayList<>();
    private static List<String> possibleLevels = new ArrayList<>();
    private static List<String> possiblePlaces = new ArrayList<>();

    static {
        possibleGoals.add("losing weight");
        possibleGoals.add("muscle building");
        possibleGoals.add("weight control");
        possibleGoals.add("strengthening heart and blood vessels");
        possibleGoals.add("stress release");
        possibleGoals.add("improving overall condition");
        possibleGoals.add("improving body shape");
        possibleGoals.add("increasing flexibility");
        possibleGoals.add("other");

        possibleLevels.add("newbie");
        possibleLevels.add("fan");
        possibleLevels.add("amateur");
        possibleLevels.add("pro");
        possibleLevels.add("sportsman");

        possiblePlaces.add("any");
        possiblePlaces.add("gym");
        possiblePlaces.add("gymnasium");
        possiblePlaces.add("fitness");
        possiblePlaces.add("open space");
        possiblePlaces.add("cross-country");
        possiblePlaces.add("swimming pool");
    }

    public String getGoalFromId(int id) {
        if(id < 0 || id > possibleGoals.size()) id = 8;
        String goal = possibleGoals.get(id);
        goal = goal.toLowerCase();
        goal = "#"+goal;
        goal = goal.replace(' ', '_');
        return goal;
    }

    public String getPriceString() {
        if(forShare) return "Free for share";
        if(price == 0) return "Free";
        return Math.round(price * 100) * 0.01 + " $";
    }

    public String getLevelFromId(int id) {
        if(id < 0 || id > possibleLevels.size()) id = 0;
        String goal = possibleLevels.get(id);
        goal = goal.toLowerCase();
        goal = ""+goal;
        goal = goal.replace(' ', '_');
        return goal;
    }

    public String getPlaceFromId(int id) {
        if(id < 0 || id > possiblePlaces.size()) id = 0;
        String goal = possiblePlaces.get(id);
        goal = goal.toLowerCase();
        goal = ""+goal;
        goal = goal.replace(' ', '_');
        return goal;
    }

    public String getPlace() {return getPlaceFromId(getPlaceId());}

    public String getLevel() {return getLevelFromId(getLevelId());}

    public void sort() {
        for (int i = 0; i < days.size(); i++) {
            TrainingDay day = days.get(i);

            for (int j = 0; j < day.getTrainingSets().size(); j++) {
                TrainingSet trainingSet = day.getTrainingSets().get(j);

                for (int k = 0; k < trainingSet.getSupersets().size(); k++) {
                    Superset superset = trainingSet.getSupersets().get(k);

                    for (int l = 0; l < superset.getSets().size(); l++) {
                        Set set = superset.getSets().get(l);

                        Collections.sort(set.getMedias());
                    }
                    Collections.sort(superset.getSets());
                }
                Collections.sort(trainingSet.getSupersets());
            }
            Collections.sort(day.getTrainingSets());
        }
        Collections.sort(days);
    }

    //////////////////////////////////////////////////////

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getProducerName() {
        return producerName;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getGoalIds() {
        if (goalIds == null) {
            return new ArrayList<>();
        } else return goalIds;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public List<TrainingDay> getDays() {
        return days;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }


    public boolean isForShare() {
        return forShare;
    }

    public ProducerData getProducerData() {
        return producerData;
    }
}
