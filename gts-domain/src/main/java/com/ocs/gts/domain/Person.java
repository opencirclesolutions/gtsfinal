package com.ocs.gts.domain;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.VisibilityType;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.AttributeOrder;
import com.ocs.dynamo.domain.model.annotation.Model;
import com.ocs.dynamo.functional.domain.Country;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
@Model(displayProperty = "fullName")
@AttributeOrder(attributeNames = { "firstName", "nickName", "lastName", "organization", "born" })
public class Person extends AbstractEntity<Integer> {

    private static final long serialVersionUID = -3436199710873943375L;

    @Id
    @SequenceGenerator(name = "person_id_gen", sequenceName = "person_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_gen")
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(max = 255)
    @Column(name = "nickname")
    @Attribute(displayName = "Nickname")
    private String nickName;

    @NotNull
    @JoinColumn(name = "organization")
    @ManyToOne(fetch = FetchType.LAZY)
    @Attribute(showInTable = VisibilityType.SHOW, complexEditable = true, directNavigation = true)
    private Organization organization;

    @ElementCollection
    @CollectionTable(name = "person_lucky_numbers")
    @Column(name = "lucky_number")
    @Attribute(complexEditable = true, minValue = 10, maxValue = 25)
    private Set<Integer> luckyNumbers = new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date born;

    @NotNull
    @JoinColumn(name = "country_of_origin")
    @ManyToOne(fetch = FetchType.LAZY)
    @Attribute(showInTable = VisibilityType.SHOW, searchable = true, multipleSearch = true, complexEditable = true)
    private Country countryOfOrigin;

    public Date getBorn() {
        return born;
    }

    public Country getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getFirstName() {
        return firstName;
    }

    @Attribute(readOnly = true, main = true)
    public String getFullName() {
        StringBuilder builder = new StringBuilder();
        builder.append(firstName);
        builder.append(" '" + nickName + "' ");
        builder.append(lastName);
        return builder.toString();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Integer> getLuckyNumbers() {
        return luckyNumbers;
    }

    public String getNickName() {
        return nickName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public void setCountryOfOrigin(Country countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLuckyNumbers(Set<Integer> luckyNumbers) {
        this.luckyNumbers = luckyNumbers;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}
