package com.lm.bank.busobj.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user",
        uniqueConstraints= {@UniqueConstraint(name = "email_unique", columnNames = {"email"})})

public class UserDto {
    private String email;
    private String password;
    private Long id;
    private Set<AccountDto> accounts;

    public UserDto() {

    }

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 256)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @GeneratedValue(generator="user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="user_id_seq",sequenceName="user_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email) &&
                Objects.equals(password, userDto.password) &&
                Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, password, id);
    }

    @OneToMany(mappedBy = "owner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public Set<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDto> accounts) {
        this.accounts = accounts;
    }

    @PreRemove
    private void removeTransactionRefs() {
        if (accounts != null) {
            getAccounts().forEach(AccountDto::removeTransactionRefs);
        }
    }


}
