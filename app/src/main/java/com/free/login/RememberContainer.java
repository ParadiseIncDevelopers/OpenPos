package com.free.login;

public class RememberContainer {
    private final String macAddress;
    private final String id;
    private final String email;
    private final String nameAndSurname;

    private RememberContainer(String macAddress, String id, String email, String nameAndSurname) {
        this.macAddress = macAddress;
        this.id = id;
        this.email = email;
        this.nameAndSurname = nameAndSurname;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public static class Builder {
        private String macAddress;
        private String id;
        private String email;
        private String nameAndSurname;

        public Builder setMacAddress(String macAddress) {
            this.macAddress = macAddress;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setNameAndSurname(String nameAndSurname) {
            this.nameAndSurname = nameAndSurname;
            return this;
        }

        public RememberContainer build() {
            return new RememberContainer(macAddress, id, email, nameAndSurname);
        }
    }
}

