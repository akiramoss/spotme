package com.spotme.spot;

import com.spotme.common.AuditableEntity;
import com.spotme.spot.domain.Category;
import com.spotme.spot.domain.Visibility;
import com.spotme.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spots")
public class Spot extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ElementCollection
    @OrderColumn(name = "image_order")
    private List<String> imageUrls = new ArrayList<>();

    protected Spot() {
    }

    public Spot(String title, String description, Double latitude, Double longitude,
                Category category, User owner) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.owner = owner;
        this.visibility = Visibility.PRIVATE;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Category getCategory() {
        return category;
    }

    public User getOwner() {
        return owner;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
