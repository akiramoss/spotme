package com.spotme.spot.domain;

import com.spotme.common.AuditableEntity;
import com.spotme.user.domain.User;
import jakarta.persistence.CollectionTable;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A user's personal geolocated bookmark.
 * <p>
 * Invariants:
 * <ul>
 *   <li>{@code owner} is never null — every spot belongs to exactly one user.</li>
 *   <li>{@code visibility} defaults to {@link Visibility#PRIVATE} and has no
 *       active business logic yet (see {@link Visibility}).</li>
 *   <li>{@code imageUrls} holds at most {@value #MAX_IMAGES} entries, fixed
 *       at creation time — there is currently no use case for editing them
 *       after the spot is created.</li>
 * </ul>
 */
@Entity
@Table(name = "spots")
public class Spot extends AuditableEntity {

    public static final int MAX_IMAGES = 5;

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
    @CollectionTable(name = "spot_images", joinColumns = @JoinColumn(name = "spot_id"))
    @OrderColumn(name = "image_order")
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    protected Spot() {
    }

    /**
     * @param imageUrls image URLs in display order; may be null or empty.
     *                  At most {@value #MAX_IMAGES} are allowed.
     * @throws IllegalArgumentException if more than {@value #MAX_IMAGES} URLs are provided
     */
    public Spot(String title, String description, Double latitude, Double longitude,
                Category category, User owner, List<String> imageUrls) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.owner = owner;
        this.visibility = Visibility.PRIVATE;

        if (imageUrls != null) {
            if (imageUrls.size() > MAX_IMAGES) {
                throw new IllegalArgumentException(
                        "A spot can have at most " + MAX_IMAGES + " images, got " + imageUrls.size());
            }
            this.imageUrls = new ArrayList<>(imageUrls);
        }
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

    /** Returns an unmodifiable view of the spot's images, in display order. */
    public List<String> getImageUrls() {
        return List.copyOf(imageUrls);
    }
}
