package com.proyecto.customer.mscustomer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer implements Serializable {
    @Id
    private Integer idCustomer;
    private String name;
    private String documentType;
    private String documentNumber;
    private String customerType;
    private static final long serialVersionUID = 1L;
}
