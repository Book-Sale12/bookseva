package com.bookseva.auth.dto;
import com.bookseva.user.entity.User;


import com.bookseva.user.entity.Role;
import com.bookseva.user.entity.UserStatus;

public class AuthResponse {
	private String accessToken;
	private String refreshToken;
	private UserDto user;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}


	public static class UserDto {
		private Long id;
		private String name;
		private String email;
		private Role role;
		private UserStatus status;
		private Integer trustScore;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public UserStatus getStatus() {
			return status;
		}

		public void setStatus(UserStatus status) {
			this.status = status;
		}

		public Integer getTrustScore() {
			return trustScore;
		}

		public void setTrustScore(Integer trustScore) {
			this.trustScore = trustScore;
		}


		public static class UserDtoBuilder {
			private Long id;
			private String name;
			private String email;
			private Role role;
			private UserStatus status;
			private Integer trustScore;

			UserDtoBuilder() {
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder id(final Long id) {
				this.id = id;
				return this;
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder name(final String name) {
				this.name = name;
				return this;
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder email(final String email) {
				this.email = email;
				return this;
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder role(final Role role) {
				this.role = role;
				return this;
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder status(final UserStatus status) {
				this.status = status;
				return this;
			}

			/**
			 * @return {@code this}.
			 */
			public AuthResponse.UserDto.UserDtoBuilder trustScore(final Integer trustScore) {
				this.trustScore = trustScore;
				return this;
			}

			public AuthResponse.UserDto build() {
				return new AuthResponse.UserDto(this.id, this.name, this.email, this.role, this.status, this.trustScore);
			}

			@Override
			public String toString() {
				return "AuthResponse.UserDto.UserDtoBuilder(id=" + this.id + ", name=" + this.name + ", email=" + this.email + ", role=" + this.role + ", status=" + this.status + ", trustScore=" + this.trustScore + ")";
			}
		}

		public static AuthResponse.UserDto.UserDtoBuilder builder() {
			return new AuthResponse.UserDto.UserDtoBuilder();
		}

		public UserDto() {
		}

		public UserDto(final Long id, final String name, final String email, final Role role, final UserStatus status, final Integer trustScore) {
			this.id = id;
			this.name = name;
			this.email = email;
			this.role = role;
			this.status = status;
			this.trustScore = trustScore;
		}
	}


	public static class AuthResponseBuilder {
		private String accessToken;
		private String refreshToken;
		private UserDto user;

		AuthResponseBuilder() {
		}

		/**
		 * @return {@code this}.
		 */
		public AuthResponse.AuthResponseBuilder accessToken(final String accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		/**
		 * @return {@code this}.
		 */
		public AuthResponse.AuthResponseBuilder refreshToken(final String refreshToken) {
			this.refreshToken = refreshToken;
			return this;
		}

		/**
		 * @return {@code this}.
		 */
		public AuthResponse.AuthResponseBuilder user(final UserDto user) {
			this.user = user;
			return this;
		}

		public AuthResponse build() {
			return new AuthResponse(this.accessToken, this.refreshToken, this.user);
		}

		@Override
		public String toString() {
			return "AuthResponse.AuthResponseBuilder(accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken + ", user=" + this.user + ")";
		}
	}

	public static AuthResponse.AuthResponseBuilder builder() {
		return new AuthResponse.AuthResponseBuilder();
	}

	public AuthResponse() {
	}

	public AuthResponse(final String accessToken, final String refreshToken, final UserDto user) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = user;
	}
}
