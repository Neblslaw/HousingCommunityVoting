package pl.lodz.p.it.mateusz.gielczynski.UserService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(
//        indexes = {
//                @Index(name = DatabaseIndexConsts.ACCOUNT_LOCATION_ID_INDEX, columnList = DatabaseConsts.ACCOUNT_LOCATION_ID),
//                @Index(name = DatabaseIndexConsts.ACCOUNT_CREATED_BY_INDEX, columnList = DatabaseConsts.ABSTRACT_ENTITY_CREATED_BY),
//                @Index(name = DatabaseIndexConsts.ACCOUNT_MODIFIED_BY_INDEX, columnList = DatabaseConsts.ABSTRACT_ENTITY_MODIFIED_BY)
//        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private String login;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(unique = true, nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String language;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean archived = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Group> groups = new HashSet<>();


    public Account(String login, String passwordHash, String email, String firstName, String lastName, String language) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
    }

    public List<String> getRoles() {
        return groups.stream().flatMap(group -> group.getRoles().stream()).map(Role::getName).toList();
    }
}
