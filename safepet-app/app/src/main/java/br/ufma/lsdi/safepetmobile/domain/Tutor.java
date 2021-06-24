package br.ufma.lsdi.safepetmobile.domain;

public class Tutor {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private SafePlace safePlace;

    public Tutor(Long id, String name, String email, String login, String password, SafePlace safePlace) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.safePlace = safePlace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SafePlace getSafePlace() {
        return safePlace;
    }

    public void setSafePlace(SafePlace safePlace) {
        this.safePlace = safePlace;
    }

    //Tutor(Long id, String name, String email, String login, String password, SafePlace safePlace)

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s, %s]", id, name, email, login, password, safePlace);
    }
}
