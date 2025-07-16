package com.promptwise.promptchain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.promptwise.promptchain.common.util.json.JacksonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "app_user") // Explicitly map to the app_user table
public class AppUserEntity implements Comparable<AppUserEntity> { // Implemented Comparable

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // For BIGSERIAL in PostgreSQL
  @Column(name = "user_id")
  private final Long userId; // Use Long for BIGSERIAL

  @Column(name = "user_email", unique = true, nullable = false)
  private final String userEmail;

  @Column(name = "password", nullable = false)
  private final String password;

  @Column(name = "user_name", nullable = false)
  private final String userName; // Renamed from 'name' to match table column

  // Changed to Instant for UTC representation
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC") // ISO 8601 UTC format
  @JsonSerialize(using = InstantSerializer.class) // Changed serializer
  @Column(name = "created_at", nullable = false)
  private final Instant createdAt;

  // Changed to Instant for UTC representation
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC") // ISO 8601 UTC format
  @JsonSerialize(using = InstantSerializer.class) // Changed serializer
  @Column(name = "last_updated_at", nullable = false)
  private Instant lastUpdatedAt; // Changed type, not final as it can be updated

  // Private constructor for internal use by factory method
  private AppUserEntity(final Long userId, final String userEmail, final String password,
                        final String userName, final Instant createdAt, final Instant lastUpdatedAt) {
    this.userId = userId;
    this.userEmail = userEmail;
    this.password = password;
    this.userName = userName;
    this.createdAt = createdAt;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @JsonCreator
  public static AppUserEntity create(
          @JsonProperty("userId") final Long userId,
          @JsonProperty("userEmail") final String userEmail,
          @JsonProperty("password") final String password,
          @JsonProperty("userName") final String userName,
          @JsonProperty("createdAt") final Instant createdAt,
          @JsonProperty("lastUpdatedAt") final Instant lastUpdatedAt) {
    return new AppUserEntity(userId, userEmail, password, userName, createdAt, lastUpdatedAt);
  }

  // Getters
  public Long getUserId() {
    return userId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public String getPassword() {
    return password;
  }

  public String getUserName() {
    return userName;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  // Setter for mutable field
  public void setLastUpdatedAt(Instant lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AppUserEntity that = (AppUserEntity) o;
    // Equality based on the unique business key (userEmail)
    return Objects.equals(userEmail, that.userEmail);
  }

  @Override
  public int hashCode() {
    // Hash code based on the unique business key (userEmail)
    return Objects.hash(userEmail);
  }

  /**
   * Compares this AppUserEntity with the specified AppUserEntity for order.
   * Returns a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * <p>
   * The comparison is based on the {@code userEmail} field.
   *
   * @param other the AppUserEntity to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null or its userEmail is null.
   */
  @Override
  public int compareTo(AppUserEntity other) {
    // userEmail is marked as nullable = false, so no explicit null check is strictly needed here
    // assuming JPA ensures non-null values.
    return this.userEmail.compareTo(other.userEmail);
  }

  @Override
  public String toString() {
    return JacksonUtil.getInstance().serializeObjectToJsonString(this);
  }

}
