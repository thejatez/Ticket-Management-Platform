package com.ticketplatform.dto.response;

public class UserDetailResponse {

    private Long id;
    private String username;
    private String role;
    private Integer ticketCount;

    public UserDetailResponse(Long id, String username, String role, Integer ticketCount) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.ticketCount = ticketCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }
}
