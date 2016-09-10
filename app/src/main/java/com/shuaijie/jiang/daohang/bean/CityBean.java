package com.shuaijie.jiang.daohang.bean;

import java.util.List;

public class CityBean {

    private String note;
    private List<Data> data;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String name;
        private List<City> city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<City> getCity() {
            return city;
        }

        public void setCity(List<City> city) {
            this.city = city;
        }

        public static class City {
            private String name;
            private List<String> county;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getCounty() {
                return county;
            }

            public void setCounty(List<String> county) {
                this.county = county;
            }
        }
    }
}
