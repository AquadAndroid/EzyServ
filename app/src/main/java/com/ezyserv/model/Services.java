package com.ezyserv.model;

import java.io.Serializable;

/**
 * Created by SONI on 8/28/2017.
 */

public class Services implements Serializable {
    private static final long serialVersionUID = 124568521541L;
    private String id;
    private String name;
    private String description;
    private String image;
    private Data services;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Data getServices() {
        return services;
    }

    public void setServices(Data services) {
        this.services = services;
    }

    public class Data implements Serializable {
        private static final long serialVersionUID = 12402315L;
        private String id;
        private String service_id;
        private String name;
        private String description;
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
