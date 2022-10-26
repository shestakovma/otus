package ru.otus.crm.model;


import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public Address() {
    }

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(this.id, this.street);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String name) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
