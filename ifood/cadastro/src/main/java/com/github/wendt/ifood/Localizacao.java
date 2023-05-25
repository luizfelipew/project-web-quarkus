package com.github.wendt.ifood;

import jakarta.persistence.*;

@Entity
@Table(name = "localizacao")
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String latitude;

    public String longitude;
}
