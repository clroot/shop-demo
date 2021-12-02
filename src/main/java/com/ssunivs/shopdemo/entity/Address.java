package com.ssunivs.shopdemo.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PACKAGE)
public class Address {

    private String address;

    private String detailArea;

    private String zipcode;
}
