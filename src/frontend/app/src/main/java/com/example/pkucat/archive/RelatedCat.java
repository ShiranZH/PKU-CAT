package com.example.pkucat.archive;

public class RelatedCat {
    public String relation;
    public String name;
    public byte[] avatar;
    RelatedCat(String relation, String name, byte[] avatar) {
        this.name=name;
        this.relation = relation;
        this.avatar=avatar;
    }
}
