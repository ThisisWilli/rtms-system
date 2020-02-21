package com.willi.Bean;

import java.util.Date;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-20 19:49
 **/

public class Order {
    private Integer id;
    private String name;
    private Integer host_id;
    private String host_name;
    private String neighbourhood_group;
    private String neighbourhood;
    private Double latitude;
    private Double longitude;
    private String room_type;
    private Double price;
    private Integer minimum_nights;
    private Integer number_of_reviews;
    private String last_review;
    private Double reviews_per_month;
    private Integer calculated_host_listings_count;
    private Integer availability_365;

    public Order(Integer id, String name, Integer host_id, String host_name, String neighbourhood_group,
                 String neighbourhood, Double latitude, Double longitude, String room_type, Double price,
                 Integer minimum_nights, Integer number_of_reviews, String last_review, Double reviews_per_month,
                 Integer calculated_host_listings_count, Integer availability_365) {
        this.id = id;
        this.name = name;
        this.host_id = host_id;
        this.host_name = host_name;
        this.neighbourhood_group = neighbourhood_group;
        this.neighbourhood = neighbourhood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.room_type = room_type;
        this.price = price;
        this.minimum_nights = minimum_nights;
        this.number_of_reviews = number_of_reviews;
        this.last_review = last_review;
        this.reviews_per_month = reviews_per_month;
        this.calculated_host_listings_count = calculated_host_listings_count;
        this.availability_365 = availability_365;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHost_id() {
        return host_id;
    }

    public void setHost_id(Integer host_id) {
        this.host_id = host_id;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getNeighbourhood_group() {
        return neighbourhood_group;
    }

    public void setNeighbourhood_group(String neighbourhood_group) {
        this.neighbourhood_group = neighbourhood_group;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getMinimum_nights() {
        return minimum_nights;
    }

    public void setMinimum_nights(Integer minimum_nights) {
        this.minimum_nights = minimum_nights;
    }

    public Integer getNumber_of_reviews() {
        return number_of_reviews;
    }

    public void setNumber_of_reviews(Integer number_of_reviews) {
        this.number_of_reviews = number_of_reviews;
    }

    public String getLast_review() {
        return last_review;
    }

    public void setLast_review(String last_review) {
        this.last_review = last_review;
    }

    public Double getReviews_per_month() {
        return reviews_per_month;
    }

    public void setReviews_per_month(Double reviews_per_month) {
        this.reviews_per_month = reviews_per_month;
    }

    public Integer getCalculated_host_listings_count() {
        return calculated_host_listings_count;
    }

    public void setCalculated_host_listings_count(Integer calculated_host_listings_count) {
        this.calculated_host_listings_count = calculated_host_listings_count;
    }

    public Integer getAvailability_365() {
        return availability_365;
    }

    public void setAvailability_365(Integer availability_365) {
        this.availability_365 = availability_365;
    }
}
