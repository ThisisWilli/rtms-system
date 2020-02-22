package com.willi.bean;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-22 20:49
 **/

public class Neigh {
    private Integer id;
    private String neighbourhood;

    public Neigh(Integer id, String neighbourhood) {
        this.id = id;
        this.neighbourhood = neighbourhood;
    }

    @Override
    public String toString() {
        return "Neigh{" +
                "id=" + id +
                ", neighbourhood='" + neighbourhood + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }
}
