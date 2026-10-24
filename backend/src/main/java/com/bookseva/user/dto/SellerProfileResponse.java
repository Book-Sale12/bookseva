package com.bookseva.user.dto;
import com.bookseva.user.entity.User;


public class SellerProfileResponse {
    private Long id;
    private String name;
    private String collegeName;
    private String status;
    private Integer trustScore;
    private Double averageRating;
    private long totalReviews;
    private long totalBooksListed;
    private long totalBooksSold;
    private long activeListings;

    SellerProfileResponse(final Long id, final String name, final String collegeName, final String status, final Integer trustScore, final Double averageRating, final long totalReviews, final long totalBooksListed, final long totalBooksSold, final long activeListings) {
        this.id = id;
        this.name = name;
        this.collegeName = collegeName;
        this.status = status;
        this.trustScore = trustScore;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.totalBooksListed = totalBooksListed;
        this.totalBooksSold = totalBooksSold;
        this.activeListings = activeListings;
    }


    public static class SellerProfileResponseBuilder {
        private Long id;
        private String name;
        private String collegeName;
        private String status;
        private Integer trustScore;
        private Double averageRating;
        private long totalReviews;
        private long totalBooksListed;
        private long totalBooksSold;
        private long activeListings;

        SellerProfileResponseBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder collegeName(final String collegeName) {
            this.collegeName = collegeName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder status(final String status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder trustScore(final Integer trustScore) {
            this.trustScore = trustScore;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder averageRating(final Double averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder totalReviews(final long totalReviews) {
            this.totalReviews = totalReviews;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder totalBooksListed(final long totalBooksListed) {
            this.totalBooksListed = totalBooksListed;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder totalBooksSold(final long totalBooksSold) {
            this.totalBooksSold = totalBooksSold;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerProfileResponse.SellerProfileResponseBuilder activeListings(final long activeListings) {
            this.activeListings = activeListings;
            return this;
        }

        public SellerProfileResponse build() {
            return new SellerProfileResponse(this.id, this.name, this.collegeName, this.status, this.trustScore, this.averageRating, this.totalReviews, this.totalBooksListed, this.totalBooksSold, this.activeListings);
        }

        @Override
        public String toString() {
            return "SellerProfileResponse.SellerProfileResponseBuilder(id=" + this.id + ", name=" + this.name + ", collegeName=" + this.collegeName + ", status=" + this.status + ", trustScore=" + this.trustScore + ", averageRating=" + this.averageRating + ", totalReviews=" + this.totalReviews + ", totalBooksListed=" + this.totalBooksListed + ", totalBooksSold=" + this.totalBooksSold + ", activeListings=" + this.activeListings + ")";
        }
    }

    public static SellerProfileResponse.SellerProfileResponseBuilder builder() {
        return new SellerProfileResponse.SellerProfileResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCollegeName() {
        return this.collegeName;
    }

    public String getStatus() {
        return this.status;
    }

    public Integer getTrustScore() {
        return this.trustScore;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public long getTotalReviews() {
        return this.totalReviews;
    }

    public long getTotalBooksListed() {
        return this.totalBooksListed;
    }

    public long getTotalBooksSold() {
        return this.totalBooksSold;
    }

    public long getActiveListings() {
        return this.activeListings;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCollegeName(final String collegeName) {
        this.collegeName = collegeName;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setTrustScore(final Integer trustScore) {
        this.trustScore = trustScore;
    }

    public void setAverageRating(final Double averageRating) {
        this.averageRating = averageRating;
    }

    public void setTotalReviews(final long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public void setTotalBooksListed(final long totalBooksListed) {
        this.totalBooksListed = totalBooksListed;
    }

    public void setTotalBooksSold(final long totalBooksSold) {
        this.totalBooksSold = totalBooksSold;
    }

    public void setActiveListings(final long activeListings) {
        this.activeListings = activeListings;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SellerProfileResponse)) return false;
        final SellerProfileResponse other = (SellerProfileResponse) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getTotalReviews() != other.getTotalReviews()) return false;
        if (this.getTotalBooksListed() != other.getTotalBooksListed()) return false;
        if (this.getTotalBooksSold() != other.getTotalBooksSold()) return false;
        if (this.getActiveListings() != other.getActiveListings()) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$trustScore = this.getTrustScore();
        final Object other$trustScore = other.getTrustScore();
        if (this$trustScore == null ? other$trustScore != null : !this$trustScore.equals(other$trustScore)) return false;
        final Object this$averageRating = this.getAverageRating();
        final Object other$averageRating = other.getAverageRating();
        if (this$averageRating == null ? other$averageRating != null : !this$averageRating.equals(other$averageRating)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$collegeName = this.getCollegeName();
        final Object other$collegeName = other.getCollegeName();
        if (this$collegeName == null ? other$collegeName != null : !this$collegeName.equals(other$collegeName)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SellerProfileResponse;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $totalReviews = this.getTotalReviews();
        result = result * PRIME + (int) ($totalReviews >>> 32 ^ $totalReviews);
        final long $totalBooksListed = this.getTotalBooksListed();
        result = result * PRIME + (int) ($totalBooksListed >>> 32 ^ $totalBooksListed);
        final long $totalBooksSold = this.getTotalBooksSold();
        result = result * PRIME + (int) ($totalBooksSold >>> 32 ^ $totalBooksSold);
        final long $activeListings = this.getActiveListings();
        result = result * PRIME + (int) ($activeListings >>> 32 ^ $activeListings);
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $trustScore = this.getTrustScore();
        result = result * PRIME + ($trustScore == null ? 43 : $trustScore.hashCode());
        final Object $averageRating = this.getAverageRating();
        result = result * PRIME + ($averageRating == null ? 43 : $averageRating.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $collegeName = this.getCollegeName();
        result = result * PRIME + ($collegeName == null ? 43 : $collegeName.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SellerProfileResponse(id=" + this.getId() + ", name=" + this.getName() + ", collegeName=" + this.getCollegeName() + ", status=" + this.getStatus() + ", trustScore=" + this.getTrustScore() + ", averageRating=" + this.getAverageRating() + ", totalReviews=" + this.getTotalReviews() + ", totalBooksListed=" + this.getTotalBooksListed() + ", totalBooksSold=" + this.getTotalBooksSold() + ", activeListings=" + this.getActiveListings() + ")";
    }
}
