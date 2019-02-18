package malna314.springfeeder.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "roles" )
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;


    @ManyToMany(mappedBy = "roles")
    private Set<AuthUser> users = new HashSet<AuthUser>();

    private Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<AuthUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AuthUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", role=" + role + "]";
    }
}